/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicTier.Client;

import DataTier.Packs.Datapack;
import LogicTier.InGame.WaterElements.Submarine;
import PresentationTier.Cliente.PantallaBienvenidaCliente;
import java.awt.Color;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonardo
 */
public class ClientDataInterfaceHandler extends Thread {

    Datapack datapack;
    PantallaBienvenidaCliente pantallaCliente;

    public ClientDataInterfaceHandler(Datapack datapack, PantallaBienvenidaCliente pantallaCliente) {
        this.datapack = datapack;
        this.pantallaCliente = pantallaCliente;
        this.setName("Interface Handler");
    }

    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                double depht = datapack.self.depth;
                pantallaCliente.getDepht_monitor().setValue((int)depht);
                pantallaCliente.getDepht_monitorlabel().setText("" + depht);

                double speed = datapack.self.speed;
                pantallaCliente.getSpeed_monitor().setValue((int)speed);
                pantallaCliente.getSpeed_monitorlabel().setText("" + speed);

                double direction = datapack.self.direction;
                pantallaCliente.getDirection_monitor().setValue((int) direction);
                pantallaCliente.getDirection_monitorlabel().setText("" + direction);
                
                        
                pantallaCliente.getRadar().neighborhood = datapack.neighborhood;
                pantallaCliente.getRadar().localisation = datapack.self.localisation;
                pantallaCliente.getRadar().repaint();
                
                //TODO 
                //
                 double health = datapack.self.health;
                        

            } catch (InterruptedException ex) {
                Logger.getLogger(ClientDataInterfaceHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
