/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import Math.Point;
import Math.Sphere;
import Math.Triangle;

import Scene.Scene;
import Scene.Colour;
import Scene.AmbientLight;
import Scene.PointLight;
import Scene.Material;

import PPM.Texture;

/**
 *
 * @author htrefftz
 */
public class Main extends JPanel {
    Image image;
    private final boolean DEBUG = false;
    
    static int width;
    static int height;
    
    int l1x = 100;
    int l1y = 100;
    int l1z = 100;
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Create all elements in the scene
     * Could be read from a text filetatus
     */
    public void createScene() {
        AmbientLight al = new AmbientLight(new Colour(.2, .2, .2));
        Scene.setAmbientLight(al);
        
        PointLight pl1 = new PointLight(new Point(l1x, l1y, l1z), new Colour(1, 1, 1));
        //PointLight pl1 = new PointLight(new Point(0, 30, -110), new Colour(1, 1, 1));
        //Scene.addPointLight(pl1);
        Scene.addPointLight(pl1);

        double Ko = 0.7;          // Weight of this object's color
        double Kr = 0.3;          // Weight of the reflected color
        double Kt = 0;          // Weight of the refracted color

        // Sphere 1
        Texture texture1 = new Texture("earth.ppm");
        double Ka1 = .2;        // ambient
        double Kd1 = .8;        // difuse
        double Ks1 = .7;          // specular
        int n1 = 32;
        Colour color = new Colour(1, 0, 0);     // object's color
        Material material1 = new Material(Ka1, Kd1, Ks1, n1, color, Ko, Kr, Kt, texture1);
        Sphere sp1 = new Sphere(new Point(-20, 0, -200), 20, material1);
        Scene.addIntersectable(sp1);

        // Sphere 2
        Texture texture2 = new Texture("madera.ppm");
        double Ka2 = .2;        // ambient
        double Kd2 = .8;        // difuse
        double Ks2 = .7;          // specular
        int n2 = 64;
        Colour color2 = new Colour(0, 0, 1);
        Material material2 = new Material(Ka2, Kd2, Ks2, n2, color2, Ko, Kr, Kt, texture2);
        Sphere sp2 = new Sphere(new Point(20, 0, -200), 20, material2);
        Scene.addIntersectable(sp2);

        // Triangle
        Texture texture3 = new Texture("earth.ppm");
        double Ka3 = .2;        // ambient
        double Kd3 = .8;        // difuse
        double Ks3 = .7;          // specular
        int n3 = 64;
        Colour color3 = new Colour(0, 1, 0);
        Material material3 = new Material(Ka3, Kd3, Ks3, n3, color3, Ko, Kr, Kt, texture3);
        Point p1 = new Point(-30, 20, -200);
        Point p2 = new Point( 30, 20, -200);
        Point p3 = new Point(  0, 50, -200);
        Triangle triangle = new Triangle(p1, p2, p3, material3);
        Scene.addIntersectable(triangle);

    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();

        int w = width;
        int h = height;      
        
        image = new Image(w, h);
        image.generateImage();      // Compute the colors of the scene
        this.setImage(image);

        
        Colour colour;
        for(int i = 0; i < image.height; i++) {
            for(int j = 0; j < image.width; j++) {
                colour = image.image[i][j];
                int red = (int) (colour.getR() * 255);
                int green = (int) (colour.getG() * 255);
                int blue = (int) (colour.getB() * 255);
                // Clamp out of range colors
                if(red > 255) red = 255;
                if(green > 255) green = 255;
                if(blue > 255) blue = 255;
                g2d.setColor(new Color(red, green, blue));
                if(DEBUG)
                    System.out.println(red + " " + green + " " + blue);
                g2d.drawLine(i, j, i, j);
            }
        }        
    }
    
    
    public static void main(String [] args) {
        JFrame frame = new JFrame("Ray Tracer 2020");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        width = 500;
        height = 500;
        
        // Create this JPanel
        Main main = new Main();
        main.createScene();         // Add lights, objects, etc.
        // Texture image = new Texture(width, height);
        // image.generateImage();      // Compute the colors of the scene
        // main.setImage(image);

        // Draw the result
        frame.add(main);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
    }
}
