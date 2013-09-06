--Attribute Definition for RDL Action.
INSERT INTO KRMS_ATTR_DEFN_T (ATTR_DEFN_ID,ACTV,CMPNT_NM,DESC_TXT,LBL,NM,NMSPC_CD,VER_NBR)
  VALUES ('KS-KRMS-ATTR-DEFN-10000','Y',null,'day','Day', 'day','KS-SYS',1)
/
INSERT INTO KRMS_ATTR_DEFN_T (ATTR_DEFN_ID,ACTV,CMPNT_NM,DESC_TXT,LBL,NM,NMSPC_CD,VER_NBR)
  VALUES ('KS-KRMS-ATTR-DEFN-10001','Y',null,'start.time','Start Time', 'startTime','KS-SYS',1)
/
INSERT INTO KRMS_ATTR_DEFN_T (ATTR_DEFN_ID,ACTV,CMPNT_NM,DESC_TXT,LBL,NM,NMSPC_CD,VER_NBR)
  VALUES ('KS-KRMS-ATTR-DEFN-10002','Y',null,'end.time','End Time', 'endTime','KS-SYS',1)
/
INSERT INTO KRMS_ATTR_DEFN_T (ATTR_DEFN_ID,ACTV,CMPNT_NM,DESC_TXT,LBL,NM,NMSPC_CD,VER_NBR)
  VALUES ('KS-KRMS-ATTR-DEFN-10003','Y',null,'facility','Facility', 'facility','KS-SYS',1)
/
INSERT INTO KRMS_ATTR_DEFN_T (ATTR_DEFN_ID,ACTV,CMPNT_NM,DESC_TXT,LBL,NM,NMSPC_CD,VER_NBR)
  VALUES ('KS-KRMS-ATTR-DEFN-10004','Y',null,'room','Room', 'room','KS-SYS',1)
/

