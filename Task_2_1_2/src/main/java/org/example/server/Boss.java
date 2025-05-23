package org.example.server;

import org.example.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
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

    private String pathToNumbers;
    private List<Config.BossConfig.WorkerInfo> workers;
    private ArrayList<Task> tasks;
    private HashSet<Task> processTasks = new HashSet<Task>();

    public Boss(Config.BossConfig config) {
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

        tasks = createTasks(numbers);

        Selector selector;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int countWorkers = 0;
        for (Config.BossConfig.WorkerInfo worker : workers) {
            try {
                SocketChannel chan = SocketChannel.open();
                chan.configureBlocking(false);
                chan.bind(new InetSocketAddress("0.0.0.0", 0));
                chan.connect(new InetSocketAddress(worker.host, worker.port));
                chan.register(selector, SelectionKey.OP_CONNECT);
            } catch (Exception ignored) { }
            countWorkers++;
        }

        if (countWorkers == 0) {
            throw new RuntimeException("No worker found");
        }

        boolean ans = false;
        boolean needWork = true;
        while (needWork) {
            try {
                int res = selector.select();

                if (res == 0) continue;

                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = set.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    try {
                        SocketChannel ch = (SocketChannel) key.channel();
                        if (key.isConnectable()) {
                            processConnect(key);
                        } else if (key.isReadable()) {
                            if (processRead(key)) {
                                ans = true;
                                needWork = false;
                                break;
                            }
                        } else if (key.isWritable()) {
                            processWrite(key, false);
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        key.cancel();
                        countWorkers--;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (countWorkers <= 0) {
                throw new RuntimeException("No worker found");
            }

            if (tasks.isEmpty() && processTasks.isEmpty()) {
                sendEnd(selector);
                break;
            }
        }

        while (countWorkers != 0) {
            try {
                selector.select();
                Set<SelectionKey> set = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = set.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    try {
                        SocketChannel ch = (SocketChannel) key.channel();
                        if (key.isConnectable()) {
                            if (ch.finishConnect()) {
                                key.interestOps(SelectionKey.OP_WRITE);
                            }
                        } else if (key.isReadable()) {
                            processRead(key);
                        } else if (key.isWritable()) {
                            processWrite(key, true);
                            countWorkers--;
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                        key.cancel();
                        countWorkers--;
                    }
                }
            } catch (IOException ignored) {
            }
        }

        for (SelectionKey key : selector.keys()) {
            try {
                key.channel().close();
            } catch (IOException ignored) { }
        }
        try {
            selector.close();
        } catch (IOException ignored) { }

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

    private void processConnect(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        if (ch.finishConnect()) {
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private boolean processRead(SelectionKey key) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();
        Selector selector = key.selector();

        ByteBuffer buffer = ByteBuffer.allocate(1);
        ch.read(buffer);

        key.interestOps(SelectionKey.OP_WRITE);
        processTasks.remove(key.attachment());
        if (buffer.get(0) == 1) {
            return true;
        }

        return false;
    }

    private void processWrite(SelectionKey key, boolean end) throws IOException {
        SocketChannel ch = (SocketChannel) key.channel();

        if (end) {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            ch.write(buffer);
            return;
        }

        Task task = tasks.get(tasks.size() - 1);
        tasks.remove(tasks.size() - 1);
        processTasks.add(task);

        ByteBuffer buffer = ByteBuffer.allocate((task.numbers.size() + 1) * 4 + 1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte) 1);
        buffer.putInt(task.numbers.size());
        for (int i = 0; i < task.numbers.size(); i++) {
            buffer.putInt(task.numbers.get(i));
        };
        buffer.position(0);
        ch.write(buffer);

        key.interestOps(SelectionKey.OP_READ);
        key.attach(task);
    }

    private void sendEnd(Selector selector) {
        for (SelectionKey key : selector.selectedKeys()) {
            try {
                SocketChannel ch = (SocketChannel) key.channel();
                ch.write(ByteBuffer.wrap(new byte[]{0}));
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
