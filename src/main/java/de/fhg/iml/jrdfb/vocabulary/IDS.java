package de.fhg.iml.jrdfb.vocabulary;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class IDS {

    /**
     * The namespace of the vocabulary as a string
     */
    private static final String uri = "http://industrialdataspace.org/2016/10/idsv/core#";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    public static final String SENDER = uri + "sender" ;
    public static final String RECEIVER = uri + "receiver" ;
    public static final String DIGEST = uri + "digest" ;
    public static final String AUTH_TOKEN = uri + "authToken" ;
    public static final String CREATED_AT = uri + "createdAt" ;
    public static final String CUSTOM_PROPERTY = uri + "customProperty" ;
    public static final String FORMAT = uri + "format" ;
    public static final String TRANSFERED_DATASET = uri + "TransferedDataset" ;


}
