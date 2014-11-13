/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentationTier.Cliente;

import DataTier.Packs.Datapack;
import LogicTier.InGame.WaterElements.WaterElement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Leonardo
 */
public class radar extends javax.swing.JPanel {

    public Point localisation;
    public ArrayList<WaterElement> neighborhood;
    public double radar_power = 10000;
    
    private int dotsize = 10; 

    /**
     * Creates new form radar
     */
    public radar() {
        initComponents();
        neighborhood = new ArrayList (); 
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);

        Dimension size = getSize();
        Insets insets = getInsets();

        int w = size.width - 3 - insets.left - insets.right;
        int h = size.height - 3 - insets.top - insets.bottom;
        Random r = new Random();

        // Dibuja puntos azules de adorno para el radar
        for (int i = 0; i < 10000; i++) {

            int x = Math.abs(r.nextInt()) % w;
            int y = Math.abs(r.nextInt()) % h;
            g2d.drawLine(x, y, x, y);
        }
        // Dibuja perimetro del radar
        g2d.setColor(Color.BLACK);
        g2d.drawOval(0, 0, w, h);

        // Dibuja centr del radar
        g2d.setColor(Color.white);
        g2d.fillOval(w / 2 - dotsize/2, h / 2 - dotsize/2, dotsize, dotsize);

        for (WaterElement neighbor : neighborhood) {               
            
            if (neighbor != null) {
                Point neighborLocalisation = neighbor.localisation;
                double distance = localisation.distance(neighborLocalisation);
                if (distance < radar_power) {
                    double neighborXdiference = neighborLocalisation.x - localisation.x;
                    double neighborYdiference = neighborLocalisation.y - localisation.y;
                    double relative_w = radar_power / w;
                    double relative_h = radar_power / h;
                    double relative_neighborXdiference = neighborXdiference /2 / relative_w; 
                    double relative_neighborYdiference = neighborYdiference /2 / relative_h;
                    //TODO hacer color realtivo a quien es el vecino
                    g2d.setColor(Color.red);
                    g2d.fillOval(w/2-dotsize/2+(int)relative_neighborXdiference,h/2-dotsize/2+(int)relative_neighborYdiference, dotsize, dotsize);

                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}