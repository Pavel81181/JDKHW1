package org.example;



public class Main {
    public static void main(String[] args) {

        Server server = new Server();
        new Client(server);
        new Client(server);
    }
}