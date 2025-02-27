package org.pizzahunt;

/**
 * Исключение, выкидывающееся при обнаружении некорректных настроек в файле конфигурации.
 */
public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String message) {
        super(message);
    }
}
