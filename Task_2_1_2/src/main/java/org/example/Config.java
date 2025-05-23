package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {
    public static class BossConfig {
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

    public static BossConfig readBossConfig(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(file), BossConfig.class);
    }

    public static WorkerConfig readWorkerConfig(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(file), WorkerConfig.class);
    }

    private Config() {
    }
}
