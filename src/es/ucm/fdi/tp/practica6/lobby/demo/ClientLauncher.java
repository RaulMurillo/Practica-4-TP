package es.ucm.fdi.tp.practica6.lobby.demo;

import javax.swing.*;

import es.ucm.fdi.tp.practica6.lobby.demo.net.ObjectClient;
import es.ucm.fdi.tp.tp.practica6.lobby.demo.ui.GraphicalConsole;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by mfreire on 28/04/16.
 */
public class ClientLauncher {
    private ObjectClient oc;

    public void launchInConsole() throws IOException {
        oc = new ObjectClient() {
            @Override
            public void connectionEstablished() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Write something, I dare you! ");
                System.out.println("> ");
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    sendData(line);
                    if (line.equals("stop")) {
                        stop();
                        return;
                    }
                    System.out.println("> ");
                }
            }
        };
        oc.start();
    }

    private static void displayInFrame(JPanel jp) {
        JFrame jf = new JFrame("Test");
        jf.setLayout(new BorderLayout());
        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.add(jp);
        jf.setVisible(true);
    }

    public void launchInWindow() throws IOException {
        final GraphicalConsole gc = new GraphicalConsole("Test") {
            @Override
            protected void handleTextEntry(String text) {
                oc.sendData(text);
            }
        };
        displayInFrame(gc);

        oc = new ObjectClient() {
            @Override
            public void dataReceived(final Object data) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        gc.showText("Received: " + data);
                    }
                });
            }
        };
        oc.start();
    }


    public void launchInMessageWindow() throws IOException {
        final GraphicalConsole gc = new GraphicalConsole("Test") {
            @Override
            protected void handleTextEntry(String text) {
                oc.sendData(ChatMessage.fromString(text));
            }
        };
        displayInFrame(gc);

        oc = new ObjectClient() {
            @Override
            public void dataReceived(final Object data) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ((ChatMessage)data).showOn(gc);
                    }
                });
            }
        };
        oc.start();
    }


    public static void main(String ... args) throws IOException {
        new ClientLauncher().launchInMessageWindow();
    }
}
