/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTier.Server;

import DataTier.Packs.Datapack;
import LogicTier.InGame.WaterElements.WaterElement;
import java.io.*;
import java.net.*;
import java.util.Vector;
import javax.swing.JTextArea;

/**
 *
 * @author Jose
 */
public class TheardToClient extends Thread {

    Socket client = null;//referencia a socket de comunicacion de cliente
    DataInputStream entrada = null;//Para leer comunicacion
    DataOutputStream salida = null;//Para enviar comunicacion	
    Server server;// referencia al servidor

    // para envio de mensajes al enemigo
    TheardToClient enemigo = null;
    // identificar el numero de jugador
    JTextArea server_text_area;
    Datapack Serverpack; 

    public TheardToClient(Socket cliente, Server serv, JTextArea server_text_area, Datapack DatapackPointer) {
        this.client = cliente;
        this.server = serv;
        this.server_text_area = server_text_area;
        Serverpack =  DatapackPointer; 
        
        this.setName("(Server)Data Handler");
    }

    @Override
    public void run() {
        server_text_area.append("New client conected \n");

        while (true) {
            try {
                // inicializa para lectura y escritura con stream de cliente
                ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                Datapack Clientpack;
                
                while (true) {
                    //>>>>>>>>>>>>>>>>>>>>>< Lee datos de Cliente
                    Clientpack = (Datapack) ois.readObject();
                    if (Clientpack != null) {
                        
                        if (Serverpack.self == null){
                            // Si es la primera lectura
                            Serverpack = FirstServerConenction(Clientpack); 
                        }
                        else{
                            DataFromClientpackToServerPack(Clientpack, Serverpack);
                        }
                    //>>>>>>>>>>>>>>>>>>>>< Escribe datos a cliente
                        Serverpack.DatapackLock.lock();
                        try {
                            oos.reset();
                            oos.writeObject(Serverpack);
                        } 
                        catch (IOException ex) {
                            System.out.println("Error enviando datapack:" + ex);
                        }
                        Serverpack.DatapackLock.unlock();


                    }

                }

            } catch (Exception e) {
                server_text_area.append("Server-Client theard error '\n" + e);
                break;
            }
        }
    }

    private Datapack FirstServerConenction(Datapack Clientpack) {
        // En caso de que sea la primera entrada al servidor
        
        Serverpack.player = Clientpack.player;
        Serverpack.self = Clientpack.self;
        return Serverpack;
    }

    private void DataFromClientpackToServerPack(Datapack Clientpack, Datapack Serverpack) {
        Serverpack.DatapackLock.lock();
        String data = Clientpack.self.toString() + "\n";
        server_text_area.append(data);
        Serverpack.player = Clientpack.player;
        Serverpack.self.WaterElementLock.lock();
        Serverpack.self.control_depht = Clientpack.self.control_depht;
        Serverpack.self.control_direction = Clientpack.self.control_direction;
        Serverpack.self.control_speed = Clientpack.self.control_speed;
        Serverpack.self.WaterElementLock.unlock();
        Serverpack.DatapackLock.unlock();
    }
}