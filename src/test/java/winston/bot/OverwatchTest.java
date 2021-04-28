package winston.bot;

import exception.PlayerNotFoundException;
import models.Player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void getAllHeroesIsNotEmpty() throws IOException {
        overwatch.startup();
        assertFalse(Overwatch.getAllHeroes().isEmpty());
    }

    @Test
    void exceptionThrownWhenArgumentsAreWrongOrder() {
        PlayerNotFoundException thrown = assertThrows(
                PlayerNotFoundException.class,
                () -> overwatch.getPlayerStats(List.of("eu", "pc", "Hammy#21436")),
                "Oh no, the assertion failed! *gasp*"
        );

        assertTrue(thrown.getMessage().contains("Arguments are Invalid / Wrong Order!"));
    }

    @Test
    void exceptionThrownWhenPlayerNameDoesNotHaveHashSymbol() {
        PlayerNotFoundException thrown = assertThrows(
                PlayerNotFoundException.class,
                () -> overwatch.getPlayerStats(List.of("pc", "eu", "Hamm-436")),
                "Oh no, the assertion failed! *gasp*"
        );

        assertTrue(thrown.getMessage().contains("Player Not Found!"));
    }

    @Test
    void exceptionThrownWhenPlayerNameHasHashSymbolButStillWrong() {
        PlayerNotFoundException thrown = assertThrows(
                PlayerNotFoundException.class,
                () -> overwatch.getPlayerStats(List.of("pc", "eu", "Hammy#436122")),
                "Oh no, the assertion failed! *gasp*"
        );

        assertTrue(thrown.getMessage().contains("Player Not Found!"));
    }
}