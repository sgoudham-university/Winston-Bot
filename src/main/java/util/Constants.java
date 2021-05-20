package util;

import java.nio.file.Paths;

public enum Constants {
    RESOURCES_PATH(Paths.get("src", "main", "resources").toString()),
    WINSTON_WEDNESDAY(Paths.get(RESOURCES_PATH.toString(), "winstonWednesday.mp4").toString()),
    WINSTON_VOICE_LINES(Paths.get(RESOURCES_PATH.toString(), "winstonVoiceLines.txt").toString()),
    WINSTON_AUDIO(Paths.get(RESOURCES_PATH.toString(), "audio").toString()),

    WINSTON_OH_YEAH(Paths.get(WINSTON_AUDIO.toString(), "winston_oh_yeah.mp3").toString()),
    WINSTON_DONT_GET_ME_ANGRY(Paths.get(WINSTON_AUDIO.toString(), "winston_dont_get_me_angry.mp3").toString()),
    WINSTON_HOW_EMBARRASSING(Paths.get(WINSTON_AUDIO.toString(), "winston_how_embarrassing.mp3").toString()),
    WINSTON_NO_MONKEY_BUSINESS(Paths.get(WINSTON_AUDIO.toString(), "winston_no_monkey_business.mp3").toString()),
    WINSTON_OH_NO(Paths.get(WINSTON_AUDIO.toString(), "winston_oh_no.mp3").toString()),
    WINSTON_SORRY_ABOUT_THAT(Paths.get(WINSTON_AUDIO.toString(), "winston_sorry_about_that.mp3").toString()),
    WINSTON_THAT_WAS_AWESOME(Paths.get(WINSTON_AUDIO.toString(), "winston_that_was_awesome.mp3").toString());

    private final String constant;

    Constants(String constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return constant;
    }
}
