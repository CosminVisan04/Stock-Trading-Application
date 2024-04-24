package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.networking.client.MessageSendBack;

import java.util.HashMap;
import java.util.Map;

/**
 * The MessageHandle class implements the MessageHandler interface,
 * providing functionality to handle incoming messages on the server side.
 */
@Slf4j
public class MessageHandle implements MessageHandler {

    private CommandHandler commandHandler;

    /**
     * This is the constructor for the MessageHandle.
     * @param commandHandler is the command handler to be used for processing possible commands.
     */
    public MessageHandle(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handleMessage(String message, MessageSendBack sendBack) {
        Message temp = Message.fromJson(message);

        verifyCommand(sendBack, temp);
    }

    /**
     * Verifies if in the command handler is a command with the given header.
     * @param sendBack the sender of the message to the client.
     * @param message the message that had a matching header.
     */
    public void verifyCommand(MessageSendBack sendBack , Message message){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("body", message.getBody());

        if((commandHandler.isCommand(message.getHeader()))){
            commandHandler.handleCmd(message.getHeader(), parameters);
        } else {
            sendBack.sendBack("Couldn't match the command!");
        }
    }
}
