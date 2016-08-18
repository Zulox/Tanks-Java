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

    DefaultListModel model = new DefaultListModel();
    private JButton[][] btn = new JButton[8][8];
    private Spots[][] spotz = new Spots[8][8];
    private ArrayList<Integer> aimoves = new ArrayList<Integer>();
    private ArrayList<Integer> moves = new ArrayList<Integer>();
    private ArrayList<Integer> pmoves = new ArrayList<Integer>();
    private boolean playerWin = false;
    private boolean AIWin = false;
    private int tries = 1;
    private int listpos = 0;
    Timer t;
    TimerTask tt;

    public int P2tankY;
    public int P2tankX;
    public int P1tankY;
    public int P1tankX;

    public boolean playerTurn = false;

    //Contstructor Run all function 
    public TBoard() {
        initComponents();
        lst_moves.setModel(model);
        addbutton();
        initializePiece();
        maskingButton();
        
        generateAImoves();
    }

    //Add button to the JFrame 10*10 as the board
    private void addbutton() {
        for (int y = 0; y <= 7; y++) {
            for (int x = 0; x <= 7; x++) {
                btn[y][x] = new JButton();
                pnl_Board.add(btn[y][x]);
                btn[y][x].setBackground(new java.awt.Color(255, 255, 255));
            }
        }
    }

    private void initializePiece() {

        for (int y = 0; y <= 7; y++) {
            for (int x = 0; x <= 7; x++) {
                spotz[y][x] = new Spots();
            }
        }

        spotz[0][0].occupySpot(new TankPiece(2, 0, 0, 2));
        spotz[7][7].occupySpot(new TankPiece(1, 7, 7, 1));

    }

    private void REinitializePiece() {

        for (int y = 0; y <= 7; y++) {
            for (int x = 0; x <= 7; x++) {
                spotz[y][x].DeletePiece();
            }
        }

        spotz[0][0].occupySpot(new TankPiece(2, 0, 0, 2));
        spotz[7][7].occupySpot(new TankPiece(1, 7, 7, 1));

    }
    
    private void resetboard(){
    
            REinitializePiece();
        maskingButton();
    
    }

    private void maskingButton() {
        int teams = 0;
        int state = 0;

        for (int y = 0; y <= 7; y++) {
            for (int x = 0; x <= 7; x++) {

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
    }

    private void beginbattle() {
        /* 
        moves.add(2);
        moves.add(3);
        moves.add(2);
        moves.add(2);
        moves.add(2);
        moves.add(3);
        moves.add(3);
        moves.add(3);
        moves.add(6);
        moves.add(1);
        moves.add(1);

        pmoves.add(1);
        pmoves.add(4);
        pmoves.add(4);
        pmoves.add(1);
        pmoves.add(1);
        pmoves.add(1);
        pmoves.add(4);
        pmoves.add(4);
        pmoves.add(4);
        pmoves.add(1);
        pmoves.add(1);
        */

        P2tankY = spotz[0][0].piece.getY();
        P2tankX = spotz[0][0].piece.getX();

        P1tankY = spotz[7][7].piece.getY();
        P1tankX = spotz[7][7].piece.getX();

        //  Timer timer = new Timer(1000, listener);  
        //  timer.start();
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {

                if ((moves.size() != 0) || (pmoves.size() != 0)) {
                    PlayerTurn();
                    AITurn();
                    
                } else {
                    t.cancel();
                    tt.cancel();
                    JOptionPane.showMessageDialog(null, "You Failed to destroy the enemy tank", "TRY AGAIN", JOptionPane.PLAIN_MESSAGE);
                    tries++;
                    lbl_Tries.setText("" + tries);
                    resetboard();
                }

            }
        ;
        };
        t.schedule(tt, 1000, 1000);

    }

    private void AITurn() {

        int moving = moves.get(0);
        moves.remove(0);
        switch (moving) {

            case 1:
                if (spotz[P2tankY][P2tankX].piece.MoveUp(spotz)) {
                    spotz[P2tankY - 1][P2tankX].occupySpot(spotz[P2tankY][P2tankX].piece);
                    spotz[P2tankY - 1][P2tankX].piece.setState(moving);
                    spotz[P2tankY][P2tankX].releaseSpot();
                    MovingAvatar(P2tankY, P2tankX, P2tankY - 1, P2tankX);
                    P2tankY -= 1;
                } else {
                    spotz[P2tankY][P2tankX].piece.setState(moving);
                    StaticAvatar(P2tankY, P2tankX);
                }
                ;
                break;
            case 2:
                if (spotz[P2tankY][P2tankX].piece.MoveRight(spotz)) {
                    spotz[P2tankY][P2tankX + 1].occupySpot(spotz[P2tankY][P2tankX].piece);
                    spotz[P2tankY][P2tankX + 1].piece.setState(moving);
                    spotz[P2tankY][P2tankX].releaseSpot();
                    MovingAvatar(P2tankY, P2tankX, P2tankY, P2tankX + 1);
                    P2tankX += 1;
                } else {
                    spotz[P2tankY][P2tankX].piece.setState(moving);
                    StaticAvatar(P2tankY, P2tankX);

                }
                ;
                break;
            case 3:
                if (spotz[P2tankY][P2tankX].piece.MoveDown(spotz)) {
                    spotz[P2tankY + 1][P2tankX].occupySpot(spotz[P2tankY][P2tankX].piece);
                    spotz[P2tankY + 1][P2tankX].piece.setState(moving);
                    spotz[P2tankY][P2tankX].releaseSpot();
                    MovingAvatar(P2tankY, P2tankX, P2tankY + 1, P2tankX);
                    P2tankY += 1;
                } else {
                    spotz[P2tankY][P2tankX].piece.setState(moving);
                    StaticAvatar(P2tankY, P2tankX);

                }
                ;
                break;
            case 4:
                if (spotz[P2tankY][P2tankX].piece.MoveLeft(spotz)) {
                    spotz[P2tankY][P2tankX - 1].occupySpot(spotz[P2tankY][P2tankX].piece);
                    spotz[P2tankY][P2tankX - 1].piece.setState(moving);
                    spotz[P2tankY][P2tankX].releaseSpot();
                    MovingAvatar(P2tankY, P2tankX, P2tankY, P2tankX - 1);
                    P2tankX -= 1;
                } else {
                    spotz[P2tankY][P2tankX].piece.setState(moving);
                    StaticAvatar(P2tankY, P2tankX);

                }
                ;
                break;
            case 5:
                spotz[P2tankY][P2tankX].piece.setState(moving);
                StaticAvatar(P2tankY, P2tankX);

                if (spotz[P2tankY][P2tankX].piece.FireUp(spotz)) {
                    AIWin = true;
                    JOptionPane.showMessageDialog(null, "AI tank shot you down", "TRY AGAIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                    tries++;
                    lbl_Tries.setText("" + tries);
                    resetboard();
                }
                ;
                break;
            case 6:
                spotz[P2tankY][P2tankX].piece.setState(moving);
                StaticAvatar(P2tankY, P2tankX);

                if (spotz[P2tankY][P2tankX].piece.FireRight(spotz)) {
                    AIWin = true;
                    JOptionPane.showMessageDialog(null, "AI tank shot you down", "TRY AGAIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                    tries++;
                    lbl_Tries.setText("" + tries);
                    resetboard();
                }
                ;
                break;
            case 7:
                spotz[P2tankY][P2tankX].piece.setState(moving);
                StaticAvatar(P2tankY, P2tankX);

                if (spotz[P2tankY][P2tankX].piece.FireDown(spotz)) {
                    AIWin = true;
                    JOptionPane.showMessageDialog(null, "AI tank shot you down", "TRY AGAIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                    tries++;
                    lbl_Tries.setText("" + tries);
                    resetboard();
                }
                ;
                break;
            case 8:
                spotz[P2tankY][P2tankX].piece.setState(moving);
                StaticAvatar(P2tankY, P2tankX);

                if (spotz[P2tankY][P2tankX].piece.FireLeft(spotz)) {
                    AIWin = true;
                    JOptionPane.showMessageDialog(null, "AI tank shot you down", "TRY AGAIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                    tries++;
                    lbl_Tries.setText("" + tries);
                    resetboard();
                }
                ;
                break;
        }
    }

    private void PlayerTurn() {

        
        lst_moves.setSelectedIndex(listpos);
        listpos++;
        
        int moving = pmoves.get(0);
        pmoves.remove(0);
        switch (moving) {

            case 1:
                if (spotz[P1tankY][P1tankX].piece.MoveUp(spotz)) {
                    spotz[P1tankY - 1][P1tankX].occupySpot(spotz[P1tankY][P1tankX].piece);
                    spotz[P1tankY - 1][P1tankX].piece.setState(moving);
                    spotz[P1tankY][P1tankX].releaseSpot();
                    MovingAvatar(P1tankY, P1tankX, P1tankY - 1, P1tankX);
                    P1tankY -= 1;

                } else {
                    spotz[P1tankY][P1tankX].piece.setState(moving);
                    StaticAvatar(P1tankY, P1tankX);

                }
                ;
                break;
            case 2:
                if (spotz[P1tankY][P1tankX].piece.MoveRight(spotz)) {
                    spotz[P1tankY][P1tankX + 1].occupySpot(spotz[P1tankY][P1tankX].piece);
                    spotz[P1tankY][P1tankX + 1].piece.setState(moving);
                    spotz[P1tankY][P1tankX].releaseSpot();
                    MovingAvatar(P1tankY, P1tankX, P1tankY, P1tankX + 1);
                    P1tankX += 1;

                } else {
                    spotz[P1tankY][P1tankX].piece.setState(moving);
                    StaticAvatar(P1tankY, P1tankX);

                }
                ;
                break;
            case 3:
                if (spotz[P1tankY][P1tankX].piece.MoveDown(spotz)) {
                    spotz[P1tankY + 1][P1tankX].occupySpot(spotz[P1tankY][P1tankX].piece);
                    spotz[P1tankY + 1][P1tankX].piece.setState(moving);
                    spotz[P1tankY][P1tankX].releaseSpot();
                    MovingAvatar(P1tankY, P1tankX, P1tankY + 1, P1tankX);
                    P1tankY += 1;

                } else {
                    spotz[P1tankY][P1tankX].piece.setState(moving);
                    StaticAvatar(P1tankY, P1tankX);

                }
                ;
                break;
            case 4:
                if (spotz[P1tankY][P1tankX].piece.MoveLeft(spotz)) {
                    spotz[P1tankY][P1tankX - 1].occupySpot(spotz[P1tankY][P1tankX].piece);
                    spotz[P1tankY][P1tankX - 1].piece.setState(moving);
                    spotz[P1tankY][P1tankX].releaseSpot();
                    MovingAvatar(P1tankY, P1tankX, P1tankY, P1tankX - 1);
                    P1tankX -= 1;

                } else {
                    spotz[P1tankY][P1tankX].piece.setState(moving);
                    StaticAvatar(P1tankY, P1tankX);

                }

                ;
                break;
            case 5:
                spotz[P1tankY][P1tankX].piece.setState(moving);
                StaticAvatar(P1tankY, P1tankX);

                if (spotz[P1tankY][P1tankX].piece.FireUp(spotz)) {
                    playerWin = true;
                    JOptionPane.showMessageDialog(null, "You manage to take down the AI Tank", "YOU WIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                }
                ;
                break;
            case 6:
                spotz[P1tankY][P1tankX].piece.setState(moving);
                StaticAvatar(P1tankY, P1tankX);

                if (spotz[P1tankY][P1tankX].piece.FireRight(spotz)) {
                    playerWin = true;
                    JOptionPane.showMessageDialog(null, "You manage to take down the AI Tank", "YOU WIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                }
                ;
                break;
            case 7:
                spotz[P1tankY][P1tankX].piece.setState(moving);
                StaticAvatar(P1tankY, P1tankX);

                if (spotz[P1tankY][P1tankX].piece.FireDown(spotz)) {
                    playerWin = true;
                    JOptionPane.showMessageDialog(null, "You manage to take down the AI Tank", "YOU WIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                }
                ;
                break;
            case 8:
                spotz[P1tankY][P1tankX].piece.setState(moving);
                StaticAvatar(P1tankY, P1tankX);

                if (spotz[P1tankY][P1tankX].piece.FireLeft(spotz)) {
                    playerWin = true;
                    JOptionPane.showMessageDialog(null, "You manage to take down the AI Tank", "YOU WIN", JOptionPane.PLAIN_MESSAGE);
                    t.cancel();
                    tt.cancel();
                }
                ;
                break;

        }

    }

    private void MovingAvatar(int Y1, int X1, int Y2, int X2) {

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

    private void StaticAvatar(int Y1, int X1) {

        if (spotz[Y1][X1].isOccupied()) {
            int teams = spotz[Y1][X1].piece.getTeam();
            int state = spotz[Y1][X1].piece.getState();

            try {
                Image img = ImageIO.read(getClass().getResource("/Tank/Image/Tank" + teams + state + ".png"));
                Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
                btn[Y1][X1].setIcon(new ImageIcon(newimg));

            } catch (IOException ex) {
            }
        }

    }

    private void generateAImoves() {

        //Random rn = new Random();

        int randomNum = 0;
        int y = 0;
        int x = 0;
        int lastspot = 0;

        while ((aimoves.size() < 18)) {
           // y = spotz[0][0].piece.getY();
           // x = spotz[0][0].piece.getX();

            
            randomNum = getRandomInteger(10, 1);
            //randomNum = rn.nextInt((8 - 1) + 1) + 1;

            switch (randomNum) {
                case 1:
                    //if (spotz[0][0].piece.MoveUp(spotz) && lastspot != 3) {
                         aimoves.add(1);
                    //    lastspot = 1;
                    // }
                    ;
                    break;
                case 2:
                   // if (spotz[0][0].piece.MoveRight(spotz) && lastspot != 4) {
                        aimoves.add(2);
                   //     lastspot = 2;
                   // }
                    ;
                    break;
                case 3:
                   // if (spotz[0][0].piece.MoveDown(spotz) && lastspot != 1) {
                        aimoves.add(3);
                   //    lastspot = 3;
                   // }
                    ;
                    break;
                case 4:
                   // if (spotz[0][0].piece.MoveLeft(spotz) && lastspot != 2) {
                        aimoves.add(4);
                   //     lastspot = 4;
                   // }
                    ;
                    break;
                case 5:
                    aimoves.add(5);                                     
                    ;
                    break;
                case 6:
                    aimoves.add(6);                                     
                    ;
                    break;
                case 7:
                    aimoves.add(7);                                     
                    ;
                    break;        
                case 8:
                    aimoves.add(8);                                     
                    ;
                    break; 
            }
           // spotz[0][0].piece.setY(0);
          //  spotz[0][0].piece.setX(0);          
        }
    }
   
    public static int getRandomInteger(int maximum, int minimum){
       return ((int) (Math.random()*(maximum - minimum))) + minimum; 
    }




    
    private void CopyAIMove(){
        moves.clear();
        
        for(int i = 0 ; i < 18 ; i++){
        moves.add(aimoves.get(i));
        }

        for(int i = 0 ; i < 18 ; i++){
        System.out.println("" + moves.get(i));
        }

    }

    private void generatePlayermove() {

        String placeholder;
        for (int i = 0; i < 18; i++) {

            placeholder = (String) model.getElementAt(i);

            if (placeholder.length() < 7) {

                if (placeholder.equals("UP")) {
                    pmoves.add(1);
                }
                if (placeholder.equals("RIGHT")) {
                    pmoves.add(2);
                }
                if (placeholder.equals("DOWN")) {
                    pmoves.add(3);
                }
                if (placeholder.equals("LEFT")) {
                    pmoves.add(4);
                }

            } else {
                if (placeholder.equals("Fire UP")) {
                    pmoves.add(5);
                }
                if (placeholder.equals("Fire RIGHT")) {
                    pmoves.add(6);
                }
                if (placeholder.equals("Fire DOWN")) {
                    pmoves.add(7);
                }
                if (placeholder.equals("Fire LEFT")) {
                    pmoves.add(8);
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
        lbl_Tries = new javax.swing.JLabel();
        btn_Start = new javax.swing.JButton();
        btn_Mleft = new javax.swing.JButton();
        btn_Mright = new javax.swing.JButton();
        btn_Mdown = new javax.swing.JButton();
        btn_Mup = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btn_Fup = new javax.swing.JButton();
        btn_Fleft = new javax.swing.JButton();
        btn_Fright = new javax.swing.JButton();
        btn_Fdown = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btn_clear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lst_moves = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnl_Board.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_Board.setLayout(new java.awt.GridLayout(8, 8));

        lbl_Tries.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbl_Tries.setForeground(new java.awt.Color(255, 51, 51));
        lbl_Tries.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Tries.setText("1");

        btn_Start.setBackground(new java.awt.Color(204, 255, 204));
        btn_Start.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btn_Start.setText("START");
        btn_Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_StartActionPerformed(evt);
            }
        });

        btn_Mleft.setBackground(new java.awt.Color(153, 255, 153));
        btn_Mleft.setText("LEFT");
        btn_Mleft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MleftActionPerformed(evt);
            }
        });

        btn_Mright.setBackground(new java.awt.Color(153, 255, 153));
        btn_Mright.setText("RIGHT");
        btn_Mright.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MrightActionPerformed(evt);
            }
        });

        btn_Mdown.setBackground(new java.awt.Color(153, 255, 153));
        btn_Mdown.setText("DOWN");
        btn_Mdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MdownActionPerformed(evt);
            }
        });

        btn_Mup.setBackground(new java.awt.Color(153, 255, 153));
        btn_Mup.setText("UP");
        btn_Mup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MupActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("MOVE");
        jLabel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("SHOOT");

        btn_Fup.setBackground(new java.awt.Color(255, 153, 153));
        btn_Fup.setText("UP");
        btn_Fup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FupActionPerformed(evt);
            }
        });

        btn_Fleft.setBackground(new java.awt.Color(255, 153, 153));
        btn_Fleft.setText("LEFT");
        btn_Fleft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FleftActionPerformed(evt);
            }
        });

        btn_Fright.setBackground(new java.awt.Color(255, 153, 153));
        btn_Fright.setText("RIGHT");
        btn_Fright.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FrightActionPerformed(evt);
            }
        });

        btn_Fdown.setBackground(new java.awt.Color(255, 153, 153));
        btn_Fdown.setText("DOWN");
        btn_Fdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_FdownActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setText("TRIES :");

        btn_clear.setBackground(new java.awt.Color(153, 153, 255));
        btn_clear.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btn_clear.setText("CLEAR");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        lst_moves.setToolTipText("");
        jScrollPane1.setViewportView(lst_moves);

        javax.swing.GroupLayout pnl_infoLayout = new javax.swing.GroupLayout(pnl_info);
        pnl_info.setLayout(pnl_infoLayout);
        pnl_infoLayout.setHorizontalGroup(
            pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(32, 32, 32))
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_Tries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnl_infoLayout.createSequentialGroup()
                                .addComponent(btn_Mdown)
                                .addGap(1, 1, 1))
                            .addComponent(btn_Mup, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createSequentialGroup()
                                .addComponent(btn_Fdown)
                                .addGap(31, 31, 31))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createSequentialGroup()
                                .addComponent(btn_Fup, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))))
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addComponent(btn_Mleft, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_Mright, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addComponent(btn_Fleft)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Fright)))
                .addContainerGap())
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_infoLayout.setVerticalGroup(
            pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbl_Tries))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_clear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Fup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Mup, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Mleft)
                    .addComponent(btn_Mright)
                    .addComponent(btn_Fleft)
                    .addComponent(btn_Fright))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Mdown)
                    .addComponent(btn_Fdown))
                .addGap(18, 18, 18)
                .addComponent(btn_Start, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_Board, javax.swing.GroupLayout.PREFERRED_SIZE, 852, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_info, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnl_Board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StartActionPerformed
         listpos = 0;
         int totalmove =  lst_moves.getModel().getSize() ;
        
         if(totalmove == 18){
         pmoves.clear();
         generatePlayermove();
         CopyAIMove();
         beginbattle();
         }
         else{
         JOptionPane.showMessageDialog(null, "Please enter 18 mvoes input", "INVALID", JOptionPane.PLAIN_MESSAGE);
         }
        
        
       
    }//GEN-LAST:event_btn_StartActionPerformed

    private void btn_FupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FupActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("Fire UP");
        }   // TODO add your handling code here:
    }//GEN-LAST:event_btn_FupActionPerformed

    private void btn_MupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MupActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("UP");
        }
    }//GEN-LAST:event_btn_MupActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        for (int i = 0; i < totalmove; i++) {
            model.remove(0);
        }

    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_MleftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MleftActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("LEFT");
        }
    }//GEN-LAST:event_btn_MleftActionPerformed

    private void btn_MrightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MrightActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("RIGHT");
        }
    }//GEN-LAST:event_btn_MrightActionPerformed

    private void btn_MdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MdownActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("DOWN");
        }
    }//GEN-LAST:event_btn_MdownActionPerformed

    private void btn_FleftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FleftActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("Fire LEFT");
        }
    }//GEN-LAST:event_btn_FleftActionPerformed

    private void btn_FrightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FrightActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("Fire RIGHT");
        }
    }//GEN-LAST:event_btn_FrightActionPerformed

    private void btn_FdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_FdownActionPerformed
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove < 18) {
            model.addElement("Fire DOWN");
        }
    }//GEN-LAST:event_btn_FdownActionPerformed

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
    private javax.swing.JButton btn_Fdown;
    private javax.swing.JButton btn_Fleft;
    private javax.swing.JButton btn_Fright;
    private javax.swing.JButton btn_Fup;
    private javax.swing.JButton btn_Mdown;
    private javax.swing.JButton btn_Mleft;
    private javax.swing.JButton btn_Mright;
    private javax.swing.JButton btn_Mup;
    private javax.swing.JButton btn_Start;
    private javax.swing.JButton btn_clear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_Tries;
    private javax.swing.JList lst_moves;
    private javax.swing.JPanel pnl_Board;
    private javax.swing.JPanel pnl_info;
    // End of variables declaration//GEN-END:variables
}
