package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.command.MqPutCmd;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestMqPutCmd {
    private ThreadSafeMessageQueue messageQueue;
    private MqPutCmd mqPutCmd;

    @BeforeEach
    public void setUp() {
        messageQueue = new ThreadSafeMessageQueue();
        mqPutCmd = new MqPutCmd(messageQueue);
    }

    @Test
    void testConsumerConstructor() {
        assertNotNull(mqPutCmd);
    }

    @Test
    public void testExecuteWithValidParameters() {
        String body = "Test message body";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("body", body);

        mqPutCmd.execute(parameters);

        assertEquals(1, messageQueue.getSize());

        Message queuedMessage = messageQueue.dequeue();
        assertEquals(body, queuedMessage.getBody());
    }

    @Test
    public void testExecuteWithNullParameters() {
        mqPutCmd.execute(null);

        assertEquals(0, messageQueue.getSize());
    }

    @Test
    public void testExecuteWithoutBodyParameter() {
        Map<String, Object> parameters = new HashMap<>();

        mqPutCmd.execute(parameters);

        assertEquals(0, messageQueue.getSize());
    }
}
