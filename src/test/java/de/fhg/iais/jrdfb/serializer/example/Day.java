package de.fhg.iais.jrdfb.serializer.example;

import de.fhg.iais.jrdfb.annotation.RdfType;

/**
 * @see <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html">https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html</a>
 */

@RdfType("http://www.w3.org/2006/time#day")
public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY
}
