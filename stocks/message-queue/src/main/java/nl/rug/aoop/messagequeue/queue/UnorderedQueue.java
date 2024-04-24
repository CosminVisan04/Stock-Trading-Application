package nl.rug.aoop.messagequeue.queue;

import nl.rug.aoop.messagequeue.message.Message;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The class used for an unordered queue.
 */
public class UnorderedQueue implements MessageQueue {

    private Queue<Message> unorderedQueue;

    /**
     * The constructor of the ordered queue.
     */
    public UnorderedQueue() {
        this.unorderedQueue = new LinkedList<Message>();
    }

    @Override
    public void enqueue(Message message) {
        if (message == null) {
            return;
        }
        unorderedQueue.add(message);
    }

    @Override
    public Message dequeue() {
        return unorderedQueue.poll();
    }

    @Override
    public int getSize() {
        return unorderedQueue.size();
    }
}
