/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

public class Chevron extends Piece {

    public Chevron(int team, int x, int y) {
        super(team, x, y);
        super.setTypes(3);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean ValidMove(int x, int y, Spots[][] spotz) {
        int tempX = x - super.getX();
        int tempY = y - super.getY();

        tempX = Math.abs(tempX);
        tempY = Math.abs(tempY);
        System.out.println("total =" + (tempX + tempY));

        if ((tempX + tempY) == 3) {
            super.setX(x);
            super.setY(y);
            return true;

        }

        return false;
    }

  
}
