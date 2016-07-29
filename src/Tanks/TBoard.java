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
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.Timer;

public class TBoard extends javax.swing.JFrame {

    private JButton[][] btn = new JButton[10][10];
    private Spots[][] spotz = new Spots[10][10];
    private ArrayList<Integer> moves = new ArrayList<Integer>();
    private ArrayList<Integer> pmoves = new ArrayList<Integer>();
     Timer t ;
     TimerTask tt;

    public int P2tankY;
    public int P2tankX;
    public int P1tankY;
    public int P1tankX;
    public int moving = 0;

    //Contstructor Run all function 
    public TBoard() {
        initComponents();
        addbutton();
        initializePiece();
        maskingButton();
    }

    //Add button to the JFrame 10*10 as the board
    private void addbutton() {
        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                btn[y][x] = new JButton();
                pnl_Board.add(btn[y][x]);
                btn[y][x].setBackground(new java.awt.Color(255, 255, 255));
            }
        }
    }

    private void initializePiece() {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                spotz[y][x] = new Spots();
            }
        }

        spotz[0][0].occupySpot(new TankPiece(2, 0, 0, 2));
        spotz[9][9].occupySpot(new TankPiece(1, 9, 9, 1));

    }

    private void maskingButton() {
        int teams = 0;
        int state = 0;

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {

                if (spotz[y][x].isOccupied()) {
                    teams = spotz[y][x].piece.getTeam();
                    state = spotz[y][x].piece.getState();

                    try {
                        Image img = ImageIO.read(getClass().getResource("/Tank/Image/Tank" + teams + state + ".png"));
                        Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
                        btn[y][x].setIcon(new ImageIcon(newimg));

                    } catch (IOException ex) {
                    }
                } else {
                    btn[y][x].setIcon(null);
                }
            }
        }
        System.out.println("Yolo");

    }

    private void beginbattle() {
        // AIMoves();
        moves.add(2);
        moves.add(2);
        moves.add(3);
        moves.add(2);
        moves.add(4);

        pmoves.add(1);
        pmoves.add(4);
        pmoves.add(1);
        pmoves.add(1);
        pmoves.add(2);

        P2tankY = spotz[0][0].piece.getY();
        P2tankX = spotz[0][0].piece.getX();

        P1tankY = spotz[9][9].piece.getY();
        P1tankX = spotz[9][9].piece.getX();

        int turn = 2;

        //  Timer timer = new Timer(1000, listener);  
        //  timer.start();
         t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {

                if (moves.size() != 0) {
                    
                    System.out.println("Yolo" + moves.size());
                    lbl_Turn.setText("Yolo" + moves.size());

                    moving = moves.get(0);
                    moves.remove(0);
                    switch (moving) {

                        case 1:
                            if (spotz[P2tankY][P2tankX].piece.MoveUp()) {
                                spotz[P2tankY - 1][P2tankX].occupySpot(spotz[P2tankY][P2tankX].piece);
                                spotz[P2tankY - 1][P2tankX].piece.setState(moving);
                                spotz[P2tankY][P2tankX].releaseSpot();
                                MovingStuff(P2tankY, P2tankX, P2tankY - 1, P2tankX);
                                P2tankY -= 1;
                            }
                            ;
                            break;
                        case 2:
                            if (spotz[P2tankY][P2tankX].piece.MoveRight()) {
                                spotz[P2tankY][P2tankX + 1].occupySpot(spotz[P2tankY][P2tankX].piece);
                                spotz[P2tankY][P2tankX + 1].piece.setState(moving);
                                spotz[P2tankY][P2tankX].releaseSpot();
                                MovingStuff(P2tankY, P2tankX, P2tankY, P2tankX + 1);
                                P2tankX += 1;
                            }
                            ;
                            break;
                        case 3:
                            if (spotz[P2tankY][P2tankX].piece.MoveDown()) {
                                spotz[P2tankY + 1][P2tankX].occupySpot(spotz[P2tankY][P2tankX].piece);
                                spotz[P2tankY + 1][P2tankX].piece.setState(moving);
                                spotz[P2tankY][P2tankX].releaseSpot();
                                MovingStuff(P2tankY, P2tankX, P2tankY + 1, P2tankX);
                                P2tankY += 1;
                            }
                            ;
                            break;
                        case 4:
                            if (spotz[P2tankY][P2tankX].piece.MoveLeft()) {
                                spotz[P2tankY][P2tankX - 1].occupySpot(spotz[P2tankY][P2tankX].piece);
                                spotz[P2tankY][P2tankX - 1].piece.setState(moving);
                                spotz[P2tankY][P2tankX].releaseSpot();
                                MovingStuff(P2tankY, P2tankX, P2tankY, P2tankX - 1);
                                P2tankX -= 1;
                            }
                            ;
                            break;

                    }
                }else{
                t.cancel();
                tt.cancel();
                lbl_Turn.setText("Yolo Habis" );

                }

            }
        ;
        };
            t.schedule(tt, 1000, 1000);

    }

    private void MovingStuff(int Y1, int X1, int Y2, int X2) {

        if (spotz[Y2][X2].isOccupied()) {
            int teams = spotz[Y2][X2].piece.getTeam();
            int state = spotz[Y2][X2].piece.getState();

            try {
                Image img = ImageIO.read(getClass().getResource("/Tank/Image/Tank" + teams + state + ".png"));
                Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
                btn[Y2][X2].setIcon(new ImageIcon(newimg));

            } catch (IOException ex) {
            }
        }

        btn[Y1][X1].setIcon(null);

    }

    private void AIMoves() {

        Random rn = new Random();

        int randomNum = 0;
        int y = 0;
        int x = 0;
        int lastspot = 0;

        while ((moves.size() < 5)) {
            y = spotz[0][0].piece.getY();
            x = spotz[0][0].piece.getX();

            randomNum = rn.nextInt((4 - 1) + 1) + 1;

            switch (randomNum) {
                case 1:
                    if (spotz[0][0].piece.MoveUp() && lastspot != 3) {
                        moves.add(1);
                        lastspot = 1;
                    }
                    ;
                    break;
                case 2:
                    if (spotz[0][0].piece.MoveRight() && lastspot != 4) {
                        moves.add(2);
                        lastspot = 2;
                    }
                    ;
                    break;
                case 3:
                    if (spotz[0][0].piece.MoveDown() && lastspot != 1) {
                        moves.add(3);
                        lastspot = 3;
                    }
                    ;
                    break;
                case 4:
                    if (spotz[0][0].piece.MoveLeft() && lastspot != 2) {
                        moves.add(4);
                        lastspot = 4;
                    }
                    ;
                    break;

            }

            spotz[0][0].piece.setY(0);
            spotz[0][0].piece.setX(0);

            System.out.println("Y :  " + y + "    X :  " + x);

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
        pnl_Board.setLayout(new java.awt.GridLayout(10, 10));

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
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
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
                .addComponent(pnl_Board, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
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
        beginbattle();
    }//GEN-LAST:event_btn_NewActionPerformed

    private void btn_howActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_howActionPerformed


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
            java.util.logging.Logger.getLogger(TBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TBoard().setVisible(true);
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