--Action to create Day 1 Session 1 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10000','day1session1','Day 1: 8am - 10am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12037',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10000', 'KS-KRMS-ACTN-10000', 'KS-KRMS-ATTR-DEFN-10000', '1', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10001', 'KS-KRMS-ACTN-10000', 'KS-KRMS-ATTR-DEFN-10001', '46800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10002', 'KS-KRMS-ACTN-10000', 'KS-KRMS-ATTR-DEFN-10002', '54000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10003', 'KS-KRMS-ACTN-10000', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10004', 'KS-KRMS-ACTN-10000', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 3 Session 2 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10001','day3session2','Day 3: 10:30am - 12:30am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12038',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10005', 'KS-KRMS-ACTN-10001', 'KS-KRMS-ATTR-DEFN-10000', '3', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10006', 'KS-KRMS-ACTN-10001', 'KS-KRMS-ATTR-DEFN-10001', '55800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10007', 'KS-KRMS-ACTN-10001', 'KS-KRMS-ATTR-DEFN-10002', '59400000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10008', 'KS-KRMS-ACTN-10001', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10009', 'KS-KRMS-ACTN-10001', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 3 Session 5 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10002','day3session5','Day 3: 6:30pm - 8:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12039',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10010', 'KS-KRMS-ACTN-10002', 'KS-KRMS-ATTR-DEFN-10000', '3', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10011', 'KS-KRMS-ACTN-10002', 'KS-KRMS-ATTR-DEFN-10001', '84600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10012', 'KS-KRMS-ACTN-10002', 'KS-KRMS-ATTR-DEFN-10002', '91800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10013', 'KS-KRMS-ACTN-10002', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10014', 'KS-KRMS-ACTN-10002', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 2 Session 2 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10003','day2session2','Day 2: 10:30am - 12:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12047',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10015', 'KS-KRMS-ACTN-10003', 'KS-KRMS-ATTR-DEFN-10000', '2', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10016', 'KS-KRMS-ACTN-10003', 'KS-KRMS-ATTR-DEFN-10001', '55800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10017', 'KS-KRMS-ACTN-10003', 'KS-KRMS-ATTR-DEFN-10002', '63000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10018', 'KS-KRMS-ACTN-10003', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10019', 'KS-KRMS-ACTN-10003', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 1 Session 3 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10004','day1session3','Day 1: 01:30pm - 03:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12041',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10020', 'KS-KRMS-ACTN-10004', 'KS-KRMS-ATTR-DEFN-10000', '1', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10021', 'KS-KRMS-ACTN-10004', 'KS-KRMS-ATTR-DEFN-10001', '66600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10022', 'KS-KRMS-ACTN-10004', 'KS-KRMS-ATTR-DEFN-10002', '73800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10023', 'KS-KRMS-ACTN-10004', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10024', 'KS-KRMS-ACTN-10004', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 1 Session 4 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10005','day1session4','Day 1: 04:00pm - 06:00pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12045',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10025', 'KS-KRMS-ACTN-10005', 'KS-KRMS-ATTR-DEFN-10000', '1', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10026', 'KS-KRMS-ACTN-10005', 'KS-KRMS-ATTR-DEFN-10001', '75600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10027', 'KS-KRMS-ACTN-10005', 'KS-KRMS-ATTR-DEFN-10002', '82800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10028', 'KS-KRMS-ACTN-10005', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10029', 'KS-KRMS-ACTN-10005', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 1 Session 2 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10006','day1session2','Day 1: 10:30am - 12:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12054',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10030', 'KS-KRMS-ACTN-10006', 'KS-KRMS-ATTR-DEFN-10000', '1', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10031', 'KS-KRMS-ACTN-10006', 'KS-KRMS-ATTR-DEFN-10001', '55800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10032', 'KS-KRMS-ACTN-10006', 'KS-KRMS-ATTR-DEFN-10002', '63000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10033', 'KS-KRMS-ACTN-10006', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10034', 'KS-KRMS-ACTN-10006', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 2 Session 4 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10007','day2session4','Day 2: 04:00pm - 06:00pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12048',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10035', 'KS-KRMS-ACTN-10007', 'KS-KRMS-ATTR-DEFN-10000', '2', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10036', 'KS-KRMS-ACTN-10007', 'KS-KRMS-ATTR-DEFN-10001', '75600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10037', 'KS-KRMS-ACTN-10007', 'KS-KRMS-ATTR-DEFN-10002', '82800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10038', 'KS-KRMS-ACTN-10007', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10039', 'KS-KRMS-ACTN-10007', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 2 Session 5 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10008','day2session5','Day 2: 06:30pm - 08:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12050',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10040', 'KS-KRMS-ACTN-10008', 'KS-KRMS-ATTR-DEFN-10000', '2', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10041', 'KS-KRMS-ACTN-10008', 'KS-KRMS-ATTR-DEFN-10001', '84600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10042', 'KS-KRMS-ACTN-10008', 'KS-KRMS-ATTR-DEFN-10002', '91800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10043', 'KS-KRMS-ACTN-10008', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10044', 'KS-KRMS-ACTN-10008', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/


--Action to create Day 3 Session 1 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10009','day3session1','Day 3: 08:00am - 10:00am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12059',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10045', 'KS-KRMS-ACTN-10009', 'KS-KRMS-ATTR-DEFN-10000', '3', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10046', 'KS-KRMS-ACTN-10009', 'KS-KRMS-ATTR-DEFN-10001', '46800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10047', 'KS-KRMS-ACTN-10009', 'KS-KRMS-ATTR-DEFN-10002', '54000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10048', 'KS-KRMS-ACTN-10009', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10049', 'KS-KRMS-ACTN-10009', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/


