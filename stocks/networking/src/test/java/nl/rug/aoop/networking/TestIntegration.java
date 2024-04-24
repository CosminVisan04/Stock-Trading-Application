package nl.rug.aoop.networking;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.command.MqPutCmd;
import nl.rug.aoop.messagequeue.message.Message;
import nl.rug.aoop.messagequeue.queue.ThreadSafeMessageQueue;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.producer.NetWorkProducer;
import nl.rug.aoop.networking.server.MessageHandle;
import nl.rug.aoop.networking.server.Server;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
class TestIntegration {
    @Test
    public void testIntegrate() {
        ThreadSafeMessageQueue messageQueue = new ThreadSafeMessageQueue();
        CommandHandler commandHandler = setupCommandHandler(messageQueue);
        Thread serverThread = startServer(commandHandler);

        sleep(1000);

        Client client1 = createClient("Client1", commandHandler);

        NetWorkProducer producer = new NetWorkProducer(client1);
        Message message = new Message("MqPut", "Test Body");
        producer.networkProduce(message);

        sleep(1000);

        printMessageQueueStatus(messageQueue, message.getBody());

        stopServer(serverThread);
    }

    private static CommandHandler setupCommandHandler(ThreadSafeMessageQueue messageQueue) {
        CommandHandler commandHandler = new CommandHandler();
        MqPutCmd mqPutCommand = new MqPutCmd(messageQueue);
        commandHandler.newCommand("MqPut", mqPutCommand);
        return commandHandler;
    }

    private static Thread startServer(CommandHandler commandHandler) {
        Thread serverThread = new Thread(() -> {
            try {
                Server server = new Server(6200, new MessageHandle(commandHandler));
                server.run();
            } catch (IOException e) {
                log.error("Could not run the server.");
            }
        });
        serverThread.start();
        return serverThread;
    }

    private static Client createClient(String clientName, CommandHandler commandHandler) {
        Client client = null;
        try {
            client = new Client(new InetSocketAddress("localhost", 6200), (message, responder) -> {
                System.out.println(clientName + " received: " + message);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error("Thread sleep interrupted.");
        }
    }

    private static void printMessageQueueStatus(ThreadSafeMessageQueue messageQueue, String expectedBody) {
        log.info("Message Queue Size: " + messageQueue.getSize());
        assertEquals(1, messageQueue.getSize());

        String fromQueueBody = messageQueue.dequeue().getBody();
        log.info("Message Dequeued: " + fromQueueBody);
        assertEquals(expectedBody, fromQueueBody);

        log.info("Message Queue Size after Dequeue: " + messageQueue.getSize());
        assertEquals(0, messageQueue.getSize());
    }

    private static void stopServer(Thread serverThread) {
        serverThread.interrupt();
        log.info("Server Stopped.");
    }
}