/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

public class Sun extends Piece {

    public Sun(int team, int x, int y) {
        super(team, x, y);
        super.setTypes(4);
        // TODO Auto-generated constructor stub
    }
    
    
    //check wheter the new spot is valid move for individual pieces
    @Override
    public boolean ValidMove(int x, int y, Spots[][] spotz) {
        int tempX = x - super.getX();
        int tempY = y - super.getY();

        tempX = Math.abs(tempX);
        tempY = Math.abs(tempY);

        if (tempX == 0) {
            tempX++;
        }
        if (tempY == 0) {
            tempY++;
        }

        int checker = tempX * tempY;
        System.out.println("X =" + tempX + "    Y = " + tempY);

        if ((checker == 0) || (checker == 1)) {
            super.setX(x);
            super.setY(y);
            return true;
        }

        return false;
    }

}
