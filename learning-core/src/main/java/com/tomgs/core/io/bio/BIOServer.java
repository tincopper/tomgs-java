package com.tomgs.core.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/20 1.0 
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(6666);
        while (true) {
            Socket socket = serverSocket.accept();

        }
    }

}
