package org.kuali.maven.mojo.s3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.kuali.maven.common.UrlBuilder;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * <p>
 * This mojo updates a bucket serving as an origin for a Cloud Front distribution. It generates an html directory
 * listing for each "directory" in the bucket and stores the html under a key in the bucket such that a regular http
 * request for a directory returns the html instead of the XML for "object does not exist" Amazon would normally return.
 * For example: The url "http://www.mybucket.com/foo/bar" returns an html page containing a listing of all the files and
 * directories under "foo/bar" in the bucket.
 * </p>
 * <p>
 * If a directory contains an object with a key that is the same as the default object, the plugin copies the object to
 * a key representing the directory structure. For example, the url "http://www.mybucket.com/foo/bar/index.html"
 * represents an object in an S3 bucket under the key "foo/bar/index.html". This plugin will copy the object from the
 * key "foo/bar/index.html" to the key "foo/bar/". This causes the url "http://www.mybucket.com/foo/bar/" to return the
 * same content as the url "http://www.mybucket.com/foo/bar/index.html"
 * </p>
 * <p>
 * It also generates an html directory listing at the root of the bucket hierarchy and places that html into the bucket
 * as the default object, unless a default object already exists.
 * </p>
 *
 * @goal updateoriginbucket
 * @aggregator
 */
public class UpdateOriginBucketMojo extends S3Mojo {

    private static final String S3_INDEX_METADATA_KEY = "maven-cloudfront-plugin-index";
    private static final String S3_INDEX_CONTENT_TYPE = "text/html";
    CloudFrontHtmlGenerator generator;
    S3DataConverter converter;

    /**
     * The groupId for the organization
     *
     * @parameter expression="${organizationGroupId}" default-value="org.kuali"
     */
    private String organizationGroupId;

    /**
     * This controls the caching behavior for CloudFront. By default, CloudFront edge locations cache content from an S3
     * bucket for 24 hours. That interval is shortened to 1 hour for the html indexes generated by this plugin.
     *
     * @parameter expression="${cacheControl}" default-value="max-age=3600, must-revalidate"
     */
    private String cacheControl;

    /**
     * If true, the complete hierarchy underneath <code>prefix</code> will be recursively updated. If false, only the
     * directory corresponding to the prefix will be updated along with the path back to the root of the bucket
     *
     * @parameter expression="${recurse}" default-value="true"
     */
    private boolean recurse;

    /**
     * If true, "foo/bar/index.html" will get copied to "foo/bar/"
     *
     * @parameter expression="${copyDefaultObjectWithDelimiter}" default-value="true"
     */
    private boolean copyDefaultObjectWithDelimiter;

    /**
     * If true, "foo/bar/index.html" will get copied to "foo/bar". This is defaulted to false because the relative
     * pathing in the html generated by the maven-site-plugin does not render correctly from a url without the trailing
     * slash.
     *
     * @parameter expression="${copyDefaultObjectWithoutDelimiter}" default-value="false"
     */
    private boolean copyDefaultObjectWithoutDelimiter;

    /**
     * The stylesheet to use for the directory listing
     *
     * @parameter expression="${css}" default-value="http://s3browse.ks.kuali.org/css/style.css"
     */
    private String css;

    /**
     * Image representing a file
     *
     * @parameter expression="${fileImage}" default-value="http://s3browse.ks.kuali.org/images/page_white.png"
     */
    private String fileImage;

    /**
     * Image representing a directory
     *
     * @parameter expression="${directoryImage}" default-value="http://s3browse.ks.kuali.org/images/folder.png"
     */
    private String directoryImage;

    /**
     * When displaying the last modified timestamp, use this timezone
     *
     * @parameter expression="${timezone}" default-value="GMT"
     */
    private String timezone;

    /**
     * When displaying the last modified timestamp use this format
     *
     * @parameter expression="${dateFormat}" default-value="EEE, dd MMM yyyy HH:mm:ss z"
     */
    private String dateFormat;

    /**
     * The key containing the default object for the Cloud Front distribution. If this object already exists, the plugin
     * will not modify it. If it does not exist, this plugin will generate an html directory listing and place it into
     * the bucket under this key.
     *
     * @parameter expression="${defaultObject}" default-value="index.html";
     */
    private String defaultObject;

    /**
     * The html for browsing a directory will be created under this key
     *
     * @parameter expression="${browseHtml}" default-value="browse.html";
     */
    private String browseHtml;

