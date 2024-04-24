package nl.rug.aoop.messagequeue.producer;

import lombok.Getter;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.OrderedQueue;

/**
 * The class used for a producer.
 */
@Getter
public class Producer implements MQProducer {
    private OrderedQueue orderedQueue;

    /**
     * This is the constructor of a producer.
     *
     * @param orderedQueue the queue in which the producer put messages.
     */
    public Producer(OrderedQueue orderedQueue) {
        this.orderedQueue = orderedQueue;
    }

    @Override
    public void put(Message message) {
        orderedQueue.enqueue(message);
    }
}