

ALTER TABLE KSEN_ATPATP_RELTN
    ADD CONSTRAINT KSEN_ATPATP_RELTN_FK2 FOREIGN KEY (RELATED_ATP_ID)
    REFERENCES KSEN_ATP (ID)
/

ALTER TABLE KSEN_ATPATP_RELTN
    ADD CONSTRAINT KSEN_ATPATP_RELTN_FK1 FOREIGN KEY (ATP_ID)
    REFERENCES KSEN_ATP (ID)
/


ALTER TABLE KSEN_ATPATP_RELTN_ATTR
    ADD CONSTRAINT KSEN_ATPATP_RELTN_ATTR_FK1 FOREIGN KEY (OWNER_ID)
    REFERENCES KSEN_ATPATP_RELTN (ID)
/


ALTER TABLE KSEN_ATPMSTONE_RELTN
    ADD CONSTRAINT KSEN_ATPMSTONE_RELTN_FK1 FOREIGN KEY (ATP_ID)
    REFERENCES KSEN_ATP (ID)
/


ALTER TABLE KSEN_ATP_ATTR
    ADD CONSTRAINT KSEN_ATP_ATTR_FK1 FOREIGN KEY (OWNER_ID)
    REFERENCES KSEN_ATP (ID)
/


ALTER TABLE KSEN_MSTONE_ATTR
    ADD CONSTRAINT FK3DFA6EE1BA0FC113 FOREIGN KEY (OWNER_ID)
    REFERENCES KSEN_MSTONE (ID)
/

