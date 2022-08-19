package by.arsy.game;

import by.arsy.adapters.XMLAdapter;

import static by.arsy.game.TennisRunner.getBall;
import static by.arsy.game.TennisRunner.isGamed;

public class SpeedBallThread extends Thread {

    private static final long BUST_BALL_SPEED = XMLAdapter.getBustBallSpeed();
    private static final long TIMER_SPEED_UP = XMLAdapter.getTimerSpeedUp();

    @Override
    public void run() {

        while (isGamed()) {
            if (getBall().getPauseForStepBall() > BUST_BALL_SPEED) {
                getBall().setPauseForStepBall(getBall().getPauseForStepBall() - BUST_BALL_SPEED);
            }
            try {
                Thread.sleep(TIMER_SPEED_UP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
