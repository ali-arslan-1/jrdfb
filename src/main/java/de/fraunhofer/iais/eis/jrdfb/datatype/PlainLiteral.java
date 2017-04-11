package de.fraunhofer.iais.eis.jrdfb.datatype;

/**
 * Created by christian on 11.04.17.
 */
public class PlainLiteral {

    private String value = "", lang = "";

    public PlainLiteral(String value, String lang) {
        this.value = value;
        this.lang = lang;
    }

    public PlainLiteral(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getLang() {
        return lang;
    }
}
