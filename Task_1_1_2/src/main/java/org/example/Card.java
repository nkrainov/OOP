package org.example;

public class Card {
    private String name;

    public Card(String cardName){
        name = cardName;
    }

    public int getValue(int curSum){
        int ans = 0;
        if (name.contains("Туз")) {
            if (curSum >= 11) {
                ans = 1;
            }
            else {
                ans = 11;
            }
        } else if (name.contains("Двойка")) {
            ans = 2;
        } else if (name.contains("Тройка")) {
            ans = 3;
        } else if (name.contains("Четверка")) {
            ans = 4;
        } else if (name.contains("Пятерка")) {
            ans = 5;
        } else if (name.contains("Шестерка")) {
            ans = 6;
        } else if (name.contains("Семерка")) {
            ans = 7;
        } else if (name.contains("Восьмерка")) {
            ans = 8;
        } else if (name.contains("Девятка")) {
            ans = 9;
        } else if (name.contains("Десятка") || name.contains("Валет") || name.contains("Дама") || name.contains("Король")) {
            ans = 10;
        }
        return ans;
    }
}
