package by.arsy.game;

import by.arsy.adapters.XMLAdapter;
import by.arsy.gameObj.Wall;

import java.util.Random;

import static by.arsy.game.TennisRunner.*;

public class CreateWallThread extends Thread {

    private static final int BUST_AMOUNT_WALLS = XMLAdapter.getBustAmountWalls();
    private static final int GAME_AMOUNT_WALLS = (GameWindow.getCOLS() / 2) * BUST_AMOUNT_WALLS;
    private static final long PAUSE_BUILD_WALL_SPEED = XMLAdapter.getPauseBuildWallSpeed();

    @Override
    public void run() {
        int variantsForWallPosition = GameWindow.getAllPixels() / 3;
        Random randomPosition = new Random();

        OUT:
        while (isGamed()) {

            int wallPosition = variantsForWallPosition + randomPosition.nextInt(variantsForWallPosition);

            synchronized (getWalls()) {
                for (Wall wall : getWalls()) {
                    if (wall.getPlacePosition() == wallPosition) {
                        continue OUT;
                    }
                }
            }


            try {
                Thread.sleep(PAUSE_BUILD_WALL_SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (isPause()) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (getWalls()) {
                if (getWalls().size() < GAME_AMOUNT_WALLS) {
                    getWalls().add(new Wall(wallPosition));
                }
            }
        }
    }
}
