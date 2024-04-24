package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.MessageHandler;
import nl.rug.aoop.networking.client.MessageSendBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The ClientHandler class represents a handler for client connections in a server.
 */
@Slf4j
public class ClientHandler implements Runnable, MessageSendBack {
    private final BufferedReader in;
    private final PrintWriter out;
    private Socket socket;
    @Getter
    private int id;
    private MessageHandler messageHandler;

    /**
     * This is the constructor for a ClientHandler.
     * @param socket is the client's socket.
     * @param id is the unique ID assigned to this client handler.
     * @param messageHandler is the message handler for processing incoming messages.
     * @throws IOException if an I/O error occurs when creating the input or output streams.
     */
    public ClientHandler(Socket socket, int id, MessageHandler messageHandler) throws IOException {
        this.socket = socket;
        this.id = id;
        this.messageHandler = messageHandler;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run(){
        try {
            out.println(this.id);
            String fromClient = in.readLine();
            while(fromClient != null){
                messageHandler.handleMessage(fromClient, this);
                log.info("Client with id: " + id + " sent the following message: " + fromClient);
                fromClient = in.readLine();
            }
        } catch (IOException e) {
            log.error("Could not read from the client");
        } finally {
            terminate();
        }
    }

    /**
     * Terminates the client handler by closing the socket and stopping the thread.
     */
    public void terminate(){
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket!");
        }
    }

    @Override
    public void sendBack(String string) {
        out.println(string);
    }
}
