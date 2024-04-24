package nl.rug.aoop.messagequeue.message;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class, MessageTypeAdapter, is a Gson TypeAdapter for serializing and deserializing
 * Message objects to and from JSON.
 */
public class MessageTypeAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("header", message.getHeader());
        jsonObject.addProperty("body", message.getBody());
        jsonObject.addProperty("timestamp", message.getTimestamp().format(DATE_TIME_FORMATTER));
        return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
            jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String header = jsonObject.get("header").getAsString();
        String body = jsonObject.get("body").getAsString();
        String timestampStr = jsonObject.get("timestamp").getAsString();
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DATE_TIME_FORMATTER);

        Message message = new Message(header, body);
        message.setTimestamp(timestamp);

        return message;
    }
}