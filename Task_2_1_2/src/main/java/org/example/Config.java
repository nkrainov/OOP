package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    public static class TaskGiverConfig {
        public static class WorkerInfo {
            public String host;
            public int port;
        }

        public String pathToNumbers;
        public List<WorkerInfo> workers;
    }

    public static class WorkerConfig {
        public int port;
    }

    public static TaskGiverConfig readBossConfig(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(file), TaskGiverConfig.class);
    }

    public static WorkerConfig readWorkerConfig(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(file), WorkerConfig.class);
    }

    private Config() {
    }
}
