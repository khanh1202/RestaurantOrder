package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client that deals with communication with the server
 * @author Gia Khanh Dinh
 */
public class ClientPoint extends Thread {
    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Controller myCtrl;
    private boolean shouldStop; //indicate whether the thread should stop running

    public ClientPoint(String serverAddress, Controller controller) throws Exception {
        // Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        myCtrl = controller;
        shouldStop = false;
    }

    public void run() {
        String response;
        while (!shouldStop) {
            try {
                response = in.readLine();
                if (response.startsWith("UPDATE"))
                    myCtrl.updateOrders(); //update the order list if server tells client to do
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyServer() {
        out.println("UPDATE");
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public void quitServer() {
        out.println("QUIT");
    }
}
