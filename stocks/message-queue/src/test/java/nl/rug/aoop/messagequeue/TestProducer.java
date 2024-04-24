package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.producer.Producer;
import nl.rug.aoop.messagequeue.queue.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestProducer {
    private Producer producer;

    @BeforeEach
    void setUp() {
        OrderedQueue queue = new OrderedQueue();
        producer = new Producer(queue);
    }

    @Test
    void testProducerConstructor() {
        assertNotNull(producer);
    }

    @Test
    void testPutMessages() {
        Message message1 = new Message("Header 1", "Test 1");
        Message message2 = new Message("Header 2", "Test 2");
        Message message3 = new Message("Header 3", "Test 3");

        producer.put(message1);
        producer.put(message2);
        producer.put(message3);

        assertEquals(3, producer.getOrderedQueue().getSize());
    }

    @Test
    void testPutNullMessage() {
        Message message = null;
        producer.put(message);

        assertEquals(0, producer.getOrderedQueue().getSize());
    }

    @Test
    void testPutDequeueMessage() {
        Message message = new Message("Test Header", "Test Body");

        producer.put(message);
        Message messageDeque = producer.getOrderedQueue().dequeue();

        assertNotNull(messageDeque);

        assertEquals("Test Header", messageDeque.getHeader());
        assertEquals("Test Body", messageDeque.getBody());
    }
}