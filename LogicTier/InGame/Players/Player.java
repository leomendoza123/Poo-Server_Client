/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicTier.InGame.Players;

import java.io.Serializable;

/**
 *
 * @author Leonardo
 */
public class Player implements Serializable{
    public int points; 
    public String name; 
    public Team team; 

    public Player(int points, String name) {
        this.points = points;
        this.name = name;
        this.team = null;
    }
    public Player() {
        this.team = new Team();
    }

    
    @Override
    public String toString() {
        return "player{" + "name=" + name + ", team=" + team + '}';
    }

  
    
    
    
}
