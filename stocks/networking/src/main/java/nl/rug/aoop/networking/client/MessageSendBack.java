package nl.rug.aoop.networking.client;

/**
 * This is an interface that the classes that implement it should provide a mechanism
 * to send a string message back to the sender.
 */
public interface MessageSendBack {
    /**
     * Sends a string message back to the client.
     *
     * @param string is the message to be sent back.
     */
    void sendBack(String string);
}
