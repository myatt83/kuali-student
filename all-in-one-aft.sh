#!/bin/bash -e

# Jenkins script to perform an all in one AFT test
#
# written by Orlando Ramirez (orlando.ramirezmartinez@utoronto.ca)
# enhanced by Haroon Rafique (haroon.rafique@kuali.org)

# REPO_PREFIX="https://svn.kuali.org/repos/student/enrollment/ks-deployments/tags/builds/ks-deployments-2.1/2.1.1-FR2-M1/"
# maven version 3.2 on jenkins
MVN="mvn"
AFT_URL="https://svn.kuali.org/repos/student/test/functional-automation/sambal/trunk"

# tomcat variables
TOMCAT_VERSION=6.0.41
export CATALINA_HOME=${WORKSPACE}/tomcat
export CATALINA_OPTS="-Xms512m -Xmx4g -XX:MaxPermSize=512m -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -Xloggc:${WORKSPACE}/tomcat/logs/heap.log -XX:HeapDumpPath=${WORKSPACE}/tomcat/logs -XX:+HeapDumpOnOutOfMemoryError"

function usage() {
  cat <<EOT
Usage: $ME

    --debug
        Enable debug messages during execution
    --help
        Print this message and exit.
    --tag_url <url of svn tags>
        required - URL of the deployments directory to pull tags from
    --build_job_url <url of build job>
        required - URL for the CI job that creates the tagged builds
    --aft_url <url for afts>
        URL for AFT code

EOT
    exit 0
}


function dbgprint() {
  if [ $DEBUG -ne 0 ]; then
    set +x
    msg="$(date +%Y-%m-%d-%H:%M):"
    msg="$msg ${FUNCNAME[1]}: "
    for arg in $*
    do
      msg="$msg $arg"
    done
    echo -e "$msg"
    set -x
  fi
}

