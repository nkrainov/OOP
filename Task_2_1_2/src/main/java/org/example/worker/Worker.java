package org.example.worker;

import org.example.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.nio.*;
import java.util.Arrays;

public class Worker extends Thread {
    private static class Work {
        ArrayList<Integer> arr;
    }

    private static class Result {
        boolean NotIsPrime;
    }

    private int port;

    public Worker(Config.WorkerConfig workerConfig) {
        port = workerConfig.port;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
            serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                while (checkEnd(socket)) {
                    Work work = getWork(socket);
                    Result res = doWork(work);
                    sendAns(socket, res);
                }

                socket.close();
            } catch (IOException e) {
                System.err.println("Error: " + e);
            }
        }
    }

    private boolean checkEnd(Socket socket) throws IOException {
        int res = socket.getInputStream().read();
        if (res == -1) {
            throw new IOException("Unexpected end of stream");
        }

        return res != 0;
    }

    private Work getWork(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        Work work = new Work();
        work.arr = new ArrayList<>();

        byte[] bufferCount = new byte[4];
        int read = 0;
        while (bufferCount.length - read != 0) {
            read = stream.read(bufferCount, 0, bufferCount.length);
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(bufferCount, 0, 4));
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int count = byteBuffer.getInt();
        int countBytesInMessage = count * 4;

        byte[] message = new byte[countBytesInMessage];
        read = 0;
        while (countBytesInMessage - read != 0) {
            int res = stream.read(message, read, countBytesInMessage - read);
            if (res == 0) {
                throw new IOException("Unexpected end of stream");
            }
            read += res;
        }

        ByteBuffer buffer = ByteBuffer.wrap(message);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < count; i++) {
            work.arr.add(buffer.getInt());
        }

        return work;
    }

    private Result doWork(Work work) {
        Result res = new Result();
        res.NotIsPrime = work.arr.stream().parallel().anyMatch(Worker::isComposite);
        return res;
    }

    private static boolean isComposite(Integer number) {
        int sqrt = (int) Math.sqrt(number);
        for (int i = 2; i < sqrt + 1; i++) {
            if (number % i == 0) {
                return true;
            }
        }

        return false;
    }

    private void sendAns(Socket socket, Result res) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(res.NotIsPrime ? 1 : 0);
    }
}
