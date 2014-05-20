DECLARE TEMP NUMBER;
BEGIN
  SELECT COUNT(*) INTO TEMP FROM USER_TABLES WHERE TABLE_NAME = 'KSEN_SCHED_RQST_SET_ATTR';
        IF TEMP > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KSEN_SCHED_RQST_SET_ATTR CASCADE CONSTRAINTS PURGE'; END IF;
END;
/




CREATE TABLE KSEN_SCHED_RQST_SET_ATTR
(
        OBJ_ID               VARCHAR2(36) NULL ,
        ATTR_KEY             VARCHAR2(255) NULL ,
        ATTR_VALUE           VARCHAR2(4000) NULL ,
        ID                   VARCHAR2(255) NOT NULL ,
        OWNER_ID             VARCHAR2(255) NULL
)
/


ALTER TABLE KSEN_SCHED_RQST_SET_ATTR
        ADD CONSTRAINT  KSEN_SCHED_RQST_SET_ATTR_P PRIMARY KEY (ID)
/