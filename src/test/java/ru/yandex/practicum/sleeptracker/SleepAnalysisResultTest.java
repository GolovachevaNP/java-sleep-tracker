package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepAnalysisResultTest {

    // Проверить корректность вывода описания и результата анализа
    @Test
    void shouldStoreDescriptionAndResult() {
        SleepAnalysisResult<Long> result = new SleepAnalysisResult<>("Описание", 5L);
        assertEquals("Описание", result.getDescription());
        assertEquals(5L, result.getAnalysisResult());
    }
}
