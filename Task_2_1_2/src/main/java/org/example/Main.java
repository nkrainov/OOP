package org.example;

import org.example.server.TasksGiver;
import org.example.worker.Worker;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.exit(1);
        }

        if (args[0].equals("taskgiver")) {
            Config.TaskGiverConfig bossConfig = Config.readBossConfig(args[1]);
            TasksGiver TasksGiver = new TasksGiver(bossConfig);
            TasksGiver.start();
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