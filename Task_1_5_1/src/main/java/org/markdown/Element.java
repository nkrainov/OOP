package org.markdown;

import java.io.Serializable;

abstract public class Element implements Serializable {
    @Override
    public String toString() {
        return "MarkDown Element";
    }

    abstract Builder getBuilder();
}
