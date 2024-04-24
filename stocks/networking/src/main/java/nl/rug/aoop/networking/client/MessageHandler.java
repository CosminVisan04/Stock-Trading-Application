package nl.rug.aoop.networking.client;

/**
 * An interface for handling incoming messages in a client-server communication system.
 */
public interface MessageHandler {
    /**
     * Handles the incoming message.
     *
     * @param message is the message received from the client.
     * @param sendBack an object for sending a response back to the client.
     */
    void handleMessage(String message, MessageSendBack sendBack);
}
