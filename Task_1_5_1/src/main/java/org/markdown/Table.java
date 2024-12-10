package org.markdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Реализация таблиц.
 */
public class Table extends Element {
    public static final int ALIGN_RIGHT = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_CENTER = 2;

    private ArrayList<ArrayList<Text>> contents = new ArrayList<ArrayList<Text>>();
    private final int[] alligments;
    private final int countColumns;
    private final int countRows;

    /**
     * Builder для таблиц.
     */
    public static class Builder implements org.markdown.Builder {
        private ArrayList<Integer> alignments;
        private ArrayList<ArrayList<Text>> contents = new ArrayList<ArrayList<Text>>();
        private int countOfColumn;

        /**
         * Конструктор.
         */
        public Builder() {
            alignments = new ArrayList<>();
            alignments.add(ALIGN_CENTER);
            alignments.add(ALIGN_CENTER);
            alignments.add(ALIGN_CENTER);
            countOfColumn = 3;
        }

        /**
         * Установка выравнивания.
         */
        public void withAlignments(int... alignments) {
            if (alignments.length != countOfColumn) {
                throw new IllegalArgumentException("Unequal length texts array and countOfColumn");
            }

            this.alignments = new ArrayList<>();
            for (int alignment : alignments) {
                this.alignments.add(alignment);
            }
        }

        /**
         * установка количества колонн.
         */
        public void withCountOfColumn(int newCount) {
            countOfColumn = newCount;
            contents = new ArrayList<>();
            alignments = new ArrayList<>();
            for (int i = 0; i < newCount; i++) {
                contents.add(new ArrayList<>());
                alignments.add(ALIGN_CENTER);
            }
        }


        /**
         * Добавление строки.
         */
        public void addRow(Text... texts) {
            if (texts.length != countOfColumn) {
                throw new MarkdownSettingsException("Unequal length texts array and countOfColumn");
            }

            for (int i = 0; i < countOfColumn; i++) {
                contents.get(i).add(texts[i]);
            }
        }

        /**
         * Создание таблицы.
         */
        public Table build() {
            int[] resall = new int[alignments.size()];
            for (int i = 0; i < alignments.size(); i++) {
                resall[i] = alignments.get(i);
            }

            return new Table(countOfColumn, contents.get(0).size(), contents, resall);
        }
    }

    /**
     * Получение Builder'а.
     */
    public Builder getBuilder() {
        return new Table.Builder();
    }

    /**
     * Конструктор.
     */
    public Table(int countColumn, int countRows,
                 ArrayList<ArrayList<Text>> texts, int[] alignments) {
        if (countColumn <= 0) {
            throw new MarkdownCreateException("Negative count of columns or rows");
        }

        if (texts.size() < countColumn) {
            throw new MarkdownCreateException("Too few values for create table");
        }

        this.alligments = alignments.clone();
        this.countColumns = countColumn;
        this.countRows = countRows;

        contents = (ArrayList<ArrayList<Text>>) texts.clone();
    }

    /**
     * Сериализация в строку.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("|");
        for (int i = 0; i < countColumns; i++) {
            str.append(contents.get(i).get(0));
            str.append("|");
        }

        str.append("\n|");

        for (int alligment : alligments) {
            switch (alligment) {
                case ALIGN_RIGHT:
                    str.append("-:");
                    break;
                case ALIGN_LEFT:
                    str.append(":-");
                    break;
                case ALIGN_CENTER:
                default:
                    str.append(":-:");
                    break;
            }

            str.append("|");
        }

        str.append("\n");

        for (int i = 1; i < countRows; i++) {
            str.append("|");
            for (int j = 0; j < countColumns; j++) {
                if (contents.get(j).get(i) == null) {
                    str.append("   ");
                } else {
                    str.append(contents.get(j).get(i));
                }
                str.append("|");
            }
            str.append("\n");
        }


        return str.toString();
    }

    /**
     * Проверка на равенство.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Table) {
            Table table = (Table) obj;
            return countColumns == table.countColumns
                    && countRows == table.countRows
                    && contents.equals(table.contents)
                    && Arrays.equals(alligments, table.alligments);
        }
        return false;
    }
}