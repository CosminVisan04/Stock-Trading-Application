package nl.rug.aoop.networking.producer;

import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.networking.client.Client;

/**
 * The NetWorkProducer class represents a producer responsible for sending messages
 * over the network to a connected client.
 */
public class NetWorkProducer {
    private Client client;

    /**
     * This is the constructor for a NetWorkProducer.
     * @param client is the client from the messages will be sent over the network to the server.
     */
    public NetWorkProducer(Client client) {
        this.client = client;
    }

    /**
     * Sends a message over the network to the server, from the client.
     *
     * @param message is the message to be sent.
     */
    public void networkProduce(Message message){
        String temp = message.toJson();
        client.sendString(temp);
    }
}
