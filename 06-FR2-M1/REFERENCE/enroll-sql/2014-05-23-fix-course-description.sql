-- Fix a course description with a typo for a referenced course code of PHSY455 that should be PHYS455
UPDATE KSEN_LUI SET DESCR_PLAIN = REPLACE(DESCR_PLAIN, 'PHSY', 'PHYS') WHERE DESCR_PLAIN LIKE '%PHSY%'
/
