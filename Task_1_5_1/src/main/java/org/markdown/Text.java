package org.markdown;

import java.util.ArrayList;

public class Text extends Element {
    private final String str;

    public static class Builder implements org.markdown.Builder {
        ArrayList<Text> texts;

        public Builder() {
            texts = new ArrayList<>();
        }

        public void append(Text text) {
            texts.add(text);
        }

        public boolean remove(int num) {
            return texts.remove(num) != null;
        }

        public Text build() {
            StringBuilder res = new StringBuilder();
            for (Text text : texts) {
                res.append(text);
            }

            return new Text(res.toString());
        }
    }

    public static class Bold extends Text {
        public Bold(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        @Override
        public String toString() {
            return "**" + super.str + "**";
        }

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

    public static class Italic extends Text {
        public Italic(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        @Override
        public String toString() {
            return "*" + super.str + "*";
        }

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

    public static class Strikethrough extends Text {
        public Strikethrough(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        @Override
        public String toString() {
            return "~~" + super.str + "~~";
        }

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

    public static class Code extends Text {
        public Code(String str) {
            super(str);
            if (str.contains("\n")) {
                throw new MarkdownCreateException("String contains \\n");
            }
        }

        @Override
        public String toString() {
            return "`" + super.str + "`";
        }

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

    public static class CodeBlock extends Text {
        public CodeBlock(String str) {
            super(str);
        }

        @Override
        public String toString() {
            return "```\n" + super.str + "\n```\n";
        }

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

    public static class Quote extends Text {
        public Quote(String str) {
            super(str);
        }

        @Override
        public String toString() {
            StringBuilder string = new StringBuilder();
            for (String str : super.str.split("\n")) {
                string.append(">").append(str).append("\n");
            }

            return string.toString();
        }

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

    public static class Heading extends Text {
        private int level = 0;
        public Heading(String str, int level) {
            super(str);

            if (level < 0 || level > 3) {
                throw new MarkdownCreateException("Incorrect level of heading");
            }
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < level; i++) {
                str.append("#");
            }

            return str.append(super.str).toString();
        }

        public int getLevel() {
            return level;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof Heading) {
                Heading text = (Heading) obj;
                return super.equals(text) &&
                        text.getLevel() == level;
            }

            return false;
        }
    }

    public Builder getBuilder() {
        return new Text.Builder();
    }

    public Text(String str) {
        this.str = str;
    }

    public String getString() {
        return str;
    }

    @Override
    public String toString() {
        return str;
    }

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
