package de.fraunhofer.iais.eis.jrdfb.serializer.unmarshaller;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.serializer.MemberWrapper;
import de.fraunhofer.iais.eis.jrdfb.vocabulary.IAIS;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public abstract class BasePropUnmarshaller implements PropertyUnmarshaller {
    protected MemberWrapper memberWrapper;
    protected Model model;
    protected RdfUnmarshaller rdfUnmarshaller;

    public BasePropUnmarshaller(Field field, RdfUnmarshaller rdfUnmarshaller) {
        this.memberWrapper = new MemberWrapper(field);
        this.model = rdfUnmarshaller.model;
        this.rdfUnmarshaller = rdfUnmarshaller;
    }

    public BasePropUnmarshaller(Method method, RdfUnmarshaller rdfUnmarshaller) {
        this.memberWrapper = new MemberWrapper(method);
        this.model = rdfUnmarshaller.model;
        this.rdfUnmarshaller = rdfUnmarshaller;
    }

    /**
     * @param resource whose property class name to be resolved
     * @return java class name of property
     * @throws ReflectiveOperationException
     */
    @Nullable
    public String resolveMemberClassName(@NotNull Resource resource)
            throws ReflectiveOperationException{
        Resource metadata = (Resource)resource
                                .getProperty(model.createProperty(IAIS.CLASS_MAPPING)).getObject();
        Statement metaProperty = metadata.getProperty(getJenaProperty());
        if(metaProperty==null)
            return null;

        return metaProperty.getObject().toString();
    }

    protected Property getJenaProperty(){
        return model.createProperty(memberWrapper.getAnnotation(RdfProperty.class).value());
    }
}
