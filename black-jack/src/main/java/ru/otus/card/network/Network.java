package ru.otus.card.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private Socket socket;

    private DataInputStream in;

    private DataOutputStream out;

    private ObjectMapper mapper;

    public Network(
            String host,
            int port
    ) {
        try {
            this.socket = new Socket(host, port);
            this.mapper = new ObjectMapper();
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Action read() {
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

}
