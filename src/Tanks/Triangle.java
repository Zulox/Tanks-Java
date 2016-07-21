/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

import java.io.IOException;

public class Triangle extends Piece {

    public Triangle(int team, int x, int y) {
        super(team, x, y);
        super.setTypes(2);
        // TODO Auto-generated constructor stub
    }
    
    //check wheter the new spot is valid move for individual pieces
    @Override
    public boolean ValidMove(int x, int y, Spots[][] spotz) {
        int tempX = x - super.getX();
        int tempY = y - super.getY();
        int X2 = tempX;
        int Y2 = tempY;

        tempX = Math.abs(tempX);
        tempY = Math.abs(tempY);
        System.out.println("X =" + tempX + "    Y = " + tempY);
        System.out.println("X =" + x + "    Y = " + y);
        System.out.println("X =" + super.getX() + "    Y = " + super.getY());

        if ((tempX == tempY) && (tempX == 1)) {
            super.setX(x);
            super.setY(y);
            return true;
        } else if ((tempX == tempY) && (tempX == 2)) {
            if (X2 == 2 && Y2 == 2) {
                System.out.println("1");
                if (spotz[x - 1][y - 1].isOccupied()) {
                    return false;
                } else {
                    super.setX(x);
                    super.setY(y);
                    return true;
                }
            } else if (X2 == 2 && Y2 == -2) {
                System.out.println("2");
                if (spotz[x - 1][y + 1].isOccupied()) {
                    return false;
                } else {
                    super.setX(x);
                    super.setY(y);
                    return true;
                }
            } else if (X2 == -2 && Y2 == 2) {
                System.out.println("3");
                if (spotz[x + 1][y - 1].isOccupied()) {
                    return false;
                } else {
                    super.setX(x);
                    super.setY(y);
                    return true;
                }
            } else if (X2 == -2 && Y2 == -2) {
                System.out.println("4");
                if (spotz[x + 1][y + 1].isOccupied()) {
                    return false;
                } else {
                    super.setX(x);
                    super.setY(y);
                    return true;
                }
            }

        }
        return false;
    }

}
