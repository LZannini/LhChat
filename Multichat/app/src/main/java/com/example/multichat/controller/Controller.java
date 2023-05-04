package com.example.multichat.controller;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Controller {

    private Socket socket;
    private static Controller controller = null;

    private static final int SERVERPORT = 5000;
    private static final String SERVER_IP = "10.0.2.2";

    public static Controller getInstance() {
        if (controller != null)
            return controller;

        return new Controller();
    }

    public static Controller getNewInstance() {
        controller = null;
        controller = new Controller();
        return controller;
    }
}
