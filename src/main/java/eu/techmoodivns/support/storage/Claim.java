package eu.techmoodivns.support.storage;

import org.javatuples.Pair;

public class Claim<T> {

    private String property;

    private Pair<Operator, T> subject;

    public Claim(String property, Pair<Operator, T> subject){
        this.property = property;
        this.subject = subject;
    }

    public Claim(String property, Operator operator, T value) {
        this(property, new Pair<>(operator, value));
    }

    public Pair<Operator, T> getSubject() {
        return subject;
    }

    public void setSubject(Pair<Operator, T> subject) {
        this.subject = subject;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public enum Operator {
        EQUAL, GREATER, LESS, GREATER_OR_EQUAL, LESS_OR_EQUAL, NOT_EQUAL
    }
}
