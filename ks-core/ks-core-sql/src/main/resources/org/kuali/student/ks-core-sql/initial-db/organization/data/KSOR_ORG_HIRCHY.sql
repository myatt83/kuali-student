TRUNCATE TABLE KSOR_ORG_HIRCHY DROP STORAGE
/
INSERT INTO KSOR_ORG_HIRCHY (DESCR,EFF_DT,ID,NAME,OBJ_ID,ROOT_ORG,VER_NBR)
  VALUES ('Hierarchy used to Manage Curriculum',TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ),'kuali.org.hierarchy.Curriculum','Curriculum','2E85AD7DFB5D4205B5778ECF594BC809','141',0)
/