function get_latest_stable_build() {
  dbgprint

  OPT=""
  if [ $DEBUG -eq 0 ]; then OPT="-s"; fi
  BUILD_NUMBER=$(curl $OPT -k #CI_JOB_URL/lastStableBuild/buildNumber)
  echo "${FUNCNAME[0]}: Latest Stable Build Number found: ${BUILD_NUMBER}"
}

function svn_export() {
  dbgprint
  if [ "$1" ]; then
    dbgprint "Exporting repo $1"

    OPT=""
    if [ $DEBUG -eq 0 ]; then OPT="-q"; fi
    svn $OPT export --force $1 $2
    echo "${FUNCNAME[0]}: svn repo $1 successfully exported"
  fi
}

function initialize_config() {
  dbgprint

  # rice.keystore
  cp -p \
    ${WORKSPACE}/ks-with-rice-bundled-${BUILD_NUMBER}/src/main/resources/rice.keystore \
    /rice.keystore

  # config file in ~/kuali/main/dev
  mkdir -p ~/kuali/main/dev
  filepath=src/main/resources/org/kuali/student/ks-deployment-resources/deploy/config
  cp -p \
    ${WORKSPACE}/ks-deployment-resources-${BUILD_NUMBER}/${filepath}/ks-with-rice-bundled-config.xml \
    ~/kuali/main/dev/ks-with-rice-bundled-config.xml

  # do some substitutions
  sed -i.bak \
    -e 's#${public.url}#http://localhost:8080#g' \
    -e 's#${jdbc.url}#jdbc:oracle:thin:@oracle.ks.kuali.org:1521:ORACLE#g' \
    -e "s#\${jdbc.username}#KSAFT${BUILD_NUMBER}#g" \
    -e "s#\${jdbc.password}#KSAFT${BUILD_NUMBER}#g" \
    -e 's#${jdbc.pool.size.max}#20#g' \
    -e 's#${rice.krad.dev.mode}#false#g' \
    -e 's#${rice.krad.script.cleanup}#false#g' \
    -e 's#${keystore.file.default}#/rice.keystore#g' \
        ~/kuali/main/dev/ks-with-rice-bundled-config.xml

  dbgprint $(cat ~/kuali/main/dev/ks-with-rice-bundled-config.xml)

  echo "${FUNCNAME[0]}: config initialized successfully"
}

function initialize_schema() {
  dbgprint
  cd ${WORKSPACE}/ks-impex-bundled-db-build-${BUILD_NUMBER}

  # load bundled schema
  set -x
  ${MVN} initialize -Pdb,oracle,sonar \
    -Djdbc.username=KSAFT${BUILD_NUMBER} -Djdbc.username=KSAFT${BUILD_NUMBER}
  set +x
}

function download_tomcat() {
  dbgprint
  OPT=""
  if [ $DEBUG -eq 0 ]; then OPT="--no-verbose"; fi

  wget $OPT -O /tmp/apache-tomcat-$TOMCAT_VERSION.tar.gz \
    http://archive.apache.org/dist/tomcat/tomcat-6/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz

  # stop building if md5sum does not match
  echo "aaf541df90a5f6160e43177ba8e6ec1e  /tmp/apache-tomcat-$TOMCAT_VERSION.tar.gz" | \
    md5sum -c
  echo "${FUNCNAME[0]}: download tomcat $TOMCAT_VERSION."
}

function extract_tomcat() {
  dbgprint

  # detect if tomcat is already running
  # temporarily override immediate exit
  set +e
  pgrep -fl catalina
  # found any processes
  if [ $? -eq 0 ]; then
    echo "found existing tomcat process. Killing it."
    # kill running catalina process
    pkill -9 -f catalina
    rm -rf ${WORKSPACE}/tomcat
  fi
  set -e
  mkdir -p ${WORKSPACE}/tomcat

  tar xzf /tmp/apache-tomcat-$TOMCAT_VERSION.tar.gz --strip-components=1 \
    -C ${WORKSPACE}/tomcat
  chmod +x ${WORKSPACE}/tomcat/bin/*.sh
  echo "${FUNCNAME[0]}: extracted tomcat to ${WORKSPACE}/tomcat."
}

function install_maven_dependency_plugin() {
  dbgprint

  set -x
  ${MVN} org.apache.maven.plugins:maven-dependency-plugin:2.8:get \
        -Dartifact=org.apache.maven.plugins:maven-dependency-plugin:2.8:jar
  set +x

  echo "${FUNCNAME[0]}: installed maven dependency plugin."
}

function install_oracle_driver() {
  dbgprint

  set -x
  ${MVN} org.apache.maven.plugins:maven-dependency-plugin:2.8:copy \
    -Dartifact=com.oracle:ojdbc6_g:11.2.0.2:jar \
    -DoutputDirectory=${WORKSPACE}/tomcat/lib
  set +x
  echo "${FUNCNAME[0]}: installed oracle driver."
}

function setup_tomcat() {
  dbgprint

  rm -rf ${WORKSPACE}/tomcat/webapps/*
  cat > ${WORKSPACE}/tomcat/conf/server.xml <<EOT
<?xml version='1.0' encoding='utf-8'?>
<Server port="8005" shutdown="SHUTDOWN">
  <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
  <Listener className="org.apache.catalina.core.JasperListener" />
  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
  <Listener className="org.apache.catalina.mbeans.ServerLifecycleListener" />
  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
  <Service name="Catalina">
    <Connector port="8080" protocol="HTTP/1.1" 
               connectionTimeout="20000" 
               redirectPort="8443" />
    <Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
    <Engine name="Catalina" defaultHost="localhost">
      <Host name="localhost"  appBase="webapps"
            unpackWARs="true" autoDeploy="true"
            xmlValidation="false" xmlNamespaceAware="false">
        <Context docBase="${WORKSPACE}/tomcat" path="/tomcat"/>
        <Context docBase="\${user.home}" path="/home"/>
      </Host>
    </Engine>
  </Service>
</Server>
EOT
  echo "${FUNCNAME[0]}: tomcat setup finished."
}

function install_war_artifact() {
  dbgprint

  ${MVN} org.apache.maven.plugins:maven-dependency-plugin:2.8:copy \
    -Dartifact=org.kuali.student.web:ks-with-rice-bundled:2.1.1-FR2-M1-build-$BUILD_NUMBER:war \
    -DoutputDirectory=.

  cp -p ks-with-rice-bundled-2.1.1-FR2-M1-build-$BUILD_NUMBER.war \
    ${WORKSPACE}/tomcat/webapps/ROOT.war
  echo "${FUNCNAME[0]}: installation of war finished."
}

function start_tomcat() {
  dbgprint

  # start up tomcat
  dbgprint ${CATALINA_HOME}/bin/startup.sh
  echo "Staring Tomcat..."
  ${CATALINA_HOME}/bin/startup.sh

  # timeout will be exceeded in LIMIT*SLEEP seconds
  LIMIT=30
  SLEEP=10

  # temporarily override immediate exit
  set +e
  a=0
  while [ $a -le "$LIMIT" ]
  do
    remaining=$((($LIMIT-$a)*$SLEEP))
    a=$(($a+1))
    website=$(curl -s -k --max-time $SLEEP --connect-timeout $SLEEP http://localhost:8080/login.jsp)
    echo $website| grep 'Kuali Student Login'
    if [ $? -eq 0 ]
    then
      break
    else
      echo "Tomcat is not up (timeout in $remaining seconds)"
    fi
    sleep $SLEEP
  done
  set -e
   
  if [ $a -ge "$LIMIT" ]
  then
    echo "Timeout of $(($LIMIT*$SLEEP)) seconds exceeded"
    exit 1
  fi

  echo "${FUNCNAME[0]}: tomcat startup finished."
}

function run_afts() {
  dbgprint

  echo "${FUNCNAME[0]}: Running AFTs"
  cd ${WORKSPACE}
  ${WORKSPACE}/jenkinscucumber \
    http://localhost:8080 \
    headless_data \
    headless \
    --threads=4 \
    --parallel=true \
    --firefox=27
  echo "${FUNCNAME[0]}: AFTs finished."
}

function shutdown_tomcat() {
  dbgprint

  pkill -9 -f catalina
  echo "${FUNCNAME[0]}: tomcat shutdown finished."
}

function set_debug_mode {
  let "DEBUG+=1"
  set -x
}

function process_args {
  DEBUG=0
  BUILD_NUMBER=0

  ARGS=$(getopt --options h --longoptions "help,debug,tag_url,build_job_url,aft_url" --name "$ME" -- "$@");
  if [ $? -ne 0 ]; then usage; fi
  eval set -- "$ARGS";
  while true
  do
    case "$1" in
      -h|--help)
        usage;
        shift;
        ;;
      --debug)
        set_debug_mode
        shift;
        ;;
      --tag_url)
        shift;
        REPO_PREFIX=$2
        ;;
      --build_job_url)
        shift;
        CI_JOB_URL=$2
        ;;
      --aft_url)
        shift;
        AFT_URL=$2
        ;;
      --)
        shift;
        break;
    esac
  done
  if [ -z "$REPO_PREFIX" ];
  then
    echo "repository url not set"
    usage;
  fi
  
  if [ -z "$CI_JOB_URL" ];
  then
    echo "ci job url not set"
    usage;
  fi

}
ME=$(basename $0)

# process all of our command line arguments
process_args "$@"

# retrieve latest stable build number
get_latest_stable_build

# export impex sources from svn repo to specified path
svn_export \
  ${REPO_PREFIX}/build-${BUILD_NUMBER}/ks-dbs/ks-impex/ks-impex-bundled-db \
  ${WORKSPACE}/ks-impex-bundled-db-build-${BUILD_NUMBER}

# export deployment sources from svn repo to specified path
svn_export \
  ${REPO_PREFIX}/build-${BUILD_NUMBER}/ks-deployment-resources \
  ${WORKSPACE}/ks-deployment-resources-${BUILD_NUMBER}

# export ks-with-rice-bundled sources from svn repo to specified path
svn_export \
  ${REPO_PREFIX}/build-${BUILD_NUMBER}/ks-web/ks-with-rice-bundled \
  ${WORKSPACE}/ks-with-rice-bundled-${BUILD_NUMBER}

# export sambal sources from svn repo to specified path
svn_export \
  $AFT_URL \
  ${WORKSPACE}

# initialize config file
initialize_config

# download tomcat
download_tomcat

# extract tomcat
extract_tomcat

# install maven dependency plugin
install_maven_dependency_plugin

# install oracle driver
install_oracle_driver

# setup tomcat
setup_tomcat

# install war artifact
install_war_artifact

# initialize schema (using impex)
initialize_schema

# start tomcat
start_tomcat

# run AFTs
run_afts

# shutdown tomcat
shutdown_tomcat

# vim: tabstop=2 shiftwidth=2
