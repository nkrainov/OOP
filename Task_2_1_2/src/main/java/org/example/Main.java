package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.server.Boss;
import org.example.worker.Worker;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.exit(1);
        }

        if (args[0].equals("server")) {
            Config.BossConfig bossConfig = Config.readBossConfig(args[1]);
            Boss boss = new Boss(bossConfig);
            boss.start();
        } else if (args[0].equals("worker")) {
            Config.WorkerConfig clientConfig = Config.readWorkerConfig(args[1]);
            Worker worker = new Worker(clientConfig);
            worker.start();
        } else {
            System.out.println("Unknown role: " + args[0]);
            System.exit(1);
        }
    }
}