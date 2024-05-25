package com.github.pavelvil.http.server;

import com.github.pavelvil.http.server.handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;

    private final ExecutorService executorService;

    public Server(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (var serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("accepted new connection");
                executorService.execute(new RequestHandler(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
