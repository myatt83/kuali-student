-- KSENROLL-13389 add missing subterm ref data
-- create exam period atp
insert into KSEN_ATP (ATP_STATE, ATP_TYPE, CREATEID, CREATETIME, DESCR_FORMATTED, DESCR_PLAIN, END_DT, ID, NAME, OBJ_ID, START_DT, UPDATEID, UPDATETIME, VER_NBR) values ('kuali.atp.state.Official', 'kuali.atp.type.ExamPeriod', 'admin', TO_DATE( '20140702', 'YYYYMMDD'), 'Final Exam Period Fall 2012 Sub Term 1', 'Final Exam Period Fall 2012 Sub Term 1', TO_DATE( '20140702', 'YYYYMMDD'), 'a369c002-ce4d-4050-8394-9839c72a126e', 'Final Exam Period Fall 2012 Sub Term 1', '9111b5a1-96b7-4e36-9f2b-d742832dca84', TO_DATE( '20140702', 'YYYYMMDD'), 'admin', TO_DATE( '20140702', 'YYYYMMDD'), 0);
/
insert into KSEN_ATPATP_RELTN (ID, OBJ_ID, ATP_TYPE, ATP_STATE, ATP_ID, RELATED_ATP_ID, EFF_DT, EXPIR_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID) values ('5f68535f-ef84-4e3e-8b4f-609a5120518d', '121a56c3-d1ab-4569-beec-ee8ae471aa24', 'kuali.atp.atp.relation.associated.term2examperiod', 'kuali.atp.atp.relation.state.active', 'kuali.atp.2012HalfFall1', 'a369c002-ce4d-4050-8394-9839c72a126e', TO_DATE( '20140702', 'YYYYMMDD'), null, 0, TO_DATE( '20140702', 'YYYYMMDD'), 'admin', TO_DATE( '20140702', 'YYYYMMDD'), 'admin');
/
insert into KSEN_ATP (ATP_STATE, ATP_TYPE, CREATEID, CREATETIME, DESCR_FORMATTED, DESCR_PLAIN, END_DT, ID, NAME, OBJ_ID, START_DT, UPDATEID, UPDATETIME, VER_NBR) values ('kuali.atp.state.Official', 'kuali.atp.type.ExamPeriod', 'admin', TO_DATE( '20140702', 'YYYYMMDD'), 'Final Exam Period Fall 2012 Sub Term 2', 'Final Exam Period Fall 2012 Sub Term 2', TO_DATE( '20140702', 'YYYYMMDD'), 'dd286803-1bf2-429b-8ecb-0a22098b10a2', 'Final Exam Period Fall 2012 Sub Term 2', '3ba8e603-c16f-4106-855f-5c4d850d1843', TO_DATE( '20140702', 'YYYYMMDD'), 'admin', TO_DATE( '20140702', 'YYYYMMDD'), 0);
/
insert into KSEN_ATPATP_RELTN (ID, OBJ_ID, ATP_TYPE, ATP_STATE, ATP_ID, RELATED_ATP_ID, EFF_DT, EXPIR_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID) values ('a6104d76-7d8e-4181-bde8-cf9ead745257', 'f1f1d2a9-0fcf-4044-a635-3a8a7d4002c2', 'kuali.atp.atp.relation.associated.term2examperiod', 'kuali.atp.atp.relation.state.active', 'kuali.atp.2012HalfFall2', 'dd286803-1bf2-429b-8ecb-0a22098b10a2', TO_DATE( '20140702', 'YYYYMMDD'), null, 0, TO_DATE( '20140702', 'YYYYMMDD'), 'admin', TO_DATE( '20140702', 'YYYYMMDD'), 'admin');
/




