package command;

import java.util.Collections;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws Exception;

    String getName();

    String getHelp();

    default List<String> getAliases() {
        return Collections.emptyList();
    }
}
