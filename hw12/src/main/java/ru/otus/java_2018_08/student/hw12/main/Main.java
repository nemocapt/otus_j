package ru.otus.java_2018_08.student.hw12.main;

import ru.otus.java_2018_08.student.hw12.server.MyServer;

public class Main {
    static public void main(String[] args) throws Exception {
        MyServer server = new MyServer();

        server.start();
        server.join();

        server.stop();
    }
}
