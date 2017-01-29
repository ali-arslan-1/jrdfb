package de.fhg.iais.jrdfb.serializer.example.ssn;

import java.util.Collection;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class Sensor {
   // @property(rdfs:label)
    String label; //(e.g, "TemperatureIndoor")

    //@property(ssn:madeObservation)
    Collection<Observation> observations;
}