    @Override
    public void executeMojo() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info("Updating S3 bucket - " + getBucket());
            S3BucketContext context = getS3BucketContext();
            generator = new CloudFrontHtmlGenerator(context);
            converter = new S3DataConverter(context);
            converter.setBrowseHtml(getBrowseHtml());
            if (isRecurse()) {
                getLog().info("Recursing into " + getPrefix());
                recurse(context, getPrefix());
            }
            getLog().info("Updating hierarchy underneath " + getPrefix());
            goUpTheChain(context, getPrefix());
        } catch (Exception e) {
            throw new MojoExecutionException("Unexpected error: ", e);
        }
    }

    protected void goUpTheChain(final S3BucketContext context, final String startingPrefix) throws IOException {
        handleRoot(getS3PrefixContext(context, null));

        if (StringUtils.isEmpty(startingPrefix)) {
            return;
        }

        String[] prefixes = StringUtils.splitByWholeSeparator(startingPrefix, context.getDelimiter());
        if (prefixes.length == 1) {
            return;
        }
        String newPrefix = "";
        for (int i = 0; i < prefixes.length - 2; i++) {
            newPrefix += prefixes[i] + context.getDelimiter();
            updateDirectory(getS3PrefixContext(context, newPrefix));
        }
    }

    protected void updatePrefix() {
        UrlBuilder builder = new UrlBuilder();
        String sitePath = builder.getSitePath(getProject(), getOrganizationGroupId());
        String s = getPrefix();
        if (StringUtils.isEmpty(s)) {
            s = sitePath + "/" + getProject().getVersion() + "/";
        }
        if (s == null) {
            return;
        }
        if (!s.endsWith(getDelimiter())) {
            setPrefix(s + getDelimiter());
        }
    }

    protected S3BucketContext getS3BucketContext() throws MojoExecutionException {
        updateCredentials();
        validateCredentials();
        AWSCredentials credentials = getCredentials();
        AmazonS3Client client = new AmazonS3Client(credentials);
        updatePrefix();
        S3BucketContext context = new S3BucketContext();
        try {
            BeanUtils.copyProperties(context, this);
        } catch (Exception e) {
            throw new MojoExecutionException("Error copying properties", e);
        }
        context.setClient(client);
        context.setLastModifiedDateFormatter(getLastModifiedDateFormatter());
        context.setAbout(getAbout());
        return context;
    }

    /**
     * Create a PutObjectRequest for some html generated by this mojo. The PutObjectRequest sets the content type to
     * S3_INDEX_CONTENT_TYPE, sets the ACL to PublicRead, and adds some custom metadata so we can positively identify it
     * as an object created by this plugin
     */
    protected PutObjectRequest getPutIndexObjectRequest(final String html, final String key) throws IOException {
        InputStream in = new ByteArrayInputStream(html.getBytes());
        ObjectMetadata om = new ObjectMetadata();
        om.setCacheControl(getCacheControl());
        String contentType = S3_INDEX_CONTENT_TYPE;
        om.setContentType(contentType);
        om.setContentLength(html.length());
        om.addUserMetadata(S3_INDEX_METADATA_KEY, "true");
        PutObjectRequest request = new PutObjectRequest(getBucket(), key, in, om);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        return request;
    }

    /**
     * Return a SimpleDateFormat object initialized with the date format and timezone supplied to the mojo
     */
    protected SimpleDateFormat getLastModifiedDateFormatter() {
        SimpleDateFormat sdf = new SimpleDateFormat(getDateFormat());
        sdf.setTimeZone(TimeZone.getTimeZone(getTimezone()));
        return sdf;
    }

    /**
     * Return true if the Collection is null or contains no entries, false otherwise
     */
    protected boolean isEmpty(final Collection<?> c) {
        return c == null || c.size() == 0;
    }

    /**
     * Show some text about this plugin
     */
    protected String getAbout() {
        String date = getLastModifiedDateFormatter().format(new Date());
        PluginDescriptor descriptor = (PluginDescriptor) this.getPluginContext().get("pluginDescriptor");
        if (descriptor == null) {
            // Maven 2.2.1 is returning a null descriptor
            return "Listing generated by the maven-cloudfront-plugin on " + date;
        } else {
            String name = descriptor.getArtifactId();
            String version = descriptor.getVersion();
            return "Listing generated by the " + name + " v" + version + " on " + date;
        }
    }

    /**
     * Create an object in the bucket under a key that lets a normal http request function correctly with CloudFront /
     * S3.<br>
     * Either use the client's object or upload some html created by this plugin<br>
     */
    protected void updateDirectory(final S3PrefixContext context, final boolean isCopyIfExists, final String copyToKey)
            throws IOException {
        S3BucketContext bucketContext = context.getBucketContext();
        AmazonS3Client client = context.getBucketContext().getClient();
        String bucket = bucketContext.getBucket();

        boolean containsDefaultObject = isExistingObject(context.getObjectListing(), context.getDefaultObjectKey());
        if (containsDefaultObject && isCopyIfExists) {
            // Copy the contents of the clients default object
            String sourceKey = context.getDefaultObjectKey();
            String destKey = copyToKey;
            CopyObjectRequest request = getCopyObjectRequest(bucket, sourceKey, destKey);
            getLog().info("Copy: " + sourceKey + " to " + destKey);
            client.copyObject(request);
        } else {
            // Upload our custom content
            PutObjectRequest request = getPutIndexObjectRequest(context.getHtml(), copyToKey);
            getLog().info("Put: " + copyToKey);
            client.putObject(request);
        }
    }

    /**
     * Update this S3 "directory".
     */
    protected void updateDirectory(final S3PrefixContext context) throws IOException {
        String trimmedPrefix = converter.getTrimmedPrefix(context.getPrefix(), context.getBucketContext()
                .getDelimiter());

        // Handle "http://www.mybucket.com/foo/bar/"
        updateDirectory(context, isCopyDefaultObjectWithDelimiter(), context.getPrefix());

        // Handle "http://www.mybucket.com/foo/bar"
        updateDirectory(context, isCopyDefaultObjectWithoutDelimiter(), trimmedPrefix);

        // Handle "http://www.mybucket.com/foo/bar/browse.html"
        // context.getBucketContext().getClient().putObject(getPutIndexObjectRequest(context.getHtml(),
        // context.getBrowseHtmlKey()));
    }

    /**
     * If this is the root of the bucket and the default object either does not exist or was created by this plugin,
     * overwrite the default object with newly generated html. Otherwise, do nothing.
     */
    protected void handleRoot(final S3PrefixContext context) throws IOException {
        if (!context.isRoot()) {
            return;
        }

        AmazonS3Client client = context.getBucketContext().getClient();

        // Handle "http://www.mybucket.com/browse.html"
        PutObjectRequest request1 = getPutIndexObjectRequest(context.getHtml(), context.getBrowseHtmlKey());
        getLog().info("Put: " + context.getBrowseHtmlKey());
        client.putObject(request1);

        boolean isCreateOrUpdateDefaultObject = isCreateOrUpdateDefaultObject(context);
        if (!isCreateOrUpdateDefaultObject) {
            return;
        }

        // Update the default object
        PutObjectRequest request2 = getPutIndexObjectRequest(context.getHtml(), context.getDefaultObjectKey());
        getLog().info("Put: " + context.getDefaultObjectKey());
        client.putObject(request2);
    }

    protected S3PrefixContext getS3PrefixContext(final S3BucketContext context, final String prefix) {
        ListObjectsRequest request = new ListObjectsRequest(context.getBucket(), prefix, null, context.getDelimiter(),
                1000);
        ObjectListing objectListing = context.getClient().listObjects(request);
        List<String[]> data = converter.getData(objectListing, prefix, context.getDelimiter());
        String html = generator.getHtml(data, prefix, context.getDelimiter());
        String defaultObjectKey = StringUtils.isEmpty(prefix) ? getDefaultObject() : prefix + getDefaultObject();
        String browseHtmlKey = StringUtils.isEmpty(prefix) ? getBrowseHtml() : prefix + getBrowseHtml();
        // Is this the root of the bucket?
        boolean isRoot = StringUtils.isEmpty(prefix);

        S3PrefixContext prefixContext = new S3PrefixContext();
        prefixContext.setObjectListing(objectListing);
        prefixContext.setHtml(html);
        prefixContext.setRoot(isRoot);
        prefixContext.setDefaultObjectKey(defaultObjectKey);
        prefixContext.setPrefix(prefix);
        prefixContext.setBucketContext(context);
        prefixContext.setBrowseHtmlKey(browseHtmlKey);
        return prefixContext;
    }

    /**
     * Recurse the hierarchy of a bucket starting at "prefix" and create entries in the bucket corresponding to the
     * directory structure of the hierarchy
     */
    protected void recurse(final S3BucketContext context, final String prefix) throws IOException {
        S3PrefixContext prefixContext = getS3PrefixContext(context, prefix);

        handleRoot(prefixContext);

        // If this is not the root, there is more to do
        if (!prefixContext.isRoot()) {
            updateDirectory(prefixContext);
        }

        // Recurse down the hierarchy
        List<String> commonPrefixes = prefixContext.getObjectListing().getCommonPrefixes();
        for (String commonPrefix : commonPrefixes) {
            recurse(context, commonPrefix);
        }
    }

    /**
     * Return true if the ObjectListing contains an object under "key"
     */
    protected boolean isExistingObject(final ObjectListing objectListing, final String key) {
        List<S3ObjectSummary> summaries = objectListing.getObjectSummaries();
        for (S3ObjectSummary summary : summaries) {
            if (key.equals(summary.getKey())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if there is no object in the ObjectListing under defaultObjectKey.<br>
     * Return true if the object in the ObjectListing was created by this plugin.<br>
     * Return false otherwise.<br>
     */
    protected boolean isCreateOrUpdateDefaultObject(final S3PrefixContext context) {
        if (!isExistingObject(context.getObjectListing(), context.getDefaultObjectKey())) {
            // There is no default object, we are free to create one
            return true;
        }
        S3BucketContext s3Context = context.getBucketContext();
        // There is a default object, but if it was created by this plugin, we
        // still need to update it
        S3Object s3Object = s3Context.getClient().getObject(s3Context.getBucket(), context.getDefaultObjectKey());
        boolean isOurDefaultObject = isOurObject(s3Object);
        IOUtils.closeQuietly(s3Object.getObjectContent());
        if (isOurDefaultObject) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return true if this S3Object was created by this plugin. This is is done by checking the metadata attached to
     * this object for the presence of a custom value.
     */
    protected boolean isOurObject(final S3Object s3Object) {
        ObjectMetadata metadata = s3Object.getObjectMetadata();
        Map<String, String> userMetadata = metadata.getUserMetadata();
        String value = userMetadata.get(S3_INDEX_METADATA_KEY);
        boolean isOurObject = "true".equals(value);
        return isOurObject;
    }

    /**
     * Create a CopyObjectRequest with an ACL set to PublicRead
     */
    protected CopyObjectRequest getCopyObjectRequest(final String bucket, final String sourceKey, final String destKey) {
        CopyObjectRequest request = new CopyObjectRequest(bucket, sourceKey, bucket, destKey);
        request.setCannedAccessControlList(CannedAccessControlList.PublicRead);
        return request;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDefaultObject() {
        return defaultObject;
    }

    public void setDefaultObject(final String defaultCloudFrontObject) {
        this.defaultObject = defaultCloudFrontObject;
    }

    public String getFileImage() {
        return fileImage;
    }

    public void setFileImage(final String fileImage) {
        this.fileImage = fileImage;
    }

    public String getDirectoryImage() {
        return directoryImage;
    }

    public void setDirectoryImage(final String directoryImage) {
        this.directoryImage = directoryImage;
    }

    public String getCss() {
        return css;
    }

    public void setCss(final String css) {
        this.css = css;
    }

    public boolean isCopyDefaultObjectWithDelimiter() {
        return copyDefaultObjectWithDelimiter;
    }

    public void setCopyDefaultObjectWithDelimiter(final boolean copyDefaultObjectWithDelimiter) {
        this.copyDefaultObjectWithDelimiter = copyDefaultObjectWithDelimiter;
    }

    public boolean isCopyDefaultObjectWithoutDelimiter() {
        return copyDefaultObjectWithoutDelimiter;
    }

    public void setCopyDefaultObjectWithoutDelimiter(final boolean copyDefaultObjectWithoutDelimiter) {
        this.copyDefaultObjectWithoutDelimiter = copyDefaultObjectWithoutDelimiter;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(final String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getBrowseHtml() {
        return browseHtml;
    }

    public void setBrowseHtml(final String browseHtml) {
        this.browseHtml = browseHtml;
    }

    /**
     * @return the recurse
     */
    public boolean isRecurse() {
        return recurse;
    }

    /**
     * @param recurse
     * the recurse to set
     */
    public void setRecurse(final boolean recurse) {
        this.recurse = recurse;
    }

    /**
     * @return the organizationGroupId
     */
    public String getOrganizationGroupId() {
        return organizationGroupId;
    }

    /**
     * @param organizationGroupId
     * the organizationGroupId to set
     */
    public void setOrganizationGroupId(final String organizationGroupId) {
        this.organizationGroupId = organizationGroupId;
    }

}
