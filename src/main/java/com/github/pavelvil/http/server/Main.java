package com.github.pavelvil.http.server;

import com.github.pavelvil.http.server.common.ApplicationParameters;

public class Main {
    public static void main(String[] args) {
        ApplicationParameters.getInstance().setFileDirectory(args);

        var server = new Server(4221);
        server.start();
    }
}
