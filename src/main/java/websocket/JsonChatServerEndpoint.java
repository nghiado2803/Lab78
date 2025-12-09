package websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import model.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/jsonchat/{username}",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class
)
public class JsonChatServerEndpoint {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        if (username == null || username.trim().isEmpty()) {
            username = "Guest" + session.hashCode();
        }
        sessions.put(username, session);

        Message msg = new Message(username + " joined the chat!", 0, "server", sessions.size());
        broadcast(msg);
        System.out.println(username + " đã tham gia. Tổng: " + sessions.size());
    }

    // CHỈ DÙNG HÀM NÀY – NHẬN TEXT THUẦN TỪ CLIENT
    @OnMessage
    public void onMessage(String text, Session session) {
        String sender = sessions.entrySet().stream()
                .filter(e -> e.getValue() == session)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("Unknown");

        Message msg = new Message(text, 1, sender, sessions.size());
        broadcast(msg);
    }

    @OnClose
    public void onClose(Session session) {
        String username = sessions.entrySet().stream()
                .filter(e -> e.getValue() == session)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("Unknown");

        sessions.remove(username);
        Message msg = new Message(username + " left the chat!", 2, "server", sessions.size());
        broadcast(msg);
        System.out.println(username + " đã rời chat");
    }

    private void broadcast(Message message) {
        String json = gson.toJson(message);
        sessions.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}