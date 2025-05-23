package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TestCalcNetwork {
    private static class testPrintStream extends PrintStream {

        public testPrintStream(OutputStream out) {
            super(out);
        }

        public void println(String x) {
            try {
                out.write(x.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class testOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
        }

        @Override
        public void write(byte[] b) throws IOException {
            String str = new String(b);
            if (!str.equals("Result: true")) {
                Assertions.fail();
            }
            synchronized (flag) {
                flag.notify();
            }
        }
    }

    private static class testErrStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
        }

        @Override
        public void write(byte[] b) throws IOException {
            flagFailed = true;
            synchronized (flag) {
                flag.notify();
            }
        }

    }

    private static final Object flag = new Object();
    private static boolean flagFailed = false;

    @Test
    void testWork() throws IOException, InterruptedException {
        System.setOut(new testPrintStream(new testOutputStream()));
        String[] args1 = {"server", "config.json"};
        String[] args2 = {"worker", "workconfig.json"};
        org.example.Main.main(args2);
        org.example.Main.main(args1);

        synchronized (flag) {
            flag.wait();
        }
    }

    @Test
    void testNotWorkIfWorkersDoesntExist() throws IOException, InterruptedException {
        System.setErr(new testPrintStream(new testErrStream()));
        String[] args1 = {"server", "config.json"};
        org.example.Main.main(args1);

        synchronized (flag) {
            flag.wait(10000);
        }
        if (!flagFailed) {
            Assertions.fail();
        }
    }

}
