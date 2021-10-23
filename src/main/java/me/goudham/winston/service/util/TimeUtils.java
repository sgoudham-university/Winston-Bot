package me.goudham.winston.service.util;

import jakarta.inject.Singleton;
import java.time.Duration;

@Singleton
public class TimeUtils {
    public String formatTime(long timeInMillis) {
        Duration duration = Duration.ofMillis(timeInMillis);
        return String.format("%02d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }
}
