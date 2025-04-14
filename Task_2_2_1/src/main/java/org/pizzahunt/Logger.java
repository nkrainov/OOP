package org.pizzahunt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Класс логирования.
 */
class Logger {
    private Logger() {
    }

    private static OutputStream out = null;

    /**
     * Установка потока, куда пишутся логи.
     */
    static synchronized void setOutputStream(OutputStream out) {
        Logger.out = out;
    }

    /**
     * Написание лога в поток.
     */
    static synchronized void write(String msg) {
        if (out == null) {
            setOutputStream(System.out);
        }

        try {
            out.write((new Date().toString() + " " + msg + "\n").getBytes());
        } catch (IOException ignored) {
        }
    }
}
