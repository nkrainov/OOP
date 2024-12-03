package org.markdown;

import java.util.Objects;

public class Link extends Element{
    private final String url;
    private final Text text;

    public static class Builder {
        private String url;
        private Text.Builder text;
        private boolean image;

        public Builder(boolean image) {
            text = new Text.Builder();
            this.image = image;
        }

        public void urlReplace(String str) {
            url = str;
        }

        public void textAppend(Text text) {
            this.text.append(text);
        }

        public boolean textRemove(int num) {
            return this.text.remove(num);
        }

        public void changeType(boolean image) {
            this.image = image;
        }

        public Link build() {
            if (image) {
                return new Image(url, text.build());
            }
            return new Link(url, text.build());
        }
    }

    public static class Image extends Link {
        public Image(String url, Text text) {
            super(url, text);
        }

        @Override
        public String toString() {
            return "!" + super.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Image image) {
                return super.equals(image);
            }

            return false;
        }
    }

    public Link(String url, Text text) {
        this.url = url;
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "[" + text + "](" + url + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Link link) {
            return link.url.equals(url) && link.text.equals(text);
        }

        return false;
    }
}
