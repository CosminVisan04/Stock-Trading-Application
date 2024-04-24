package nl.rug.aoop.networking.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This Client class represents a client that can establish a socket connection to a specified server address.
 */
@Slf4j
@Getter
public class Client implements Runnable{
    /**
     * Connection timeout in milliseconds.
     * */
    public static final int TIMEOUT = 1000;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private MessageHandler messageHandler;
    private boolean isRunning;
    private boolean isConnected;
    private int id;

    /**
     * This is the constructor for a client.
     * @param address the server's InetSocketAddress.
     * @param messageHandler the handler for incoming messages.
     * @throws IOException if an I/O error occurs during socket initialization.
     */
    public Client(InetSocketAddress address, MessageHandler messageHandler) throws IOException {
        this.messageHandler = messageHandler;
        initSocket(address);
        run();
    }

    private void initSocket(InetSocketAddress address) throws IOException {
        this.socket = new Socket();
        this.socket.connect(address, TIMEOUT);
        if(!this.socket.isConnected()){
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect");
        }
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(),true);
        isConnected = true;
    }

    @Override
    public void run() {
        isRunning = true;
        Thread listenerThread = new Thread(() -> {
            try {
                String incomingMessage = in.readLine();
                boolean isFirstMessage = true;
                goListening(incomingMessage, isFirstMessage);
            } catch (IOException e) {
                log.error("Thread of the client has been closed");
                isConnected = false;
            }
        });
        listenerThread.start();
    }

    private void goListening(String incomingMessage, boolean isFirstMessage) throws IOException {
        while (incomingMessage != null) {
            if (isFirstMessage) {
                this.id = Integer.parseInt(incomingMessage);
                isFirstMessage = false;
            } else if(incomingMessage.equals("STOP")){
                terminate();
            } else {
                messageHandler.handleMessage(incomingMessage, this::sendString);
            }
            incomingMessage = in.readLine();
        }
    }

    /**
     * Terminates the client by closing the socket.
     */
    public void terminate(){
        try {
            this.in.close();
            this.out.close();
            isRunning = false;
            isConnected = false;
            socket.close();
        } catch (IOException e) {
            log.error("Could not terminate");
        }
    }

    /**
     * Sends a string message to the connected server.
     *
     * @param s is the string message to send.
     */
    public void sendString(String s){
        if(out != null && s != null && !s.isEmpty()){
            out.println(s);
        }
    }

    public int getClientHandlerId() {
        return this.id;
    }
}
