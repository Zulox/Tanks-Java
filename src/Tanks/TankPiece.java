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

    //constructor that initialize the value of the tank
    public TankPiece(int team, int y, int x, int state) {
        super();
        this.team = team;
        this.y = y;
        this.x = x;
        this.state = state;
    }

    //return X coor
    public int getX() {
        return x;
    }

    //set X coor
    public void setX(int x) {
        this.x = x;
    }

    //return Y coor
    public int getY() {
        return y;
    }

    //set Y coor    
    public void setY(int y) {
        this.y = y;
    }
    
    //return the team the tank belong to
    public int getTeam() {
        return this.team;
    }

    //return the team the tank belong to        
    public void setTeam(int team) {
        this.team = team;
    }
    
     //get the position the tank is pointing
    public int getState() {
        return this.state;
    }

     //return the position the tank is pointing
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
    
    //tank move up logic
    public boolean MoveUp( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempY-= 1;
        
        if(ValidMove(tempY) && (spotz[tempY][tempX].isOccupied()== false )){
            setY(tempY);
            return true;
        }
        return false;
    }
   
    //tank move down logic
    public boolean MoveDown( Spots[][] spotz) {
        
        int tempY = this.getY();
        int tempX = this.getX();
        tempY+= 1;
        
        if(ValidMove(tempY) && (spotz[tempY][tempX].isOccupied()== false )){
            setY(tempY);
            return true;
        }
       return false;
    }

    //tank move right logic
    public boolean MoveRight( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempX+= 1;
        
        if(ValidMove(tempX) && (spotz[tempY][tempX].isOccupied()== false )){
            setX(tempX);
              return true;
        }
        return false;
    }
    
    //tank move left logic
    public boolean MoveLeft( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempX-= 1;
        
        if(ValidMove(tempX)&& (spotz[tempY][tempX].isOccupied()== false )){
            setX(tempX);
              return true;
        }
        return false;
    }
    
    //tank fire up logic
    public boolean FireUp( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempY-= 1;
        
        if( ( ValidMove(tempY) ) &&  spotz[tempY][tempX].isOccupied() ){            
            return true;
        }
        return false;
    }
    
    //tank fire right logic
    public boolean FireRight( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempX+= 1;
        
        if( ValidMove(tempX) &&  (spotz[tempY][tempX].isOccupied()) ){            
            return true;
        }
        return false;
    }
    
    
    //tank fire down logic
    public boolean FireDown( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempY+= 1;
        
        if( ( ValidMove(tempY) ) &&  spotz[tempY][tempX].isOccupied() ){            
            return true;
        }
        return false;
    }
    
    //tank fire left logic
    public boolean FireLeft( Spots[][] spotz) {
        int tempY = this.getY();
        int tempX = this.getX();
        tempX-= 1;
        
        if( ( ValidMove(tempX) ) &&  ( spotz[tempY][tempX].isOccupied() ) ){            
            return true;
        }
        return false;
    }



  
}
