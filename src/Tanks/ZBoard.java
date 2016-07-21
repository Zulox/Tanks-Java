/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tanks;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ZBoard extends javax.swing.JFrame implements ActionListener {

    private JButton[][] btn = new JButton[6][7];
    private Spots[][] spotz = new Spots[6][7];

    private boolean ButtonState = false;
    private int tempX = 0;
    private int tempY = 0;
    private int currentTurn = 1;
    private int intervals = 0;
    private boolean gamefinished = false;

    //Contstructor Run all function 
    public ZBoard() {
        initComponents();
        addbutton();
        initializePiece();
        maskingButton();
    }

    //Function to apply icon to occupied buttons
    private void maskingButton() {
        int teams = 0;
        int types = 0;
        String specialtype = "";

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if (spotz[i][j].isOccupied()) {
                    teams = spotz[i][j].piece.getTeam();
                    types = spotz[i][j].piece.getTypes();
                    specialtype = "" + types;
                    if (types == 2 || types == 3) {
                        if (teams != currentTurn) {
                            specialtype = specialtype + "R";
                        }
                    }

                    try {
                        Image img = ImageIO.read(getClass().getResource("/Zaronian/Image/" + teams + specialtype + ".png"));
                        Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
                        btn[i][j].setIcon(new ImageIcon(newimg));

                    } catch (IOException ex) {
                    }

                } else {
                    btn[i][j].setText("  ");
                }
            }
        }
    }

    //Create piece into their own team, type and starting position
    private void initializePiece() {

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                spotz[i][j] = new Spots();
            }
        }

        spotz[0][0].occupySpot(new Plus(2, 0, 0));
        spotz[0][1].occupySpot(new Triangle(2, 0, 1));
        spotz[0][2].occupySpot(new Chevron(2, 0, 2));
        spotz[0][3].occupySpot(new Sun(2, 0, 3));
        spotz[0][4].occupySpot(new Chevron(2, 0, 4));
        spotz[0][5].occupySpot(new Triangle(2, 0, 5));
        spotz[0][6].occupySpot(new Plus(2, 0, 6));

        spotz[5][0].occupySpot(new Plus(1, 5, 0));
        spotz[5][1].occupySpot(new Triangle(1, 5, 1));
        spotz[5][2].occupySpot(new Chevron(1, 5, 2));
        spotz[5][3].occupySpot(new Sun(1, 5, 3));
        spotz[5][4].occupySpot(new Chevron(1, 5, 4));
        spotz[5][5].occupySpot(new Triangle(1, 5, 5));
        spotz[5][6].occupySpot(new Plus(1, 5, 6));

    }

    //Add button to the JFrame 6*7 as the board
    private void addbutton() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                btn[i][j] = new JButton();
                pnl_Board.add(btn[i][j]);
                btn[i][j].addActionListener((ActionListener) this);/**/

                btn[i][j].setBackground(new java.awt.Color(255, 255, 255));
            }
        }
    }

    //Flip the board --called after player finished their turn
    private void flipboard(int z, int x) {
        pnl_Board.removeAll();

        if (currentTurn == 1) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    btn[i][j] = new JButton();
                    pnl_Board.add(btn[i][j]);
                    btn[i][j].addActionListener((ActionListener) this);
                    btn[i][j].setBackground(new java.awt.Color(255, 255, 255));
                }
            }

            btn[z][x].setBackground(new java.awt.Color(204, 204, 255));

           
            if (intervals != 0 && intervals % 3 == 0) {
                ChangePiece();
            }

        } else {
            for (int i = 5; i >= 0; i--) {
                for (int j = 6; j >= 0; j--) {
                    btn[i][j] = new JButton();
                    pnl_Board.add(btn[i][j]);
                    btn[i][j].addActionListener((ActionListener) this);
                    btn[i][j].setBackground(new java.awt.Color(255, 255, 255));
                     btn[z][x].setBackground(new java.awt.Color(255, 204, 204));
                }
            }

        }

        maskingButton();

    }

    /*The main game logic, handle things such as
     -selecting/deselecting you piece
     -Moving piece
    
     */
    private void ComplexGameLogic(int i, int j) {
        if (ButtonState == false) {
            if (spotz[i][j].isOccupied() && spotz[i][j].piece.getTeam() == currentTurn) {
                tempX = i;
                tempY = j;
                //highlight Button Code
                btn[i][j].setBackground(new java.awt.Color(153, 255, 153));
                ButtonState = true;
            } else {
                lbl_Note.setText("Not your piece");
            }
        } else if (tempX == i && tempY == j) {
            ButtonState = false;

            btn[i][j].setBackground(new java.awt.Color(255, 255, 255));

        } else if (spotz[tempX][tempY].piece.ValidMove(i, j, spotz)) {

            if (spotz[i][j].isOccupied() && spotz[i][j].piece.getTeam() == currentTurn) {
                lbl_Note.setText("[Same Team]");
                spotz[tempX][tempY].piece.setX(tempX);
                spotz[tempX][tempY].piece.setY(tempY);

            } else {

                if (spotz[i][j].isOccupied() && spotz[i][j].piece.getTypes() == 4) {

                    Winthegame(currentTurn);
                }
                spotz[i][j].occupySpot(spotz[tempX][tempY].releaseSpot());
                ButtonState = false;

                btn[tempX][tempY].setBackground(new java.awt.Color(255, 255, 255));
                if (currentTurn == 1) {
                    currentTurn = 2;
                    lbl_Turn.setText("" + currentTurn);
                    lbl_Turn.setForeground(Color.blue);
                    btn[i][j].setBackground(Color.blue);
                    if (gamefinished == false) {
                        lbl_Note.setText("Valid");
                        lbl_Note.setForeground(Color.blue);
                        flipboard(i, j);
                    }

                } else {
                    intervals++;
                    lbl_Interval.setText("" + (intervals + 1));
                    currentTurn = 1;
                    lbl_Turn.setText("" + currentTurn);
                    lbl_Turn.setForeground(Color.red);
                    btn[i][j].setBackground(Color.blue);

                    if (gamefinished == false) {
                        lbl_Note.setText("Valid");
                        lbl_Note.setForeground(Color.red);
                        flipboard(i, j);
                    }
                }

            }

        } else {
            lbl_Note.setText("Invalid Path");
        }
    }

    //Dynamic Button action
    public void actionPerformed(ActionEvent evt) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (evt.getSource() == btn[i][j]) {
                    ComplexGameLogic(i, j);

                }
            }
        }
    }

    //handle what happen if game is won
    public void Winthegame(int currentTurn) {
        gamefinished = true;
        String Msgs = "Player " + currentTurn + " Win";
        JOptionPane.showMessageDialog(null, Msgs, "GAME OVER", JOptionPane.PLAIN_MESSAGE);
        lbl_Note.setText(Msgs);
        lbl_Note.setForeground(new java.awt.Color(0, 153, 153));

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                btn[i][j].setEnabled(false);
            }
        }

    }

    //handle converting the piece to other type after 3 turns
    private void ChangePiece() {
        int team = 0;
        int types = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                if (spotz[i][j].isOccupied()) {
                    team = spotz[i][j].piece.getTeam();
                    types = spotz[i][j].piece.getTypes();

                    if (types == 1) {
                        spotz[i][j].DeletePiece();
                        spotz[i][j].occupySpot(new Triangle(team, i, j));
                    } else if (types == 2) {
                        spotz[i][j].DeletePiece();
                        spotz[i][j].occupySpot(new Chevron(team, i, j));
                    } else if (types == 3) {
                        spotz[i][j].DeletePiece();
                        spotz[i][j].occupySpot(new Plus(team, i, j));
                    } else {
                    }

                }

            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_Board = new javax.swing.JPanel();
        pnl_info = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbl_Turn = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbl_Interval = new javax.swing.JLabel();
        lbl_Note = new javax.swing.JLabel();
        btn_New = new javax.swing.JButton();
        btn_how = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnl_Board.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_Board.setLayout(new java.awt.GridLayout(6, 7));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TURN");

        lbl_Turn.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbl_Turn.setForeground(new java.awt.Color(255, 51, 51));
        lbl_Turn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Turn.setText("1");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Interval");

        lbl_Interval.setFont(new java.awt.Font("Trebuchet MS", 1, 36)); // NOI18N
        lbl_Interval.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Interval.setText("1");

        lbl_Note.setFont(new java.awt.Font("Arial", 0, 34)); // NOI18N
        lbl_Note.setForeground(new java.awt.Color(255, 51, 51));
        lbl_Note.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Note.setText("None");
        lbl_Note.setToolTipText("");

        btn_New.setBackground(new java.awt.Color(204, 255, 204));
        btn_New.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btn_New.setText("NEW GAME");
        btn_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NewActionPerformed(evt);
            }
        });

        btn_how.setBackground(new java.awt.Color(153, 255, 255));
        btn_how.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_how.setText("HOW TO PLAY");
        btn_how.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_howActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_infoLayout = new javax.swing.GroupLayout(pnl_info);
        pnl_info.setLayout(pnl_infoLayout);
        pnl_infoLayout.setHorizontalGroup(
            pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_how, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Interval, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btn_New, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Turn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Note, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnl_infoLayout.setVerticalGroup(
            pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_New, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Turn)
                .addGap(18, 18, 18)
                .addComponent(lbl_Note, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Interval)
                .addGap(39, 39, 39)
                .addComponent(btn_how, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_Board, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_info, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_Board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NewActionPerformed
        this.dispose();

        new ZBoard().setVisible(true);
    }//GEN-LAST:event_btn_NewActionPerformed

    private void btn_howActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_howActionPerformed
        new HowtoPlay().setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_btn_howActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ZBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ZBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ZBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ZBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ZBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_New;
    private javax.swing.JButton btn_how;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lbl_Interval;
    private javax.swing.JLabel lbl_Note;
    private javax.swing.JLabel lbl_Turn;
    private javax.swing.JPanel pnl_Board;
    private javax.swing.JPanel pnl_info;
    // End of variables declaration//GEN-END:variables
}
