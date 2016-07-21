/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

public class Piece {

    private int team;
    private int x;
    private int y;
    private int types;

    public Piece(int team, int x, int y) {
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

    public int getTypes() {
        return this.types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    //check wheter the new spot is valid move for individual pieces
    public boolean ValidMove(int x, int y, Spots[][] spotz) {
        return false;
    }

  
}
