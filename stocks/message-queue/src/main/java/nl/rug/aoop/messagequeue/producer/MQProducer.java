package nl.rug.aoop.messagequeue.producer;

import nl.rug.aoop.messagequeue.message.Message;

/**
 * The interface used by all the producers.
 */
public interface MQProducer {
    /**
     * Put a message in the queue.
     *
     * @param message the message that is to put in the queue.
     */
    void put(Message message);
}
