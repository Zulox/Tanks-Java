/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

public class TankPiece {

    private int team;
    private int x;
    private int y;

    public TankPiece(int team, int x, int y) {
        super();
        this.team = team;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTeam() {
        return this.team;
    }

    public void setTeam(int team) {
        this.team = team;
    }


    //check wheter the new spot is valid move for individual pieces
    public boolean ValidMove(int x, int y) {
        return false;
    }
    
    public void MoveUp(Spots[][] spotz) {
       
    }
   
    public void MoveRight(int x, int y, Spots[][] spotz) {
       
    }
    public void MoveDown(int x, int y, Spots[][] spotz) {
       
    }
    
    public void MoveLeft(int x, int y, Spots[][] spotz) {
       
    }

  
}
