package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    OWL("Сова"),
    LARK("Жаворонок"),
    DOVE("Голубь");

    public final String rusName;

    Chronotype(String rusName) {
        this.rusName = rusName;
    }
}
