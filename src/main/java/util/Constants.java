package util;

import java.nio.file.Paths;

public enum Constants {
    RESOURCES_PATH(Paths.get("src", "main", "resources").toString()),
    WINSTON_WEDNESDAY(Paths.get(RESOURCES_PATH.toString(), "winstonWednesday.mp4").toString());

    private final String constant;

    Constants(String constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return constant;
    }
}
