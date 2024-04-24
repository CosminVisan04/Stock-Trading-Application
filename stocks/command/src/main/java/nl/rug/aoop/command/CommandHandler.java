package nl.rug.aoop.command;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The CommandHandler class manages a collection of commands and facilitates their execution.
 */
public class CommandHandler{
    @Getter  private final Map<String, Command> cmdMap = new HashMap<>();

    /**
     * This is for adding a new command to the command Map.
     * @param string is the string for the command.
     * @param command is the command to be associated with the unique string.
     */
    public void newCommand(String string, Command command){
        cmdMap.put(string, command);
    }

    /**
     * Executes the command associated with the given string.
     *
     * @param string is the string of the command to be executed.
     * @param parameters is a map of parameters to be passed to the command's execution.
     */
    public void handleCmd(String string, Map<String,Object> parameters){
        Command cmd = cmdMap.get(string);
        if(cmd != null){
            cmd.execute(parameters);
        }
    }

    /**
     * Verifies if a command exists in the command map.
     * @param header the header to verify in the command map.
     * @return true / false if the command exists.
     */
    public boolean isCommand(String header) {
        return cmdMap.containsKey(header);
    }
}
