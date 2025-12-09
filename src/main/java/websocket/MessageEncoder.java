package websocket;

import com.google.gson.Gson;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import model.Message;

public class MessageEncoder implements Encoder.Text<Message> {
    private static final Gson gson = new Gson();

    @Override
    public String encode(Message message) {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}
}