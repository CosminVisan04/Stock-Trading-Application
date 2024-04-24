package nl.rug.aoop.messagequeue;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.MessageQueue;
import nl.rug.aoop.messagequeue.queue.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new OrderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
        assertEquals(0, queue.getSize());
    }

    @Test
    void testDequeue() {
        assertNull(queue.dequeue());
    }

    @Test
    void testQueueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Test
    void testGetSize() {
        Message message1 = new Message("header1", "body1");
        Message message2 = new Message("header2", "body2");
        Message message3 = new Message("header3", "body3");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
        queue.dequeue();
        assertEquals(2, queue.getSize());
        queue.dequeue();
        assertEquals(1, queue.getSize());
        queue.dequeue();
        assertEquals(0, queue.getSize());
    }

    @Test
    void testQueueEnqueueWithNull() {
        Message message = null;
        queue.enqueue(message);

        assertEquals(0, queue.getSize());
    }

    @Test
    void testDequeueEmptyQueue() {
        assertNull(queue.dequeue());
    }
}