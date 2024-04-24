package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.consumer.Consumer;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestConsumer {
    private Consumer consumer;

    @BeforeEach
    void setUp() {
        OrderedQueue queue = new OrderedQueue();
        consumer = new Consumer(queue);
    }

    @Test
    void testConsumerConstructor() {
        assertNotNull(consumer);
    }

    @Test
    void testPollEmptyQueue() {
        assertEquals(0, consumer.getOrderedQueue().getSize());
        assertNull(consumer.poll());
    }

    @Test
    void testPollMessage() {
        Message message = new Message("Test Header", "Test Body");

        consumer.getOrderedQueue().enqueue(message);
        Message messageDeque = consumer.poll();

        assertNotNull(messageDeque);

        assertEquals("Test Header", messageDeque.getHeader());
        assertEquals("Test Body", messageDeque.getBody());
    }
}