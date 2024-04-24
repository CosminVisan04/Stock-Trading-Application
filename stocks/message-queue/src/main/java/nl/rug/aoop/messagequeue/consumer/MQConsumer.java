package nl.rug.aoop.messagequeue.consumer;

import nl.rug.aoop.messagequeue.message.Message;

/**
 * The interface used by all the consumers.
 */
public interface MQConsumer {
    /**
     * Dequeue a message from the queue.
     *
     * @return the message that is first to take out of the queue.
     */
    Message poll();
}
