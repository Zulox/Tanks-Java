/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

public class Plus extends Piece {

    public Plus(int team, int x, int y) {
        super(team, x, y);
        super.setTypes(1);
        
    }
    
    //check wheter the new spot is valid move for individual pieces
    @Override
    public boolean ValidMove(int x, int y, Spots[][] spotz) {
        int tempX = 0;
        int tempY = 0;

        if (super.getY() == y) {
            tempX = super.getX() - x;
            //tempX = Math.abs(tempX);
            System.out.println("TEMP X VALUE = " + tempX);
            if (tempX <= 1 && tempX >= -1 && tempX != 0) {
                super.setX(x);
                return true;
            } else if (tempX == -2) {
                if (spotz[x - 1][y].isOccupied()) {
                    System.out.println("X- " + x + "  Y+ " + y);
                    return false;
                } else {
                    super.setX(x);
                    return true;
                }
            } else if (tempX == 2) {
                if (spotz[x + 1][y].isOccupied()) {
                    System.out.println("X+ " + x + "  Y+ " + y);
                    return false;
                } else {
                    super.setX(x);
                    return true;
                }
            }

        }

        if (super.getX() == x) {
            tempY = super.getY() - y;
            //tempY = Math.abs(tempY);
            System.out.println("Y " + tempY);
            if (tempY <= 1 && tempY >= -1 && tempY != 0) {
                super.setY(y);
                return true;
            } else if (tempY == -2) {
                if (spotz[x][y - 1].isOccupied()) {
                    System.out.println("X " + x + "  Y- " + y);
                    return false;
                } else {
                    super.setY(y);
                    return true;
                }
            } else if (tempY == 2) {
                if (spotz[x][y + 1].isOccupied()) {
                    System.out.println("X " + x + "  Y+ " + y);
                    return false;
                } else {
                    super.setY(y);
                    return true;
                }
            }

        }

        return false;
    }

 
}
