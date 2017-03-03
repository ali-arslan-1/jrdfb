package de.fraunhofer.iais.eis.jrdfb.serializer.util;

import de.fraunhofer.iais.eis.jrdfb.serializer.RdfSerializer;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.*;

/**
 * Created by christian on 03.03.17.
 */
public class SerializerSingleton {

    private static RdfSerializer serializer;

    public static synchronized RdfSerializer getInstance() {
        if (serializer == null) {
            instantiateSerializer();
        }
        return serializer;
    }

    private static void instantiateSerializer() {
        serializer = new RdfSerializer(ClassWithLangLiterals.class, Person.class, Student.class, InterfaceWithEnumImpl.class, ParameterImpl.class, Parameter.class, ParameterDataType.class, Address.class);
    }

}