--Action to create Day 3 Session 3 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10010','day3session3','Day 3: 01:30pm - 03:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12061',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10050', 'KS-KRMS-ACTN-10010', 'KS-KRMS-ATTR-DEFN-10000', '3', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10051', 'KS-KRMS-ACTN-10010', 'KS-KRMS-ATTR-DEFN-10001', '66600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10052', 'KS-KRMS-ACTN-10010', 'KS-KRMS-ATTR-DEFN-10002', '73800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10053', 'KS-KRMS-ACTN-10010', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10054', 'KS-KRMS-ACTN-10010', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 3 Session 4 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10011','day3session4','Day 3: 04:00pm - 06:00pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12052',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10055', 'KS-KRMS-ACTN-10011', 'KS-KRMS-ATTR-DEFN-10000', '3', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10056', 'KS-KRMS-ACTN-10011', 'KS-KRMS-ATTR-DEFN-10001', '75600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10057', 'KS-KRMS-ACTN-10011', 'KS-KRMS-ATTR-DEFN-10002', '82800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10058', 'KS-KRMS-ACTN-10011', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10059', 'KS-KRMS-ACTN-10011', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 4 Session 3 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10012','day4session3','Day 4: 01:30pm - 03:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12062',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10060', 'KS-KRMS-ACTN-10012', 'KS-KRMS-ATTR-DEFN-10000', '4', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10061', 'KS-KRMS-ACTN-10012', 'KS-KRMS-ATTR-DEFN-10001', '66600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10062', 'KS-KRMS-ACTN-10012', 'KS-KRMS-ATTR-DEFN-10002', '73800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10063', 'KS-KRMS-ACTN-10012', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10064', 'KS-KRMS-ACTN-10012', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 2 Session 1 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10013','day2session1','Day 2: 08:00am - 10:00am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12040',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10065', 'KS-KRMS-ACTN-10013', 'KS-KRMS-ATTR-DEFN-10000', '2', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10066', 'KS-KRMS-ACTN-10013', 'KS-KRMS-ATTR-DEFN-10001', '46800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10067', 'KS-KRMS-ACTN-10013', 'KS-KRMS-ATTR-DEFN-10002', '54000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10068', 'KS-KRMS-ACTN-10013', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10069', 'KS-KRMS-ACTN-10013', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 2 Session 3 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10014','day2session3','Day 2: 01:30pm - 03:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12042',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10070', 'KS-KRMS-ACTN-10014', 'KS-KRMS-ATTR-DEFN-10000', '2', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10071', 'KS-KRMS-ACTN-10014', 'KS-KRMS-ATTR-DEFN-10001', '66600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10072', 'KS-KRMS-ACTN-10014', 'KS-KRMS-ATTR-DEFN-10002', '73800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10073', 'KS-KRMS-ACTN-10014', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10074', 'KS-KRMS-ACTN-10014', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 4 Session 1 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10015','day4session1','Day 4: 08:00am - 10:00am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12043',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10075', 'KS-KRMS-ACTN-10015', 'KS-KRMS-ATTR-DEFN-10000', '4', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10076', 'KS-KRMS-ACTN-10015', 'KS-KRMS-ATTR-DEFN-10001', '46800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10077', 'KS-KRMS-ACTN-10015', 'KS-KRMS-ATTR-DEFN-10002', '54000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10078', 'KS-KRMS-ACTN-10015', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10079', 'KS-KRMS-ACTN-10015', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 4 Session 2 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10016','day4session2','Day 4: 10:30am - 12:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12044',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10080', 'KS-KRMS-ACTN-10016', 'KS-KRMS-ATTR-DEFN-10000', '4', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10081', 'KS-KRMS-ACTN-10016', 'KS-KRMS-ATTR-DEFN-10001', '55800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10082', 'KS-KRMS-ACTN-10016', 'KS-KRMS-ATTR-DEFN-10002', '63000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10083', 'KS-KRMS-ACTN-10016', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10084', 'KS-KRMS-ACTN-10016', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 4 Session 4 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10017','day4session4','Day 4: 04:00pm - 06:00pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12057',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10085', 'KS-KRMS-ACTN-10017', 'KS-KRMS-ATTR-DEFN-10000', '4', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10086', 'KS-KRMS-ACTN-10017', 'KS-KRMS-ATTR-DEFN-10001', '75600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10087', 'KS-KRMS-ACTN-10017', 'KS-KRMS-ATTR-DEFN-10002', '82800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10088', 'KS-KRMS-ACTN-10017', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10089', 'KS-KRMS-ACTN-10017', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 5 Session 1 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10018','day5session1','Day 5: 08:00am - 10:00am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12060',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10090', 'KS-KRMS-ACTN-10018', 'KS-KRMS-ATTR-DEFN-10000', '5', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10091', 'KS-KRMS-ACTN-10018', 'KS-KRMS-ATTR-DEFN-10001', '46800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10092', 'KS-KRMS-ACTN-10018', 'KS-KRMS-ATTR-DEFN-10002', '54000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10093', 'KS-KRMS-ACTN-10018', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10094', 'KS-KRMS-ACTN-10018', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 5 Session 2 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10019','day5session2','Day 5: 10:30am - 12:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12046',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10095', 'KS-KRMS-ACTN-10019', 'KS-KRMS-ATTR-DEFN-10000', '5', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10096', 'KS-KRMS-ACTN-10019', 'KS-KRMS-ATTR-DEFN-10001', '55800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10097', 'KS-KRMS-ACTN-10019', 'KS-KRMS-ATTR-DEFN-10002', '63000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10098', 'KS-KRMS-ACTN-10019', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10099', 'KS-KRMS-ACTN-10019', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 5 Session 3 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10020','day5session3','Day 5: 01:30pm - 03:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12049',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10100', 'KS-KRMS-ACTN-10020', 'KS-KRMS-ATTR-DEFN-10000', '5', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10101', 'KS-KRMS-ACTN-10020', 'KS-KRMS-ATTR-DEFN-10001', '66600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10102', 'KS-KRMS-ACTN-10020', 'KS-KRMS-ATTR-DEFN-10002', '73800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10103', 'KS-KRMS-ACTN-10020', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10104', 'KS-KRMS-ACTN-10020', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 5 Session 4 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10021','day5session4','Day 5: 04:00pm - 06:00pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12051',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10105', 'KS-KRMS-ACTN-10021', 'KS-KRMS-ATTR-DEFN-10000', '5', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10106', 'KS-KRMS-ACTN-10021', 'KS-KRMS-ATTR-DEFN-10001', '75600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10107', 'KS-KRMS-ACTN-10021', 'KS-KRMS-ATTR-DEFN-10002', '82800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10108', 'KS-KRMS-ACTN-10021', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10109', 'KS-KRMS-ACTN-10021', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 6 Session 1 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10022','day6session1','Day 6: 08:00am - 10:00am',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12058',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10110', 'KS-KRMS-ACTN-10022', 'KS-KRMS-ATTR-DEFN-10000', '6', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10111', 'KS-KRMS-ACTN-10022', 'KS-KRMS-ATTR-DEFN-10001', '46800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10112', 'KS-KRMS-ACTN-10022', 'KS-KRMS-ATTR-DEFN-10002', '54000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10113', 'KS-KRMS-ACTN-10022', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10114', 'KS-KRMS-ACTN-10022', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 6 Session 2 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10023','day6session2','Day 6: 10:30am - 12:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12053',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10115', 'KS-KRMS-ACTN-10023', 'KS-KRMS-ATTR-DEFN-10000', '6', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10116', 'KS-KRMS-ACTN-10023', 'KS-KRMS-ATTR-DEFN-10001', '55800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10117', 'KS-KRMS-ACTN-10023', 'KS-KRMS-ATTR-DEFN-10002', '63000000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10118', 'KS-KRMS-ACTN-10023', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10119', 'KS-KRMS-ACTN-10023', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/

