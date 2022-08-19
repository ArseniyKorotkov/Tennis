package by.arsy.game;

import by.arsy.adapters.XMLAdapter;
import by.arsy.gameObj.Gamer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static by.arsy.game.TennisRunner.*;

public class GameWindow extends JFrame implements KeyListener {

    private static final ArrayList<JLabel> LABELS;
    private static final int ROWS = XMLAdapter.getRows();
    private static final int COLS = XMLAdapter.getCols();
    private static final int KEY_KODE_LEFT_G1 = XMLAdapter.getKeyKodeLeftG1();
    private static final int KEY_KODE_RIGHT_G1 = XMLAdapter.getKeyKodeRightG1();
    private static final int KEY_KODE_LEFT_G2 = XMLAdapter.getKeyKodeLeftG2();
    private static final int KEY_KODE_RIGHT_G2 = XMLAdapter.getKeyKodeRightG2();

    private static final int ALL_PIXELS = ROWS * COLS;

    static {
        LABELS = new ArrayList<>();

        for (int i = 0; i < ALL_PIXELS; i++) {
            LABELS.add(new JLabel());
        }
    }

    protected GameWindow() {
        super("Tennis");
        this.setBounds(100, 100, COLS * 25, ROWS * 20);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addKeyListener(this);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(ROWS, COLS, 2, 2));

        for (int i = 0; i < ALL_PIXELS; i++) {
            container.add(LABELS.get(i));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(isGamed()) {
            reactionGamer(getGamer1(), e, KEY_KODE_LEFT_G1, KEY_KODE_RIGHT_G1);
            reactionGamer(getGamer2(), e, KEY_KODE_LEFT_G2, KEY_KODE_RIGHT_G2);
            reactionForStartGame(e);
        }
        restart(e);
        pause(e);

        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void reactionGamer(Gamer gamer, KeyEvent e, int keyCodeLeft, int keyCodeRight) {
            if (e.getKeyCode() == keyCodeLeft) {
                gamer.setPositionX(gamer.getPositionX() - 1);
                TennisRunner.setPause(false);
            } else if (e.getKeyCode() == keyCodeRight) {
                gamer.setPositionX(gamer.getPositionX() + 1);
                TennisRunner.setPause(false);
        }
    }

    private void reactionForStartGame(KeyEvent e) {
        if (!getBall().isMoved() && (e.getKeyCode() == KEY_KODE_LEFT_G1 || e.getKeyCode() == KEY_KODE_RIGHT_G1)) {
            getBall().setMoved(true);

            new SpeedBallThread().start();
            new CreateWallThread().start();
            new BallThread(e.getKeyCode()).start();
        }
    }

    private void restart(KeyEvent e) {
        if (e.getKeyCode() == 82) {
            if (!TennisRunner.isGamed()) {
                TennisRunner.setGamed(true);
                if (TennisRunner.getCountCoins() > TennisRunner.getRecordCoins()) {
                    TennisRunner.setRecordCoins(TennisRunner.getCountCoins());
                }
                TennisRunner.setCountCoins(0);
            }
            TennisRunner.getBall().restart();
            TennisRunner.getGamer1().restart();
            TennisRunner.getGamer2().restart();
            TennisRunner.getWalls().clear();
        }
    }

    private void pause(KeyEvent e) {
        if (e.getKeyCode() == 80) {
            TennisRunner.setPause(!TennisRunner.isPause());
        }
    }

    public static JLabel getLabelsElement(int labelIndex) {
        return LABELS.get(labelIndex);
    }

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLS() {
        return COLS;
    }

    public static int getAllPixels() {
        return ALL_PIXELS;
    }

    public static int getKeyKodeLeftG1() {
        return KEY_KODE_LEFT_G1;
    }

    public static int getKeyKodeRightG1() {
        return KEY_KODE_RIGHT_G1;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
