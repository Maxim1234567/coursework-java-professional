package ru.otus;

import ru.otus.server.Server;

public class App {
    public static void main(String[] args) {
        new Server(8080).start();
    }
}
