-- DO NOT COPY AND PASTE THIS COMMENT.  VIOLATORS WILL LOSE COMMIT ACCESS.
-- KEY1:MjAxNC0wMS0wNy1rc3BsLWFkbWluLWxlYXJuaW5ncGxhbi5zcWw=
-- KEY2:UkVGRVJFTkNF
-- TYPE:REFERENCE

INSERT INTO KSPL_LRNG_PLAN_RICH_TEXT(FORMATTED,ID,PLAIN,VER_NBR)
  VALUES ('Admin Learning Plan Test','testPlan1Desc','Admin Learning Plan Test',0)
/

INSERT INTO KSPL_LRNG_PLAN(CREATEID,CREATETIME,ID,OBJ_ID,RT_DESCR_ID,SHARED,STUDENT_ID,TYPE_ID,VER_NBR)
  VALUES ('admin',CURRENT_TIMESTAMP,'testPlan1','obj-lp5','testPlan1Desc',1,'admin','kuali.academicplan.type.plan',0)
/


