---
-- Delete any unique constraints (and their matching index) with auto-generated names and replace them with properly named constraints
---

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLO_LO_JN_LOCATEGORY DROP CONSTRAINT SYS_C0011301'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011301'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE ADD CONSTRAINT KSLO_LO_JN_LOCATEGORY_U1 UNIQUE (LO_ID, LOCATEGORY_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLU RENAME DROP CONSTRAINT SYS_C0011370'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011370'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLU ADD CONSTRAINT KSLU_CLU_U1 UNIQUE (VER_IND_ID, SEQ_NUM)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLURES_JN_RESOPT RENAME DROP CONSTRAINT SYS_C0011378'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011378'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLURES_JN_RESOPT ADD CONSTRAINT KSLU_CLURES_JN_RESOPT_U1 UNIQUE (RES_OPT_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLU_JN_ACCRED RENAME DROP CONSTRAINT SYS_C0011436'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011436'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLU_JN_ACCRED ADD CONSTRAINT KSLU_CLU_JN_ACCRED_U1 UNIQUE (CLU_ACCRED_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLU_JN_CLU_IDENT RENAME DROP CONSTRAINT SYS_C0011441'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011441'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLU_JN_CLU_IDENT ADD CONSTRAINT KSLU_CLU_JN_CLU_IDENT_U1 UNIQUE (ALT_CLU_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLU_JN_CLU_INSTR RENAME DROP CONSTRAINT SYS_C0011444'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011444'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLU_JN_CLU_INSTR ADD CONSTRAINT KSLU_CLU_JN_CLU_INSTR_U1 UNIQUE (CLU_INSTR_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLU_PUBL_JN_CLU_INSTR RENAME DROP CONSTRAINT SYS_C009456'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C009456'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLU_PUBL_JN_CLU_INSTR ADD CONSTRAINT KSLU_CLU_PUBL_JN_CLU_INSTR_U1 UNIQUE (CLU_INSTR_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_CLU_PUBL_VARI RENAME DROP CONSTRAINT SYS_C0011470'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011470'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_CLU_PUBL_VARI ADD CONSTRAINT KSLU_CLU_PUBL_VARI_U1 UNIQUE (VARI_KEY, OWNER)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_MEMSHIP_KSLU_SPARAM RENAME DROP CONSTRAINT SYS_C0011543'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011543'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_MEMSHIP_KSLU_SPARAM ADD CONSTRAINT KSLU_MEMSHIP_KSLU_SPARAM_U1 UNIQUE (SEARCHPARAMETERS_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_RSRC RENAME DROP CONSTRAINT SYS_C0011561'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011561'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_RSRC ADD CONSTRAINT KSLU_RSRC_U1 UNIQUE (RSRC_TYPE_ID, CLU_ID)
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE KSLU_SPARAM_KSLU_SPVALUE RENAME DROP CONSTRAINT SYS_C0011566'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -02443 THEN RAISE; END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP INDEX SYS_C0011566'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -01418 THEN RAISE; END IF;
END;
/

ALTER TABLE KSLU_SPARAM_KSLU_SPVALUE ADD CONSTRAINT KSLU_SPARAM_KSLU_SPVALUE_U1 UNIQUE (VALUES_ID)
/
