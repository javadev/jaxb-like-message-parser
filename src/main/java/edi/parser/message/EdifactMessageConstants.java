package edi.parser.message;

public interface EdifactMessageConstants {
    public static final String AMADEUS_APOSTROPHE = "\u001C";
    public static final String AMADEUS_PLUS_SIGN = "\u001D";
    public static final String AMADEUS_COLON = "\u001F";
    public static final String AMADEUS_NEW_LINE = "\015\012";

    public static final String TKTREQ = "TKTREQ";
    public static final String TKTRES = "TKTRES";
    public static final String TKCREQ = "TKCREQ";
    public static final String TKCRES = "TKCRES";
    public static final String TKCUAC = "TKCUAC";
    public static final String ITAREQ = "ITAREQ";
    public static final String ITARES = "ITARES";
    public static final String HWPREQ = "HWPREQ";
    public static final String HWPRES = "HWPRES";
    public static final String CLTREQ = "CLTREQ";
    public static final String CLTRES = "CLTRES";
    public static final String CONTRL = "CONTRL";
    public static final String APSINQ = "APSINQ";
    public static final String APSRES = "APSRES";

    public static final String AMADEUS_CODE = "1A";

    public void emptyMethod();
}
