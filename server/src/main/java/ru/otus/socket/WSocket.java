package ru.otus.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.dto.Dto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WSocket {

    private final Socket socket;

    private final DataInputStream in;

    private final DataOutputStream out;

    private final ObjectMapper mapper;

    public WSocket(Socket socket) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.mapper = new ObjectMapper();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Dto dto) {
        try {
            out.writeUTF(mapper.writeValueAsString(dto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Dto read() {
        try {
            return mapper.readValue(in.readUTF(), Dto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
