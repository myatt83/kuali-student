---Update Standard Final Exam  day 5 session 4 16:00 -18:00
UPDATE KRMS_ACTN_ATTR_T ATT
SET ATT.ATTR_VAL = '57600000' 
WHERE ATT.ACTN_ATTR_DATA_ID IN ( 'KS-KRMS-ACTN-ATTR-10231','KS-KRMS-ACTN-ATTR-10106')
/

---Update Common Final Exam Day 2 session 5 18:30-20:30
UPDATE KRMS_ACTN_ATTR_T ATT
SET ATT.ATTR_VAL = '73800000' 
WHERE ATT.ACTN_ATTR_DATA_ID IN ('KS-KRMS-ACTN-ATTR-10167', 'KS-KRMS-ACTN-ATTR-10042')
/