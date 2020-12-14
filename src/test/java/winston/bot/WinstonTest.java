package winston.bot;

import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WinstonTest {
    private final JDA jda = Mockito.mock(JDA.class);
    private Winston winston;

    @BeforeEach
    void setup() {
        winston = new Winston(jda);
    }

    @Test
    public void botTokenShouldBeValid() {
        Mockito.when(jda.getToken()).thenReturn(System.getenv("TOKEN"));
        assertEquals(System.getenv("TOKEN"), winston.getJDA().getToken());
    }

    @Test
    public void botTokenShouldNotBeValid() {
        Mockito.when(jda.getToken()).thenReturn("KJSDJF;LK29384KLSDFNlksjfl928034urlknsdf");
        assertFalse(Boolean.parseBoolean(System.getenv("TOKEN")), winston.getJDA().getToken());
    }
}
