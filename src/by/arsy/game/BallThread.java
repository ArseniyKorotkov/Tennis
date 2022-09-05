package by.arsy.game;

import by.arsy.adapters.JBDCAdapter;

import static by.arsy.game.TennisRunner.*;

public class BallThread extends Thread {

    private final int keyCode;

    public BallThread(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public void run() {

        getBall().isStartBall(keyCode);

        try {
            Thread.sleep(getBall().getPauseForStepBall());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (isGamed()) {

            while (isPause()) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            TennisRunner.getBall().moveBall();
            try {
                Thread.sleep(TennisRunner.getBall().getPauseForStepBall());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (TennisRunner.getCountCoins() > TennisRunner.getRecordCoins()) {
            JBDCAdapter.updateBest(TennisRunner.getCountCoins());
        }
        TennisRunner.getBall().setMoved(false);
    }
}
