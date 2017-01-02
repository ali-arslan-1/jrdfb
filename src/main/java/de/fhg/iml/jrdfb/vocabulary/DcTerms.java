package de.fhg.iml.jrdfb.vocabulary;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class DcTerms {

    /**
     * The namespace of the vocabulary as a string
     */
    private static final String uri = "http://purl.org/dc/terms/";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    public static final String IDENTIFIER = uri + "identifier";
}
