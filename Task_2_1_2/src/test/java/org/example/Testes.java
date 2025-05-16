package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;

import static java.lang.Thread.sleep;

public class Testes {

    @Test
    void test() throws IOException {
        String[] args1 = {"server", "config.json"};
        String[] args2 = {"worker", "workconfig.json"};
        org.example.Main.main(args2);
        org.example.Main.main(args1);

        while (true) {

        }
    }

}
