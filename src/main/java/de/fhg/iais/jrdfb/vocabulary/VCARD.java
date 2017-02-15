package de.fhg.iais.jrdfb.vocabulary;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class VCARD {

    /**
     * The namespace of the vocabulary as a string
     */
    private static final String uri = "https://www.w3.org/2006/vcard/ns#";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    public static final String FN = uri + "fn";
    public static final String STREET_ADDRESS = uri + "street-address";
    public static final String COUNTRY_NAME = uri + "country-name";
    public static final String LOCALITY  = uri + "locality";
    public static final String HAS_ADDRESS  = uri + "hasAddress";
    public static final String INDIVIDUAL  = uri + "Individual";
    public static final String ADDRESS  = uri + "Address";
    public static final String BDAY  = uri + "bday";
    public static final String URL  = uri + "url";

}

