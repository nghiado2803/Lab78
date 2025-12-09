package Servlet;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/text/chat")
public class TextChatServerEndpoint {

    // Danh sách tất cả client đang kết nối
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        broadcast("Someone joined the chat!");
        System.out.println("Client kết nối: " + session.hashCode());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Gửi tin nhắn đến tất cả mọi người (bao gồm cả người gửi)
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        broadcast("Someone left the chat!");
        System.out.println("Client ngắt kết nối: " + session.hashCode());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Lỗi WebSocket: " + throwable.getMessage());
        if (session != null) {
            sessions.remove(session);
            broadcast("Someone disconnected due to error!");
        }
    }

    // Hàm gửi tin nhắn đến tất cả client đang kết nối
    private void broadcast(String message) {
        sessions.forEach(session -> {
            try {
                // DÙNG getBasicRemote() HOÀN TOÀN ĐÚNG TRÊN JAKARTA EE
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.out.println("Lỗi gửi tin nhắn tới client: " + e.getMessage());
                sessions.remove(session);
            }
        });
    }
}