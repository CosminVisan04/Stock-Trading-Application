package nl.rug.aoop.messagequeue.consumer;

import lombok.Getter;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.OrderedQueue;

/**
 * The class used for a consumer.
 */
@Getter
public class Consumer implements MQConsumer {
    private OrderedQueue orderedQueue;

    /**
     * This is the constructor of a consumer.
     *
     * @param orderedQueue the queue which is accessed by the consumer.
     */
    public Consumer(OrderedQueue orderedQueue) {
        this.orderedQueue = orderedQueue;
    }

    @Override
    public Message poll() {
        return orderedQueue.dequeue();
    }
}