--Action to create Day 6 Session 3 RDL
INSERT INTO KRMS_ACTN_T (ACTN_ID,NM,DESC_TXT,TYP_ID,RULE_ID,SEQ_NO,VER_NBR,NMSPC_CD)
  VALUES ('KS-KRMS-ACTN-10024','day6session3','Day 6: 01:30pm - 03:30pm',(SELECT TYP_ID FROM KRMS_TYP_T WHERE NM = 'kuali.krms.action.type.requested.delivery.logistic'),'KS-KRMS-RULE-12056',1,1,'KS-SYS')
/

INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10120', 'KS-KRMS-ACTN-10024', 'KS-KRMS-ATTR-DEFN-10000', '6', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10121', 'KS-KRMS-ACTN-10024', 'KS-KRMS-ATTR-DEFN-10001', '66600000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10122', 'KS-KRMS-ACTN-10024', 'KS-KRMS-ATTR-DEFN-10002', '73800000', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10123', 'KS-KRMS-ACTN-10024', 'KS-KRMS-ATTR-DEFN-10003', '370d2750-db69-4a98-b66c-377c20442f42', 1)
/
INSERT INTO KRMS_ACTN_ATTR_T (ACTN_ATTR_DATA_ID, ACTN_ID, ATTR_DEFN_ID, ATTR_VAL, VER_NBR)
  VALUES ('KS-KRMS-ACTN-ATTR-10124', 'KS-KRMS-ACTN-10024', 'KS-KRMS-ATTR-DEFN-10004', '4cbc813d-b674-44e3-af2c-29051a2c0fef', 1)
/