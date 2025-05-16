package org.example.worker;

import org.example.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.nio.*;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Worker extends Thread {
    private static class Work {
        ArrayList<Integer> arr;
    }

    private static class Result {
        boolean NotIsPrime;
    }

    private int port;
    private String inetInterface;

    public Worker(Config.WorkerConfig workerConfig) {
        port = workerConfig.port;
        inetInterface = workerConfig.inetInterface;
    }

    @Override
    public void run() {
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName(inetInterface);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

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

                checkEnd(socket);
                do {
                    Work work = getWork(socket);
                    Result res = doWork(work);
                    sendAns(socket, res);
                } while (checkEnd(socket));

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

        byte[] buffer = new byte[5];

        int read = 0;
        while (true) {
            int res = stream.read(buffer, read, buffer.length - read);
            if (res == 0) {
                throw new IOException("Unexpected end of stream");
            }

            read += res;
            if (read == buffer.length) {
                read = 0;
                work.arr.add(ByteBuffer.wrap(Arrays.copyOfRange(buffer, 0, 4)).getInt());
                if (buffer[4] == 0) {
                    break;
                }
            }
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
