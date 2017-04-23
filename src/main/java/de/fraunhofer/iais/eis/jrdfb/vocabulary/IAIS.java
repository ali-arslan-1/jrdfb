package de.fraunhofer.iais.eis.jrdfb.vocabulary;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class IAIS {

    /**
     * The namespace of the vocabulary as a string
     */
    private static final String uri = "http://jrdfb.iais.fraunhofer.de/vocab/";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    public static final String CLASS_MAPPING = uri + "classMapping";
    public static final String IS_MAPPING_ROOT = uri + "isMappingRoot";
}
