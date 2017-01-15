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
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import java.util.Timer;

public class TBoard extends javax.swing.JFrame {

    DefaultListModel model = new DefaultListModel();
    private JButton[][] btn = new JButton[10][10];
    private Spots[][] spotz = new Spots[10][10];
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

    //Contstructor that run all function when class is created 
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
        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                btn[y][x] = new JButton();
                pnl_Board.add(btn[y][x]);
                btn[y][x].setBackground(new java.awt.Color(255, 255, 255));
            }
        }
    }

    //Put tank on the board
    private void initializePiece() {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                spotz[y][x] = new Spots();
            }
        }

        spotz[0][0].occupySpot(new TankPiece(2, 0, 0, 2));
        spotz[9][9].occupySpot(new TankPiece(1, 9, 9, 1));

    }

    //Clear out the board and re-put the tank
    private void REinitializePiece() {

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                spotz[y][x].DeletePiece();
            }
        }

        spotz[0][0].occupySpot(new TankPiece(2, 0, 0, 2));
        spotz[9][9].occupySpot(new TankPiece(1, 9, 9, 1));

    }

    //This function find where the tank is located and apply their icon/image
    private void maskingButton() {
        int teams = 0;
        int state = 0;

        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {

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

    //function to reset the whole board    
    private void resetboard() {

        REinitializePiece();
        maskingButton();

    }

    //The whole battle logic when Start button is clicked
    private void beginbattle() {

        P2tankY = spotz[0][0].piece.getY();
        P2tankX = spotz[0][0].piece.getX();

        P1tankY = spotz[9][9].piece.getY();
        P1tankX = spotz[9][9].piece.getX();

        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {

                if ((moves.size() != 0) || (pmoves.size() != 0)) {
                    PlayerTurn();
                    System.out.print("Player");

                    if (playerWin == false) {
                        AITurn();
                    }

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

    //This function handle the movement animation/logic of AI tank
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

    //This function handle the movement animation/logic of Player tank
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
                    btn_Start.setEnabled(false);
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
                    btn_Start.setEnabled(false);
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
                    btn_Start.setEnabled(false);
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
                    btn_Start.setEnabled(false);
                }
                ;
                break;

        }

    }

    //Handle re-applying appropriate image/icon of the tank IF the tank MOVED!
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

    //Handle re-applying appropriate image/icon of the tank IF the tank DID NOT moved!
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

    //This function handle the generation of 18 randomized AI move list
    private void generateAImoves() {

        int randomNum = 0;
        int y = 0;
        int x = 0;
        int lastspot = 0;

        randomNum = getRandomInteger(3, 2);
        aimoves.add(randomNum);
        lastspot = randomNum;

        while ((aimoves.size() < 18)) {

            randomNum = getRandomInteger(10, 1);

            switch (randomNum) {
                case 1:
                    if (lastspot != 3) {
                        aimoves.add(1);
                        lastspot = 1;
                    }
                    ;
                    break;
                case 2:
                    if (lastspot != 4) {
                        aimoves.add(2);
                        lastspot = 2;
                    }
                    ;
                    break;
                case 3:
                    if (lastspot != 1) {
                        aimoves.add(3);
                        lastspot = 3;
                    }
                    ;
                    break;
                case 4:
                    if (lastspot != 2) {
                        aimoves.add(4);
                        lastspot = 4;
                    }
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
        }
    }

    //return random integer
    public static int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    //copy 18 Generated Aimove from Master Copy to reusable Array (to ensure same AI movement for each reset)
    private void CopyAIMove() {
        moves.clear();

        for (int i = 0; i < 18; i++) {
            moves.add(aimoves.get(i));
        }

        for (int i = 0; i < 18; i++) {
            System.out.println("" + moves.get(i));
        }

    }

    //Convert the whole input from the list into command that the tank understand
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
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
        btn_Stop = new javax.swing.JButton();
        btn_newGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(940, 664));

        pnl_Board.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnl_Board.setLayout(new java.awt.GridLayout(10, 10));

        lbl_Tries.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbl_Tries.setForeground(new java.awt.Color(255, 51, 51));
        lbl_Tries.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Tries.setText("1");

        btn_Start.setBackground(new java.awt.Color(204, 255, 204));
        btn_Start.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
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

        btn_Stop.setBackground(new java.awt.Color(255, 102, 102));
        btn_Stop.setText("STOP");
        btn_Stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_StopActionPerformed(evt);
            }
        });

        btn_newGame.setText("NEW GAME");
        btn_newGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_infoLayout = new javax.swing.GroupLayout(pnl_info);
        pnl_info.setLayout(pnl_infoLayout);
        pnl_infoLayout.setHorizontalGroup(
            pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(lbl_Tries, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnl_infoLayout.createSequentialGroup()
                                .addComponent(btn_Stop, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Start, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl_infoLayout.createSequentialGroup()
                                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnl_infoLayout.createSequentialGroup()
                                            .addComponent(btn_Mleft, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(btn_Mright, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnl_infoLayout.createSequentialGroup()
                                            .addGap(33, 33, 33)
                                            .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btn_Mup, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btn_Mdown))))
                                    .addGroup(pnl_infoLayout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(jLabel1)
                                        .addGap(48, 48, 48)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnl_infoLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel2)
                                        .addGap(22, 22, 22))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createSequentialGroup()
                                        .addComponent(btn_Fdown)
                                        .addGap(31, 31, 31))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createSequentialGroup()
                                        .addComponent(btn_Fleft)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_Fright))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_infoLayout.createSequentialGroup()
                                        .addComponent(btn_Fup, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32))))
                            .addComponent(jScrollPane1)
                            .addComponent(btn_newGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnl_infoLayout.setVerticalGroup(
            pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_newGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Tries, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Stop, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear)
                    .addComponent(btn_Start, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Mup)
                    .addComponent(btn_Fup, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Fleft)
                            .addComponent(btn_Fright))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Fdown))
                    .addGroup(pnl_infoLayout.createSequentialGroup()
                        .addGroup(pnl_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Mleft)
                            .addComponent(btn_Mright))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_Mdown)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(pnl_Board, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnl_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
            .addComponent(pnl_Board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void btn_StopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StopActionPerformed
        t.cancel();
        tt.cancel();
        JOptionPane.showMessageDialog(null, "Game stopped", "RETRY", JOptionPane.PLAIN_MESSAGE);
        tries++;
        lbl_Tries.setText("" + tries);
        resetboard();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_StopActionPerformed

    private void btn_newGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newGameActionPerformed
        this.dispose();

        new TBoard().setVisible(true);
    }//GEN-LAST:event_btn_newGameActionPerformed

    //Button Start function that start battling the tank
    private void btn_StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_StartActionPerformed
        listpos = 0;
        int totalmove = lst_moves.getModel().getSize();

        if (totalmove == 18) {
            pmoves.clear();
            generatePlayermove();
            CopyAIMove();
            beginbattle();
        } else {
            JOptionPane.showMessageDialog(null, "Please enter 18 moves input", "INVALID", JOptionPane.PLAIN_MESSAGE);
        }

    }//GEN-LAST:event_btn_StartActionPerformed

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
    private javax.swing.JButton btn_Stop;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_newGame;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_Tries;
    private javax.swing.JList lst_moves;
    private javax.swing.JPanel pnl_Board;
    private javax.swing.JPanel pnl_info;
    // End of variables declaration//GEN-END:variables
}
