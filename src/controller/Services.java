package controller;

import View.View;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import model.Score;

/*
 @author nxlam
 */
public class Services {

    public Socket socket;
    public int port = 1109;
    public View view;
    public ObjectOutputStream out;
    public Thread thread;

    public static void main(String[] args) {
        new Services();
    }

    public Services() {
        // luong connect
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }).start();

        // luong view
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                view = new View();
                view.setVisible(true);
            }
        });
        thread.start();

        // luong xu ly ket noi
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!thread.isAlive()) {
                        if (view.score.getStatus().equalsIgnoreCase("new")) {
                            try {
                                System.out.println("[CLIENT] " + view.score.getColor()
                                    + ", " + view.score.getPoint() + ", " + view.score.getStatus());
                                send(view.score);
                                view.score.setStatus("old");
                                System.out.println("[CLIENT 2] " + view.score.getColor()
                                    + ", " + view.score.getPoint() + ", " + view.score.getStatus());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void connect() {
        try {
            socket = new Socket("127.0.0.1", port);
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void close() throws IOException {
        out.close();
        socket.close();
    }

    public void send(Score score) throws IOException {
        out.writeObject(score);
        out.flush();
    }
}
