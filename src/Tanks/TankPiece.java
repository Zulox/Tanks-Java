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
    
    public boolean MoveUp() {
        int tempY = this.getY();
        tempY-= 1;
        
        if(ValidMove(tempY)){
            setY(tempY);
            return true;
        }
        return false;
    }
   

    public boolean MoveDown() {
        
        int tempY = this.getY();
        tempY+= 1;
        
        if(ValidMove(tempY)){
            setY(tempY);
            return true;
        }
       return false;
    }
    
    public boolean MoveRight() {
        int tempX = this.getX();
        tempX+= 1;
        
        if(ValidMove(tempX)){
            setX(tempX);
              return true;
        }
        return false;
    }
    
    public boolean MoveLeft() {
        int tempX = this.getX();
        tempX-= 1;
        
        if(ValidMove(tempX)){
            setX(tempX);
              return true;
        }
        return false;
    }
    
    public boolean FireRight( Spots[][] spotz) {
        int currentX = this.getX() + 1;
        int y = this.getY();
        
        
        for(int x = currentX ; x <= 10 ; x++){
           
            if(spotz[y][x].isOccupied()){
                return true;
            }
            
        }
        
        
        return false;
    }

  
}
