package nl.rug.aoop.messagequeue.message;

import lombok.Getter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

import com.google.gson.*;

/**
 * The class used for a message.
 */
@Getter
public class Message implements Comparable<Message> {
    private final String header;
    private final String body;
    private final LocalDateTime timestamp;
    private static final List<LocalDateTime> OLD_TIME_STAMPS = new ArrayList<LocalDateTime>();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Message.class, new MessageTypeAdapter())
            .create();

    /**
     * This is the constructor of a message.
     *
     * @param messageHeader the header of the message.
     * @param messageBody   the body of the message.
     */
    public Message(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = generateNextTimeStamp();

    }

    /**
     * Verify if a new message is generated with an existing time stamp.
     * If yes, it will not generate the new message until the time stamp changes (LocalDateTime.now())
     *
     * @return the next generated time stamp
     */
    private static LocalDateTime generateNextTimeStamp() {
        LocalDateTime timeStamp;
        do {
            timeStamp = LocalDateTime.now();
        } while (OLD_TIME_STAMPS.contains(timeStamp));

        OLD_TIME_STAMPS.add(timeStamp);
        return timeStamp;
    }

    /**
    * Converts the message to its JSON representation.
    *
    * @return the JSON representation of the message.
    */
    public String toJson() {
        return GSON.toJson(this);
    }

    /**
     * Parses a JSON string and returns the corresponding Message object.
     *
     * @param json the JSON string to parse.
     * @return the Message object parsed from the JSON string.
     */
    public static Message fromJson(String json) {
        return GSON.fromJson(json, Message.class);
    }

    void setTimestamp(LocalDateTime timestamp) {
        try {
            Field field = Message.class.getDeclaredField("timestamp");
            field.setAccessible(true);
            field.set(this, timestamp);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(Message other) {
        return this.timestamp.compareTo(other.timestamp);
    }
}