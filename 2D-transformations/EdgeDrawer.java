package org.sample;


import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EdgeDrawer extends JPanel {
    private static final String FILE_NAME = "edges.txt";
    private static final String FILE_PATH = "";
    private Logger logger = Logger.getLogger(EdgeDrawer.class.getName());
    private ArrayList<Pair> edges;
    private ArrayList<Pair> vertices;
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 800;

    private void readFile() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
        File file = new File(FILE_PATH + FILE_NAME);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                int nPoints = Integer.parseInt(reader.readLine());
                for (int i = 0; i < nPoints; ++i) {
                    String[] points = reader.readLine().split(" ");
                    int x = Integer.parseInt(points[0]);
                    int y = Integer.parseInt(points[1]);
                    vertices.add(new Pair(x, y));
                }
                int nEdges = Integer.parseInt(reader.readLine());
                for (int i = 0; i < nEdges; ++i) {
                    String[] edgs = reader.readLine().split(" ");
                    edges.add(new Pair(Integer.parseInt(edgs[0]), Integer.parseInt(edgs[1])));
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            throw new RuntimeException("No se pudo encontrar el archivo");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        Graphics2D g2d = (Graphics2D) g;

        // size es el tamaño de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los títulos de la ventana.
        Insets insets = getInsets();

        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;
        g2d.setColor(Color.YELLOW);
        g2d.drawLine(w / 2, 0, w / 2, h);
        g2d.drawLine(0, h / 2, w, h / 2);
        paintEdges(g2d, w, h);
    }

    public void paintEdges(Graphics2D g2d, int w, int h) {
        readFile();
        int transX = w / 2;
        int transY = h / 2;
        for (Pair edge : edges) {
            Pair from = vertices.get(edge.getX());
            Pair to = vertices.get(edge.getY());
            g2d.drawLine(transX + from.getX(), transY - from.getY(), transX + to.getX(), transY - to.getY());

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Points");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new EdgeDrawer());
        // Asignarle tamaño
        frame.setSize(MAX_WIDTH, MAX_HEIGHT);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
        FileUtils.printImage(false, frame);
    }

    private static class Pair {
        private int x;
        private int y;

        public Pair() {
        }

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
