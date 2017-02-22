package de.fhg.iais.jrdfb.serializer.example;
import de.fhg.iais.jrdfb.annotation.RdfType;
import de.fhg.iais.jrdfb.annotation.RdfUri;

@RdfType("http://www.w3.org/2006/time#day")
public enum DayEnum {
    @RdfUri("http://www.example.com/day/sunday")
    SUNDAY,
    @RdfUri("http://www.example.com/day/monday")
    MONDAY,
    @RdfUri("http://www.example.com/day/tuesday")
    TUESDAY,
    @RdfUri("http://www.example.com/day/wednesday")
    WEDNESDAY,
    @RdfUri("http://www.example.com/day/thursday")
    THURSDAY,
    @RdfUri("http://www.example.com/day/friday")
    FRIDAY,
    @RdfUri("http://www.example.com/day/saturday")
    SATURDAY
}
