package de.fraunhofer.iais.eis.jrdfb.util;

import org.apache.jena.rdf.model.Model;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class TestUtils {

    public static String getModelDiff(Model first, Model second) throws UnsupportedEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Model difference  = first.difference(second);
        if(difference != null)
            difference.write(out, "TURTLE");

        return out.toString("UTF-8");
    }
}
