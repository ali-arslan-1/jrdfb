package de.fhg.iais.jrdfb.vocabulary;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class OWL {

    /**
     * The namespace of the vocabulary as a string
     */
    private static final String uri = "http://www.w3.org/2002/07/owl#";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    public static final String VERSION_INFO = uri + "versionInfo";

}

