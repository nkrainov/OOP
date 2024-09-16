package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

class BlackJackTest {
    @Test
    void testPlay() {
        BlackJack game = new BlackJack(3);
        ByteArrayInputStream test = new ByteArrayInputStream("1\n0\n1\n0\n1\n0\n1\n0\n1\n0\n1\n1\n0\n1\n1\n0\n1\n1\n0\n1\n".getBytes());
        System.setIn(test);
        game.play();

    }
}