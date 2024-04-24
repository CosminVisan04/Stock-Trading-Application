package nl.rug.aoop.messagequeue.queue;

import nl.rug.aoop.messagequeue.message.Message;

/**
 * The interface used by all queues.
 */
public interface MessageQueue {
    /**
     * Enqueue a message in the queue.
     *
     * @param message the message to be enqueued.
     */
    void enqueue(Message message);

    /**
     * Dequeue one message from the queue.
     *
     * @return the first message that needs to be out of the queue.
     */
    Message dequeue();

    /**
     * Get the size of the queue (how many messages are in the queue).
     *
     * @return the size of the queue.
     */
    int getSize();
}
