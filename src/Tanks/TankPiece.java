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
    private int state;

    public TankPiece(int team, int y, int x, int state) {
        super();
        this.team = team;
        this.y = y;
        this.x = x;
        this.state = state;
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
    
    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }


    //check wheter the new spot is valid move for individual pieces
    public boolean ValidMove(int pmove) {
        
        if( (pmove >= 0 ) && (pmove <= 9 )){
        return true;
        }
        return false;
        
        
    }
    
    public void MoveUp(Spots[][] spotz) {
        int tempY = this.getY();
        tempY+= 1;
        
        if(ValidMove(tempY)){
            setY(tempY);
        }
        
    }
   

    public void MoveDown(Spots[][] spotz) {
        
        int tempY = this.getY();
        tempY-= 1;
        
        if(ValidMove(tempY)){
            setY(tempY);
        }
       
    }
    
    public void MoveRight(Spots[][] spotz) {
        int tempX = this.getX();
        tempX+= 1;
        
        if(ValidMove(tempX)){
            setX(tempX);
        }
    }
    
    public void MoveLeft(Spots[][] spotz) {
        int tempX = this.getX();
        tempX+= 1;
        
        if(ValidMove(tempX)){
            setX(tempX);
        }
    }

  
}
