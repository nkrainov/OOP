package org.markdown;

import java.util.ArrayList;

public class Link extends Element{
    private final String url;
    private final String text;

    public static class Builder implements org.markdown.Builder {
        private String url;
        private ArrayList<Text> text;
        private boolean image;

        public Builder() {
            text = new ArrayList<>();
            this.image = false;
            url = "";
        }

        public void urlReplace(String str) {
            url = str;
        }

        public void textAppend(Text text) {
            this.text.add(text);
        }

        public boolean textRemove(int num) {
            return (this.text.remove(num) != null);
        }

        public void isImage(boolean image) {
            this.image = image;
        }

        public Link build() {
            StringBuilder str = new StringBuilder();
            for (Text text : text) {
                str.append(text.toString());
            }

            if (image) {
                return new Image(url, str.toString());
            }
            return new Link(url, str.toString());
        }
    }

    public static class Image extends Link {
        public Image(String url, String text) {
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

            if (obj instanceof Image) {
                Image image = (Image) obj;
                return super.equals(image);
            }

            return false;
        }
    }

    public Builder getBuilder() {
        return new Link.Builder();
    }

    public Link(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getText() {
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

        if (obj instanceof Link) {
            Link link = (Link) obj;
            return link.url.equals(url) && link.text.equals(text);
        }

        return false;
    }
}
