package Servlet;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class MyEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {
        String path = request.getRequestURI().getPath();
        String username = path.substring(path.lastIndexOf('/') + 1);
        username = URLDecoder.decode(username, StandardCharsets.UTF_8);
        config.getUserProperties().put("username", username);
    }
}