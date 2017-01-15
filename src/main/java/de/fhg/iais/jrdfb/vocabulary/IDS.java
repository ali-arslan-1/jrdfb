package de.fhg.iais.jrdfb.vocabulary;

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

    public static final String TRANSFERED_DATASET = uri + "TransferedDataset" ;

    public static final String SENDER = uri + "sender" ;
    public static final String RECEIVER = uri + "receiver" ;
    public static final String DIGEST = uri + "digest" ;
    public static final String AUTH_TOKEN = uri + "authToken" ;
    public static final String CREATED_AT = uri + "createdAt" ;
    public static final String CUSTOM_PROPERTY = uri + "customProperty" ;
    public static final String FORMAT = uri + "format" ;

    // BrokerMessage Vocabulary
    public static final String BROKER_DATA_REQUEST = uri + "BrokerDataRequest" ;

    public static final String DATA_REQUEST_ACTION = uri + "dataRequestAction" ;
    public static final String REQUEST_ID = uri + "requestId" ;
    public static final String PAYLOAD = uri + "payload" ;


}
