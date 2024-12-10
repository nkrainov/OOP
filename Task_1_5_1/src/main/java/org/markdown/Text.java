package org.markdown;

import java.util.ArrayList;

/**
 * Реализация текста и его оформлений.
 */
public class Text extends Element {
    private final String str;

    /**
     * Builder для текста.
     */
    public static class Builder implements org.markdown.Builder {
        public static final int TEXT = 0;
        public static final int STRIKETHROUGH = 1;
        public static final int ITALIC = 2;
        public static final int CODE = 3;
        public static final int CODEBLOCK = 4;
        public static final int QUOTE = 5;
        public static final int HEADING1 = 6;
        public static final int HEADING2 = 7;
        public static final int HEADING3 = 8;
        public static final int BOLD = 9;

        private ArrayList<String> texts;
        private int type;

        /**
         * Конструктор.
         */
        public Builder() {
            texts = new ArrayList<>();
            type = 0;
        }

        /**
         * Добавление текста.
         */
        public void append(String text) {
            texts.add(text);
        }

        /**
         * Удаление текста.
         */
        public boolean remove(int num) {
            return texts.remove(num) != null;
        }

        /**
         * Установка типа текста.
         */
        public void setType(int type) {
            if (type < 0 || type > 9) {
                throw new IllegalArgumentException("Invalid type: " + type);
            }

            this.type = type;
        }

        /**
         * Создание текста.
         */
        public Text build() {
            StringBuilder res = new StringBuilder();
            for (String text : texts) {
                res.append(text);
            }

            switch (type) {
                case BOLD:
                    return new Bold(res.toString());
                case STRIKETHROUGH:
                    return new Strikethrough(res.toString());
                case ITALIC:
                    return new Italic(res.toString());
                case CODE:
                    return new Code(res.toString());
                case CODEBLOCK:
                    return new CodeBlock(res.toString());
                case QUOTE:
                    return new Quote(res.toString());
                case HEADING1:
                    return new Heading(res.toString(), 1);
                case HEADING2:
                    return new Heading(res.toString(), 2);
                case HEADING3:
                    return new Heading(res.toString(), 3);
                case TEXT:
                    return new Text(res.toString());
                default:
                    break;
            }

            return new Text(res.toString());
        }
    }

    /**
     * Реализация выделенного текста.
     */
    public static class Bold extends Text {
        /**
         * Конструктор.
         */
        public Bold(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            return "**" + super.str + "**";
        }

        /**
         * Проверка на равенство.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Bold) {
                Bold text = (Bold) obj;
                return super.equals(text);
            }

            return false;
        }
    }

    /**
     * Реализация курсива.
     */
    public static class Italic extends Text {
        /**
         * Конструктор.
         */
        public Italic(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            return "*" + super.str + "*";
        }

        /**
         * Проверка на равенство.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Italic) {
                Italic text = (Italic) obj;
                return super.equals(text);
            }

            return false;
        }
    }

    /**
     * Реализация зачеркнутого текста.
     */
    public static class Strikethrough extends Text {
        /**
         * Конструктор.
         */
        public Strikethrough(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            return "~~" + super.str + "~~";
        }

        /**
         * Проверка на равенство.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Strikethrough) {
                Strikethrough text = (Strikethrough) obj;
                return super.equals(text);
            }

            return false;
        }
    }

    /**
     * Реализация кусочка кода.
     */
    public static class Code extends Text {
        /**
         * Конструктор.
         */
        public Code(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            return "`" + super.str + "`";
        }

        /**
         * Проверка на равенство.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Code) {
                Code text = (Code) obj;
                return super.equals(text);
            }

            return false;
        }
    }

    /**
     * Реализация блока кода.
     */
    public static class CodeBlock extends Text {
        /**
         * Конструктор.
         */
        public CodeBlock(String str) {
            super(str);
        }

        /**
         * сериализация в строку.
         */
        @Override
        public String toString() {
            return "```\n" + super.str + "\n```\n";
        }

        /**
         * Проверка на равенство.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof CodeBlock) {
                CodeBlock text = (CodeBlock) obj;
                return super.equals(text);
            }

            return false;
        }
    }

    /**
     * Реализация цитат.
     */
    public static class Quote extends Text {
        /**
         * Конструктор.
         */
        public Quote(String str) {
            super(str);
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            for (String str : super.str.split("\n")) {
                string.append(">").append(str).append("\n");
            }

            return string.toString();
        }

        /**
         * Проверка на равенство.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Quote) {
                Quote text = (Quote) obj;
                return super.equals(text);
            }

            return false;
        }
    }

    /**
     * Реализация заголовков.
     */
    public static class Heading extends Text {
        private int level = 0;

        /**
         * Конструктор.
         */
        public Heading(String str, int level) {
            super(str);

            if (level < 0 || level > 3) {
                throw new MarkdownCreateException("Incorrect level of heading");
            }

            this.level = level;
        }

        /**
         * Сериализация в строку.
         */
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("#".repeat(Math.max(0, level))).append(" ");
            return str.append(super.str).append("\n").toString();
        }

        /**
         * Получение уровня заголовка.
         */
        public int getLevel() {
            return level;
        }

        /**
         * Сравнение строк.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Heading) {
                Heading text = (Heading) obj;
                return super.equals(text)
                        && text.getLevel() == level;
            }

            return false;
        }
    }

    /**
     * Получение билдера.
     */
    public Builder getBuilder() {
        return new Text.Builder();
    }

    /**
     * Конструктор.
     */
    public Text(String str) {
        this.str = str;
    }

    /**
     * Получение внутренней строки.
     */
    public String getString() {
        return str;
    }

    /**
     * Сериализация в строку.
     */
    @Override
    public String toString() {
        return str;
    }

    /**
     * Проверка на равенство.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Text) {
            Text text = (Text) obj;
            return str.equals(text.getString());
        }

        return false;
    }
}
