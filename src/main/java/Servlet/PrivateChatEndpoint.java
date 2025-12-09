package Servlet;

import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat/{username}", configurator = MyEndpointConfigurator.class)
public class PrivateChatEndpoint {

    private static final Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // Lấy username từ userProperties (đã được lưu trong Configurator)
        String username = (String) config.getUserProperties().get("username");

        if (username == null || username.isEmpty()) {
            username = "Guest" + session.hashCode();
        }

        // Lưu username vào session để dùng sau
        session.getUserProperties().put("username", username);
        clients.put(username, session);

        broadcastOnlineList();
        System.out.println(username + " đã tham gia chat");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        String sender = (String) session.getUserProperties().get("username");

        if (message.startsWith("to:")) {
            int sep = message.indexOf("|");
            if (sep > 3) {
                String receiver = message.substring(3, sep);
                String content = message.substring(sep + 1);
                sendPrivate(receiver, sender, content);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String username = (String) session.getUserProperties().get("username");
        if (username != null) {
            clients.remove(username);
            broadcastOnlineList();
            System.out.println(username + " đã rời chat");
        }
    }

    private void sendPrivate(String to, String from, String content) {
        Session receiver = clients.get(to);
        if (receiver != null && receiver.isOpen()) {
            try {
                receiver.getBasicRemote().sendText("from:" + from + "|" + content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastOnlineList() {
        String list = "online:" + String.join(",", clients.keySet());
        clients.values().forEach(s -> {
            try {
                s.getBasicRemote().sendText(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}