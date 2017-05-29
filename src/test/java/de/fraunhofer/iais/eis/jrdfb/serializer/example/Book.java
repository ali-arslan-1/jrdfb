package de.fraunhofer.iais.eis.jrdfb.serializer.example;

import de.fraunhofer.iais.eis.jrdfb.annotation.RdfMarshaller;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfProperty;
import de.fraunhofer.iais.eis.jrdfb.annotation.RdfType;
import de.fraunhofer.iais.eis.jrdfb.serializer.example.marshaller.MyAuthorMarshaller;

import java.util.Date;

/**
 * @author <a href="mailto:ali.arslan@rwth-aachen.de">AliArslan</a>
 */
@RdfType("bibo:Book")
public class Book {

    @RdfProperty("bibo:book_title")
    String title;

    @RdfProperty("bibo:published_on")
    Date publishedOn;

    @RdfProperty("bibo:author")
    @RdfMarshaller(MyAuthorMarshaller.class)
    Author author;

    public Book(){}

    public Book(String title, Date publishedOn, Author author) {
        this.title = title;
        this.publishedOn = publishedOn;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
