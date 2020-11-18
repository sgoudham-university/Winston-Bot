import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class WinstonTest {
    private final JDA jda = Mockito.mock(JDA.class);
    private final Dotenv dotenv = Dotenv.load();
    private Winston winston;

    @BeforeEach
    void setup() {
        winston = new Winston(jda);
    }

    @Test
    public void isBotTokenValid() {
        Mockito.when(jda.getToken()).thenReturn(dotenv.get("TOKEN"));
        Assertions.assertEquals(dotenv.get("TOKEN"), winston.getJDA().getToken());
    }
}
