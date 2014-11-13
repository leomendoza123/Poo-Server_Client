/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicTier.Server;

import DataTier.Packs.Datapack;
import DataTier.Server.TheardToClient;
import LogicTier.InGame.WaterElements.Ship;
import LogicTier.InGame.WaterElements.WaterElement;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leonardo
 */
public class Ocean extends Thread {

    public Lock OceanListLock = new ReentrantLock();
    ArrayList<WaterElement> neighborhood = new ArrayList<>();
    ArrayList<Datapack> clients = new ArrayList<>();
    boolean OceanRuning;

    public boolean addclient(Datapack e) {
        OceanListLock.lock();
        boolean result = clients.add(e);
        OceanListLock.unlock();
        return result;
    }

    private boolean addneighbor(WaterElement e) {
        OceanListLock.lock();
        boolean result = neighborhood.add(e);
        OceanListLock.unlock();
        return result;
    }

    public Ocean() {
        OceanRuning = true;
        this.setName("Ocean");
        initializeShips();
    }

    @Override
    public void run() {
        while (OceanRuning) {
            clientNeighborhoodUpdate();
            controlTest();
            addNewWaterElementsFromClients(); 
            startNewWaterElements();
        }
    }

    private void startNewWaterElements() {
        OceanListLock.lock();
        for (WaterElement waterElement : neighborhood) {
            if (waterElement != null) {
                if (!waterElement.runing) {
                    waterElement.WaterElementLock.lock();
                    waterElement.setName("Water Element:" + waterElement.getId());
                    waterElement.runing = true;
                    waterElement.start();
                    waterElement.WaterElementLock.unlock();
                }
            }
        }
        OceanListLock.unlock();
    }
    
        private void addNewWaterElementsFromClients() {
        OceanListLock.lock();
        for (Datapack client : clients) {
            if (client.self != null && !neighborhood.contains(client.self)) {
                addneighbor(client.self);
            }
        }
        OceanListLock.unlock();
    }

    private void initializeShips() {

        Random r = new Random();
        Point Localization;
        for (int i = 0; i < 3; i++) {
            Localization = new Point(r.nextInt() % 1000, r.nextInt() % 1000);
            Ship newShip = new Ship(10, 0, 0, 0, Color.BLACK, 0.0, Localization);
            neighborhood.add(newShip);
        }
    }

    private void controlTest() {

        try {
            for (Datapack clientePack : clients) {
                clientePack.DatapackLock.lock();
                try {
                    clientePack.self.depth = clientePack.self.control_depht;
                    clientePack.self.direction = (double) clientePack.self.control_direction;
                    clientePack.self.speed = clientePack.self.control_speed;
                } finally {
                    clientePack.DatapackLock.unlock();
                }

            }
        } catch (java.lang.NullPointerException ex) {
        }

    }

    private void clientNeighborhoodUpdate() {

        OceanListLock.lock();
        for (Datapack datapack : clients) {
            datapack.DatapackLock.lock();
            try {
                datapack.neighborhood = new ArrayList<>();
                for (WaterElement neighbor : neighborhood) {
                    datapack.neighborhood.add(neighbor);
                }
            } finally {
                datapack.DatapackLock.unlock();
            }
        }
        OceanListLock.unlock();

    }
}