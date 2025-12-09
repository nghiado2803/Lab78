package websocket;

import com.google.gson.Gson;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import model.Message;

public class MessageDecoder implements Decoder.Text<Message> {
    private static final Gson gson = new Gson();

    @Override
    public Message decode(String json) {
        return gson.fromJson(json, Message.class);
    }

    @Override
    public boolean willDecode(String json) {
        return json != null && (json.contains("\"type\"") && json.contains("\"sender\""));
    }

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}
}