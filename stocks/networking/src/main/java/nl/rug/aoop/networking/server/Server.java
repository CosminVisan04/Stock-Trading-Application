package nl.rug.aoop.networking.server;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.client.MessageHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server class represents a simple server that listens for incoming client connections
 * on a specified port and handles them using individual threads.
 */
@Slf4j
@Getter
public class Server implements Runnable{
    private final int port;
    private ServerSocket serverSocket;
    private Map<Integer, ClientHandler> clientHandlerList = new ConcurrentHashMap<>();
    private boolean running = false;
    private int id = 0;
    private MessageHandler messageHandler;
    private ExecutorService service;

    /**
     * This is the constructor for the server.
     * @param port is the port on which the server will listen for incoming connections.
     * @param messageHandler is the message handler to process incoming messages from clients.
     * @throws IOException if an I/O error occurs while opening the server socket.
     */
    public Server(int port, MessageHandler messageHandler) throws IOException {
        serverSocket = new ServerSocket(port);
        this.messageHandler = messageHandler;
        this.port = port;
        this.id = 0;
        this.service = Executors.newCachedThreadPool();
    }

    @Override
    public void run(){
        log.info("Server started at port: " + port);
        running = true;
        while (running){
            try {
                Socket socket = this.serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, id, messageHandler);
                clientHandlerList.put(clientHandler.getId(), clientHandler);
                new Thread(clientHandler).start();
                log.info("New connection from client with id: " + id);
                id++;
            } catch (IOException e) {
                log.error("Could not accept the client connection");
            }
        }
    }

    /**
     * Terminates the server, stopping it from accepting new client connections and shutting down
     * the underlying executor service.
     */
    public void terminate(){
        running = false;
        this.service.shutdown();
    }

    public int getPort(){
        return this.serverSocket.getLocalPort();
    }

    /**
     * Get the Client Handler based on a given id.
     * @param id the id to be found.
     * @return the client handler with the given id.
     */
    public ClientHandler getClientHandlerById(int id) {
        if(clientHandlerList.get(id) != null){
            return clientHandlerList.get(id);
        }
        log.error("No client handler with the given id: " + id);
        return null;
    }

    /**
     * Send the periodic messages to the connected clients.
     * @param message the message to be sent.
     */
    public void sendPeriodicalMessages(String message){
        clientHandlerList.values().forEach(clientHandler -> {
            clientHandler.sendBack(message);
        });
    }

}