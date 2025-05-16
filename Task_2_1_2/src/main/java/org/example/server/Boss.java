package org.example.server;

import org.example.Config;
import org.example.worker.Worker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.nio.*;

public class Boss extends Thread {
    private static class Task {
        Task(List<Integer> numbers) {
            this.numbers = numbers;
        }
        List<Integer> numbers;
    }

    private String inetInterface;
    private String pathToNumbers;
    private List<Config.BossConfig.WorkerInfo> workers;

    public Boss(Config.BossConfig config) {
        inetInterface = config.inetInterface;
        workers = config.workers;
        pathToNumbers = config.pathToNumbers;
    }

    @Override
    public void run() {
        if (workers.isEmpty()) {
            throw new RuntimeException("No worker found");
        }

        ArrayList<Integer> numbers;
        try {
            numbers = readFile(new File(pathToNumbers));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName(inetInterface);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Task> tasks = createTasks(numbers);

        Selector selector;
        try {
            selector = Selector.open();
            for (Config.BossConfig.WorkerInfo worker : workers) {
                SocketChannel chan = SocketChannel.open();
                chan.configureBlocking(false);
                chan.bind(new InetSocketAddress("0.0.0.0", 0));
                chan.connect(new InetSocketAddress(worker.host, worker.port));
                chan.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int indexCur = tasks.size() - 1;
        boolean ans = false;
        boolean needEnd = true;
        while (needEnd) {
            try {
                int res = selector.select();
                if (res == 0) continue;

                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = set.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (!key.isValid()) {
                        keyIterator.remove();
                        continue;
                    }

                    SocketChannel ch = (SocketChannel) key.channel();
                    if (key.isConnectable() && indexCur >= 0) {
                        if (ch.finishConnect()) {
                            ch.register(selector, SelectionKey.OP_WRITE);
                            key.attach(tasks.get(indexCur));
                            tasks.remove(indexCur);
                            indexCur--;
                        }
                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1);
                        ch.read(buffer);

                        if (buffer.get(0) == 1) {
                            ans = true;
                            break;
                        }

                        keyIterator.remove();
                        if (indexCur < 0) {
                            needEnd = false;
                        }
                    } else if (key.isWritable()) {
                        Task task = (Task) key.attachment();
                        ByteBuffer buffer = ByteBuffer.allocate(task.numbers.size() * 5);
                        for (int i = 0; i < task.numbers.size(); i++) {
                            buffer.putInt(task.numbers.get(i));
                            if (i + 1 >= task.numbers.size()) {
                                buffer.put((byte) 0);
                            } else {
                                buffer.put((byte) 1);
                            }
                        };
                        buffer.position(0);

                        ch.write(buffer);

                        ch.register(selector, SelectionKey.OP_READ);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Result: " + ans);
    }

    private ArrayList<Integer> readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        ArrayList<Integer> ret = new ArrayList<>();

        while (scanner.hasNext()) {
            ret.add(scanner.nextInt());
        }

        return ret;
    }

    private ArrayList<Task> createTasks(ArrayList<Integer> numbers) {
        ArrayList<Task> tasks = new ArrayList<>();

        int sizeFromWorkers = numbers.size() / workers.size();

        if (sizeFromWorkers > 50) {
            sizeFromWorkers = 50;
        }

        for (int i = 0; i < numbers.size(); i += sizeFromWorkers) {
            if (i + sizeFromWorkers > numbers.size()) {
                tasks.add(new Task(numbers.subList(i, numbers.size())));
            } else {
                tasks.add(new Task(numbers.subList(i, i += sizeFromWorkers)));
            }
        }
        return tasks;
    }
}
