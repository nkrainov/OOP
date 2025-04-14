package org.markdown;

import java.util.ArrayList;

/**
 * Класс, реализовывающий представление ссылок.
 */
public class Link extends Element {
    private final String url;
    private final String text;

    /**
     * Builder для ссылок.
     */
    public static class Builder implements org.markdown.Builder {
        private String url;
        private ArrayList<Text> text;
        private boolean image;

        /**
         * Конструктор.
         */
        public Builder() {
            text = new ArrayList<>();
            this.image = false;
            url = "";
        }

        /**
         * Замена старого url на новый.
         */
        public void urlReplace(String str) {
            url = str;
        }

        /**
         * Добавление текста к описанию ссылки.
         */
        public void textAppend(Text text) {
            this.text.add(text);
        }

        /**
         * Удаление добавленного текста.
         */
        public boolean textRemove(int num) {
            return (this.text.remove(num) != null);
        }

        /**
         * Смена типа ссылки.
         */
        public void isImage(boolean image) {
            this.image = image;
        }

        /**
         * Создание объекта Link.
         */
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

    /**
     * Реализация представления картинок.
     */
    public static class Image extends Link {
        /**
         * Конструктор.
         */
        public Image(String url, String text) {
            super(url, text);
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            return "!" + super.toString();
        }

        /**
         * Проверка равенства.
         */
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

    /**
     * Получения Builder'а.
     */
    @Override
    public Builder getBuilder() {
        return new Link.Builder();
    }

    /**
     * Конструктор.
     */
    public Link(String url, String text) {
        this.url = url;
        this.text = text;
    }

    /**
     * Геттер.
     */
    public String getText() {
        return text;
    }

    /**
     * Геттер.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Сериализация в строку.
     */
    @Override
    public String toString() {
        return "[" + text + "](" + url + ")";
    }

    /**
     * Проверка на равенство.
     */
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
