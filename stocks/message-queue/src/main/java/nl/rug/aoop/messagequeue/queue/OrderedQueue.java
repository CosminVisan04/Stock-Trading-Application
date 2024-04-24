package nl.rug.aoop.messagequeue.queue;

import nl.rug.aoop.messagequeue.message.Message;

import java.util.*;

/**
 * The class used for an ordered queue.
 */
public class OrderedQueue implements MessageQueue {
    private Queue<Message> orderedQueue;

    /**
     * The constructor of the ordered queue.
     */
    public OrderedQueue() {
        Comparator<Message> messageComparator = (msg1, msg2) -> {
            return msg1.getTimestamp().compareTo(msg2.getTimestamp());
        };

        this.orderedQueue = new PriorityQueue<Message>(messageComparator);
    }

    @Override
    public void enqueue(Message message) {
        if (message == null) {
            return;
        }
        orderedQueue.add(message);
    }

    @Override
    public Message dequeue() {
        return orderedQueue.poll();
    }

    @Override
    public int getSize() {
        return orderedQueue.size();
    }
}