package nl.rug.aoop.messagequeue.command;

import nl.rug.aoop.command.Command;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;

import java.util.Map;

/**
 * MqPutCmd is a command class for putting messages into a ThreadSafeMessageQueue.
 */
public class MqPutCmd implements Command {
    private ThreadSafeMessageQueue messageQueue;

    /**
     * This is the constructor for MqPutCmd.
     * @param messageQueue is the ThreadSafeMessageQueue to put messages into.
     */
    public MqPutCmd(ThreadSafeMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void execute(Map<String, Object> parameters) {
        if(parameters != null && parameters.containsKey("body")){
            String body = (String) parameters.get("body");
            Message temp = new Message("", body);
            messageQueue.enqueue(temp);
        }
    }
}
