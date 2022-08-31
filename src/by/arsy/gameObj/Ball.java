package by.arsy.gameObj;

import by.arsy.adapters.XMLAdapter;
import by.arsy.game.GameWindow;
import by.arsy.game.TennisRunner;

import static by.arsy.game.TennisRunner.getBall;
import static by.arsy.game.TennisRunner.getWalls;

public class Ball extends GameObject {

    private final int startOldPosition = -1;
    private int oldPositionX = startOldPosition;
    private int oldPositionY = startOldPosition;
    private long pauseForStepBall = XMLAdapter.getPauseForStepBall();
    private boolean moved = false;

    public Ball(int positionY, int positionX) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void isStartBall(int keyCode) {
        if (oldPositionX == startOldPosition && oldPositionY == startOldPosition) {
            oldPositionX = positionX;
            oldPositionY = positionY;
            positionY--;
            if (keyCode == GameWindow.getKeyKodeLeftG1()) {
                positionX--;
            } else if (keyCode == GameWindow.getKeyKodeRightG1()) {
                positionX++;
            }
        }
    }

    public void moveBall() {
        boolean isBallReadyAction = false;

        while (!isBallReadyAction) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isBallReadyAction = true;
            int frontPositionY = positionY + (positionY - oldPositionY);
            int frontPositionX = positionX + (positionX - oldPositionX);

            if (frontPositionX == -1 || frontPositionX == GameWindow.getCOLS()) {
                invertX();
                isBallReadyAction = false;
                continue;
            }

            Boolean isSpaceVertical = isSpaceInit(positionY, frontPositionX);
            Boolean isSpaceHorizontal = isSpaceInit(frontPositionY, positionX);
            Boolean isSpaceDiagonal = null;

            if (isSpaceVertical != null && isSpaceHorizontal != null) {
                if (isSpaceVertical && isSpaceHorizontal) {
                    isSpaceDiagonal = isSpaceInit(frontPositionY, frontPositionX);
                }
            } else {
                break;
            }


            if (!isSpaceVertical) {
                invertX();
                isBallReadyAction = false;
            }
            if (!isSpaceHorizontal) {
                invertY();
                isBallReadyAction = false;
            }
            if (isSpaceVertical && isSpaceHorizontal && Boolean.FALSE.equals(isSpaceDiagonal)) {
                invertY();
                invertX();
                isBallReadyAction = false;
            }
        }

        ballAction();
        checkGamed();
    }

    public void restart() {
        setMoved(false);
        positionX = GameWindow.getCOLS() / 2 - 1;
        positionY = GameWindow.getROWS() - 2;
        pauseForStepBall = XMLAdapter.getPauseForStepBall();
        oldPositionX = startOldPosition;
        oldPositionY = startOldPosition;
    }

    private void ballAction() {

        if (positionY - oldPositionY == -1) {
            oldPositionY = positionY;
            positionY--;
        } else if (positionY - oldPositionY == 1) {
            oldPositionY = positionY;
            positionY++;
        }

        if (positionX - oldPositionX == -1) {
            oldPositionX = positionX;
            positionX--;
        } else if (positionX - oldPositionX == 1) {
            oldPositionX = positionX;
            positionX++;
        }
        TennisRunner.upCountCoins(1);
    }

    private void invertY() {
        if (positionY - oldPositionY == -1) {
            oldPositionY = positionY - 1;
        } else if (positionY - oldPositionY == 1) {
            oldPositionY = positionY + 1;
        }
    }

    private void invertX() {
        if (positionX - oldPositionX == -1) {
            oldPositionX = positionX - 1;
        } else if (positionX - oldPositionX == 1) {
            oldPositionX = positionX + 1;
        }
    }

    private Boolean isSpaceInit(int positionY, int positionX) {
        synchronized (GameWindow.class) {
            try {
                int labelPosition = TennisRunner.countGameObjectLabelPosition(positionY, positionX);
                return !isWall(labelPosition) && !isGamer(labelPosition);
            } catch (IndexOutOfBoundsException e) {
                TennisRunner.setGamed(false);
                return null;
            }
        }
    }

    private void checkGamed() {
        if (getBall().getPositionY() == TennisRunner.getGamer1().positionY
            || getBall().getPositionY() == TennisRunner.getGamer2().positionY) {
            TennisRunner.setGamed(false);
        }
    }


    private boolean isWall(int labelPosition) {
        synchronized (getWalls()) {
        for (int q = 0; q < TennisRunner.getWalls().size(); q++) {
            if (TennisRunner.getWalls().get(q) != null &&
                    TennisRunner.getWalls().get(q).getPlacePosition() == labelPosition) {
                //delete if is the wall
                    TennisRunner.getWalls().remove(q);
                    TennisRunner.upCountCoins(10);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGamer(int labelPosition) {
        int positionYg1 = TennisRunner.getGamer1().getPositionY();
        int positionXg1 = TennisRunner.getGamer1().getPositionX();
        int gamerPosition1 = TennisRunner.countGameObjectLabelPosition(positionYg1, positionXg1);

        int positionYg2 = TennisRunner.getGamer2().getPositionY();
        int positionXg2 = TennisRunner.getGamer2().getPositionX();
        int gamerPosition2 = TennisRunner.countGameObjectLabelPosition(positionYg2, positionXg2);

        for (int i = 0; i < XMLAdapter.getStartSizeGamers(); i++) {
            if ((gamerPosition1 + i) == labelPosition ||
                (gamerPosition2 + i) == labelPosition) {
                return true;
            }
        }
        return false;
    }


    public long getPauseForStepBall() {
        return pauseForStepBall;
    }

    public void setPauseForStepBall(long pauseForStepBall) {
        this.pauseForStepBall = pauseForStepBall;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
