package es.ucm.fdi.tp.practica6.lobby.demo;

import java.io.Serializable;

import es.ucm.fdi.tp.practica6.lobby.demo.ui.GraphicalConsole;

/**
 * A generic, abstract ChatMessage
 */
public abstract class ChatMessage implements Serializable {

    public abstract void showOn(GraphicalConsole console);

    public static ChatMessage fromString(String text) {
        if (text.contains("*")) {
            int pos = text.indexOf("*");
            return new Mult(Integer.parseInt(text.substring(0, pos).trim()),
                    Integer.parseInt(text.substring(pos+1).trim()));
        }
        if (text.contains("stop")) {
            return new Stop();
        }
        return new Text(text);
    }

    public static class Mult extends ChatMessage {
        private int a, b;
        public Mult(int a, int b) {
            this.a = a; this.b = b;
        }

        @Override
        public void showOn(GraphicalConsole console) {
            console.showText(a + " x " + b + " = " + (a*b));
        }
    }

    public static class Stop extends ChatMessage {
        @Override
        public void showOn(GraphicalConsole console) {
            console.closeWindow();
        }
    }

    public static class Text extends ChatMessage {
        private String text;
        public Text(String text) {
            this.text = text;
        }
        @Override
        public void showOn(GraphicalConsole console) {
            console.showText(text);
        }
    }
}
