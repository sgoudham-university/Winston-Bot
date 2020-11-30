package winston.bot;

import models.Player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OverwatchTest {
    private static Player pcPlayer;
    private static Player consolePlayer;
    private static final Overwatch overwatch = new Overwatch();

    @BeforeAll
    static void setup() throws Exception {
        pcPlayer = overwatch.getPlayerStats(List.of("pc", "eu", "Hammy#21436"));
        consolePlayer = overwatch.getPlayerStats(List.of("xbl", "Goudham"));
    }

    @Test
    void pcPlayerUsernameShouldBeCorrect() {
        assertEquals("Hammy", pcPlayer.getUsername());
    }

    @Test
    void pcPlayerUsernameShouldNotBeCorrect() {
        assertNotEquals("234lkjlksgd7-3s;df", pcPlayer.getUsername());
    }

    @Test
    void consolePlayerShouldBeCorrect() {
        assertEquals("Goudham", consolePlayer.getUsername());
    }

    @Test
    void consolePlayerShouldNotBeCorrect() {
        assertNotEquals("2oi34jklsjgf9-82734lksdgn", consolePlayer.getUsername());
    }

    @Test
    void getAllHeroes() {
    }
}