package nl.rug.aoop.messagequeue.queue;

import nl.rug.aoop.messagequeue.message.Message;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * This class, ThreadSafeMessageQueue, is used to implement a message queue that is thread-safe.
 */
public class ThreadSafeMessageQueue implements MessageQueue {
    private final PriorityBlockingQueue<Message> safeQueue = new PriorityBlockingQueue<>();

    @Override
    public void enqueue(Message message) {
        if (message == null) {
            return;
        }
        safeQueue.add(message);
    }

    @Override
    public Message dequeue() {
        return safeQueue.poll();
    }

    @Override
    public int getSize() {
        return safeQueue.size();
    }
}
