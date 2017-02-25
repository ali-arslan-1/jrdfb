package de.fraunhofer.iais.eis.jrdfb.serializer.example.ssn;

import java.net.URL;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class SensorOutput {
    ObservationValue hasValue;

    //@property(qu:unit)
    URL unit; // (e.g. dbpedia temperature)
}
