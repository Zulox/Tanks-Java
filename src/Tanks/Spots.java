/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

//spots act as an individual spot in the board
public class Spots {

    int x;
    int y;
    Piece piece;

    public Spots() {

        piece = null;
    }

    //occupy the spot
    public void occupySpot(Piece piece) {
        this.piece = piece;
    }

    //boolean to return if spot is occupied
    public boolean isOccupied() {
        if (piece != null) {
            return true;
        }
        return false;
    }

    //delete current pieces in the spot, but return the old piece
    public Piece releaseSpot() {
        Piece releasedPiece = this.piece;
        this.piece = null;
        return releasedPiece;
    }

    //delete current pieces in the spot
    public void DeletePiece() {
        this.piece = null;
    }

}
