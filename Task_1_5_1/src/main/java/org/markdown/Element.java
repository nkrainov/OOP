package org.markdown;

/**
 * Базовый класс для всех элементов Markdown.
 */
public abstract class Element {
    @Override
    public String toString() {
        return "MarkDown Element";
    }

    /**
     * Получение Builder'а элемента.
     */
    abstract Builder getBuilder();
}
