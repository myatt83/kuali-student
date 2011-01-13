set scan off;

INSERT INTO KSOR_ORG_TYPE (EFF_DT,EXPIR_DT,NAME,TYPE_DESC,TYPE_KEY)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Faculty','A Faculty','kuali.org.Faculty');

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_DESCR,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'1000','Recommends and develops policies and procedures for university-wide curricular standards, reviews catalog offerings and degree requirements, and initiates discussions on future curricular matters. The committee reviews college proposals and makes recommen','UBC Senate Subcommittee on Curricula','COC','Active','kuali.org.COC','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'1001','The Faculty of Land and Food Systems','FacultyLFS','kuali.org.Faculty','TESTUSER',TO_DATE( '20100117145128', 'YYYYMMDDHH24MISS' ),2);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1002','Faculty of Land and Food Systems Curriculum Committee','Land&FoodSystemsCOC','Active','kuali.org.COC','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1003','Agricultural Economics Dept','Agricultural Economics','Active','kuali.org.Department','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1004','Land and Food Systems Dept','Land and Food Systems','Active','kuali.org.Department','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1005','Agricultural Sciences Dept','Agricultural Sciences','Active','kuali.org.Department','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1006','Agricultural Sciences Curriculum Committee','AgriculturalSciencesCOC','Active','kuali.org.COC','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1007','Agricultural Economics Curriculum Committee','AgriculturalEconomicsCOC','Active','kuali.org.COC','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,LNG_NAME,SHRT_NAME,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1008','Land and Food Systems Curriculum Committee','Land&FoodSystemsCOC','Active','kuali.org.COC','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ATTR (ATTR_NAME,ATTR_VALUE,ID,OWNER)
  VALUES ('ks.org.attr.Alias','UBC The Curriculum Committee','1000','1000');

INSERT INTO KSOR_ORG_HIRCHY (DESCR,EFF_DT,EXPIR_DT,ID,NAME,ROOT_ORG)
  VALUES ('Hierarchy used to Manage Curriculum',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'kuali.org.hierarchy.UBCCurriculum','Curriculum','1000');

INSERT INTO KSOR_ORG_HIRCHY_JN_ORG_TYPE (ORG_HIRCHY_ID,ORG_TYPE_ID)
  VALUES ('kuali.org.hierarchy.UBCCurriculum','kuali.org.COC');

INSERT INTO KSOR_ORG_HIRCHY_JN_ORG_TYPE (ORG_HIRCHY_ID,ORG_TYPE_ID)
  VALUES ('kuali.org.hierarchy.UBCCurriculum','kuali.org.Faculty');

INSERT INTO KSOR_ORG_HIRCHY_JN_ORG_TYPE (ORG_HIRCHY_ID,ORG_TYPE_ID)
  VALUES ('kuali.org.hierarchy.UBCCurriculum','kuali.org.Senate');

INSERT INTO KSOR_ORG_HIRCHY_JN_ORG_TYPE (ORG_HIRCHY_ID,ORG_TYPE_ID)
  VALUES ('kuali.org.hierarchy.UBCCurriculum','kuali.org.Program');


INSERT INTO KSOR_ORG_ORG_RELTN_TYPE (EFF_DT,EXPIR_DT,NAME,ORG_HIRCHY,REV_DESCR,REV_NAME,TYPE_DESC,TYPE_KEY)
  VALUES (TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'Curriculum Child','kuali.org.hierarchy.UBCCurriculum','is parent of','is parent of','Indicates that one organization is the child of another organization in the curriculum Hierarchy','kuali.org.UBCCurriculumChild');

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20001231000000', 'YYYYMMDDHH24MISS' ),'1000','1000','1001','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1001','1001','1002','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20100117145128', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1002','1001','1002','Active','kuali.org.Chartered','TESTUSER',TO_DATE( '20100117145128', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1003','1001','1003','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20100117145128', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1004','1001','1004','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1005','1001','1005','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1006','1003','1007','Active','kuali.org.Chartered','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1007','1003','1007','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1008','1004','1008','Active','kuali.org.Chartered','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1009','1004','1008','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1010','1005','1006','Active','kuali.org.Chartered','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_ORG_RELTN (CREATEID,CREATETIME,EFF_DT,EXPIR_DT,ID,ORG,RELATED_ORG,ST,TYPE,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),'1011','1005','1006','Active','kuali.org.UBCCurriculumChild','TESTUSER',TO_DATE( '20090122000000', 'YYYYMMDDHH24MISS' ),1);


INSERT INTO KSOR_ORG_PERS_RELTN (ID,ORG,ORG_PERS_RELTN_TYPE,PERS_ID,ST,VERSIONIND)
  VALUES ('1002','1002','kuali.org.PersonRelation.Chair','edna','Active',1);

INSERT INTO KSOR_ORG_PERS_RELTN (ID,ORG,ORG_PERS_RELTN_TYPE,PERS_ID,ST,VERSIONIND)
  VALUES ('1003','1002','kuali.org.PersonRelation.Member','frank','Active',1);

INSERT INTO KSOR_ORG_PERS_RELTN (ID,ORG,ORG_PERS_RELTN_TYPE,PERS_ID,ST,VERSIONIND)
  VALUES ('1004','1000','kuali.org.PersonRelation.Member','erin','Active',1);

INSERT INTO KSOR_ORG_PERS_RELTN (ID,ORG,ORG_PERS_RELTN_TYPE,PERS_ID,ST,VERSIONIND)
  VALUES ('1005','1000','kuali.org.PersonRelation.Chair','eric','Active',1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,DESCR,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Must be a faculty member, term for 3 years','1000','100',1,'1000','kuali.org.PersonRelation.Chair',1,'Chair of the COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,DESCR,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'Six faculty members and 4 students, terms for 1 year','1001','100',1,'1000','kuali.org.PersonRelation.Member',1,'Member of the COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1002','100',1,'1001','kuali.org.PersonRelation.Dean',1,'Dean of the Land and Food Systems Faculty','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1003','100',1,'1002','kuali.org.PersonRelation.Chair',1,'Chair of the Land and Food Sytems COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1004','100',1,'1002','kuali.org.PersonRelation.Member',1,'Member of the Land and Food Systems COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1005','100',1,'1004','kuali.org.PersonRelation.Chair',1,'Chair of the Land and Food Systems Dept','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1006','100',1,'1008','kuali.org.PersonRelation.Chair',1,'Chair of the Land and Food Systems COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1007','100',1,'1008','kuali.org.PersonRelation.Member',1,'Member of the Land and Food Systems COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);

INSERT INTO KSOR_ORG_POS_RESTR (ATPDURATIONTYPEKEY,CREATEID,CREATETIME,ID,MAX_NUM_RELTN,MIN_NUM_RELTN,ORG,PERS_RELTN_TYPE,TIMEQUANTITY,TTL,UPDATEID,UPDATETIME,VERSIONIND)
  VALUES ('1','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'1008','100',1,'1008','kuali.org.PersonRelation.AdminMember',1,'Administrative Member of the Land and Food Sytems COC','TESTUSER',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),1);
