package nl.rug.aoop.command;

import java.util.Map;

/**
 * This is the interface for a command.
 */
public interface Command {

    /**
     * The executable function of every command.
     * @param parameters the parameters that the command is taking.
     */
    void execute(Map<String, Object> parameters);
}
