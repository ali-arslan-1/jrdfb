package de.fraunhofer.iais.eis.jrdfb.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
public class Utils {
    public static String pluckIdFromUri(@NotNull String uri, @NotNull String template){
        if(template.isEmpty()) return uri;

        Pattern pattern = Pattern.compile(template.replace("{RdfId}", "(.*)"));
        Matcher matcher = pattern.matcher(uri);
        while (matcher.find()){
            return matcher.group(1);
        }
        return null;
    }
}
