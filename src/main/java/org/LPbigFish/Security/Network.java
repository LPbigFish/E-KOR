package org.LPbigFish.Security;

import org.LPbigFish.Components.Values;

import java.awt.desktop.SystemSleepEvent;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    protected ServerSocket serverSocket;

    protected Socket clientSocket;

    protected static short port = 5055;

    public Network() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.accept();
            clientSocket = new Socket(Values.known_Peers[0], port);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF("03" + clientSocket.getLocalSocketAddress());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public short connectToPeer(String ip) {
        try {
            clientSocket = new Socket(ip, port);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF("03" + clientSocket.getLocalSocketAddress());
            out.flush();
            out.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public short sendData(String data) {
        try {
            int i = 0;
            for (String peer : Values.known_Peers) {
                clientSocket = new Socket(peer, port);
                if (clientSocket.isConnected()) {
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    out.writeUTF(data);
                    out.flush();
                    out.close();
                    i++;
                } else {
                    System.out.println("Peer " + peer + " was not found");
                }
            }
            System.out.println("Sent data to " + i + " peers");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
