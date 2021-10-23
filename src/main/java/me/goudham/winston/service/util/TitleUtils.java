package me.goudham.winston.service.util;

import jakarta.inject.Singleton;

@Singleton
public class TitleUtils {
    public String getTrimmedTitle(String title, int minSize) {
        int titleSize = Math.min(title.length(), minSize);
        String suffix = titleSize < title.length() ? "..." : "";
        return title.substring(0, titleSize) + suffix;
    }
}
