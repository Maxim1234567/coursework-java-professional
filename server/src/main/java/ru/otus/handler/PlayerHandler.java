package ru.otus.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Action;
import ru.otus.server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerHandler {
    private Server server;

    private Socket socket;

    private String name;

    private DataInputStream in;

    private ObjectMapper mapper;

    private DataOutputStream out;

    public PlayerHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.mapper = new ObjectMapper();
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            new Thread(this::communicate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void communicate() {
        while (true) {
            Action action = read();

            switch (action.state()) {
                case CONNECT -> {
                    name = action.player();
                    server.subscribe(this);
                    break;
                }
                case MOVE -> {
                    server.broadcastMessage(action);
                }
            }
        }
    }

    private Action read() {
        try {
            return convert(in.readUTF());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Action action) {
        try {
            out.writeUTF(convert(action));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String convert(Action action) {
        try {
            return mapper.writeValueAsString(action);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Action convert(String message) {
        try {
            return mapper.readValue(message, Action.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

}
