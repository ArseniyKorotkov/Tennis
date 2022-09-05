package by.arsy.game;

import by.arsy.adapters.JBDCAdapter;
import by.arsy.adapters.XMLAdapter;
import by.arsy.gameObj.*;

import java.util.ArrayList;

public class TennisRunner {

    static {
        JBDCAdapter.createTable();
    }

    private static final int GAMER_ID = XMLAdapter.getId();

    private static final String GAMER_VISIBLE = XMLAdapter.getGamerVisible();
    private static final String BALL_VISIBLE = XMLAdapter.getBallVisible();
    private static final String SPICE_VISIBLE = XMLAdapter.getSpiceVisible();
    private static final String WALL_VISIBLE = XMLAdapter.getWallVisible();
    private static final String COIN_VISIBLE = XMLAdapter.getCoinVisible();
    private static final int START_SIZE_GAMERS = XMLAdapter.getStartSizeGamers();
    private static int countCoins = 0;
    private static int recordCoins = JBDCAdapter.getBest();

    private static Gamer gamer1;
    private static Gamer gamer2;
    private static Ball ball;
    private volatile static ArrayList<Wall> walls;
    private static boolean gamed = true;
    private static boolean pause = false;

    public static void main(String[] args) {
        gamersInitialization();
        wallsInitialization();
        ballInitialization();

        new GameWindow();
        moviesVisible();
    }

    private static void gamersInitialization() {
        int startPositionX = (GameWindow.getCOLS() - START_SIZE_GAMERS) / 2;
        gamer1 = new Gamer(GameWindow.getROWS() - 1, startPositionX, START_SIZE_GAMERS);
        gamer2 = new Gamer(2, startPositionX, START_SIZE_GAMERS);
    }

    private static void ballInitialization() {
        int positionX = GameWindow.getCOLS() / 2 - 1;
        int positionY = GameWindow.getROWS() - 2;
        ball = new Ball(positionY, positionX);
    }

    private static void wallsInitialization() {
        walls = new ArrayList<>();
    }

    private static void moviesVisible() {
        while (true) {

            synchronized (GameWindow.class) {
                placeVisible();
                gamersVisible(gamer1, gamer2);
                wallsVisible();
                ballVisible();
                coinsVisible();

                if (!isGamed()) {
                    gameOverVisible();
                } else if (isPause()) {
                    pauseVisible();
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static void placeVisible() {
        for (int i = 0; i < GameWindow.getAllPixels(); i++) {
            GameWindow.getLabelsElement(i).setText(SPICE_VISIBLE);
        }

    }

    private static void gamersVisible(Gamer... gamers) {
        for (Gamer gamer : gamers) {
            for (int i = 0; i < gamer.getSize(); i++) {
                int labelIndex = gamer.getPositionY() * GameWindow.getCOLS() + gamer.getPositionX() + i;
                GameWindow.getLabelsElement(labelIndex).setText(GAMER_VISIBLE);
            }
        }
    }

    private static void ballVisible() {
        int ballLabelPosition = countGameObjectLabelPosition(ball);
        GameWindow.getLabelsElement(ballLabelPosition).setText(BALL_VISIBLE);
    }

    private static void wallsVisible() {
        synchronized (getWalls()) {
            walls.forEach(w -> GameWindow.getLabelsElement(w.getPlacePosition()).setText(WALL_VISIBLE));
        }
    }

    private static void coinsVisible() {

        String now = "YOUR";
        for (int q = 0; q < now.length(); q++) {
            GameWindow.getLabelsElement(q).setText(String.valueOf(now.charAt(q)));
        }

        String coins = String.valueOf(countCoins);
        for (int q = 0; q < coins.length(); q++) {
            GameWindow.getLabelsElement(GameWindow.getCOLS() - (coins.length() - q)).setText(String.valueOf(coins.charAt(q)));
        }
        GameWindow.getLabelsElement(GameWindow.getCOLS() - (coins.length() + 1)).setText(COIN_VISIBLE);

        String goal = "BEST";
        for (int q = 0; q < goal.length(); q++) {
            GameWindow.getLabelsElement(countGameObjectLabelPosition(1, q)).setText(String.valueOf(goal.charAt(q)));
        }

        String recCoins = String.valueOf(recordCoins);
        int dollarPosition = countGameObjectLabelPosition(1, GameWindow.getCOLS() - (recCoins.length() + 1));
        GameWindow.getLabelsElement(dollarPosition).setText(COIN_VISIBLE);
        for (int q = 0; q < recCoins.length(); q++) {
            int recCoinsPosition = countGameObjectLabelPosition(1, GameWindow.getCOLS() - (recCoins.length() - q));
            GameWindow.getLabelsElement(recCoinsPosition).setText(String.valueOf(recCoins.charAt(q)));
        }

    }

    private static void gameOverVisible() {
        ArrayList<String> finishTextList = new ArrayList<>();
        finishTextList.add("GAME");
        finishTextList.add("OVER");
        finishTextList.add("$" + countCoins);
        if (recordCoins < countCoins) {
            finishTextList.add("");
            finishTextList.add("IT IS");
            finishTextList.add("NEW");
            finishTextList.add("RECORD");
        }

        int startPositionWriteTextY = GameWindow.getROWS() / 2 - finishTextList.size() / 2;
        int startPositionWriteTextX;
        for (int q = 0; q < finishTextList.size(); q++) {
            String finishWord = finishTextList.get(q);
            startPositionWriteTextX = GameWindow.getCOLS() / 2 - finishWord.length() / 2;
            int labelPosition = countGameObjectLabelPosition(startPositionWriteTextY + q, startPositionWriteTextX);
            for (int i = 0; i < finishTextList.get(q).length(); i++) {
                GameWindow.getLabelsElement(labelPosition + i).setText(String.valueOf(finishWord.charAt(i)));
            }
        }
    }

    public static int countGameObjectLabelPosition(GameObject obj) {
        int objLabelPositionY = (obj.getPositionY() + 1) * GameWindow.getCOLS() - GameWindow.getCOLS();
        return objLabelPositionY + obj.getPositionX();
    }

    public static int countGameObjectLabelPosition(int positionY, int positionX) {
        int objLabelPositionY = (positionY + 1) * GameWindow.getCOLS() - GameWindow.getCOLS();
        return objLabelPositionY + positionX;
    }

    private static void pauseVisible() {
        int startPositionWriteTextY = GameWindow.getROWS() / 2;
        int startPositionWriteTextX;

        String wordPause = "PAUSE";
        startPositionWriteTextX = GameWindow.getCOLS() / 2 - wordPause.length() / 2;
        int labelPosition = countGameObjectLabelPosition(startPositionWriteTextY, startPositionWriteTextX);
        for (int i = 0; i < wordPause.length(); i++) {
            GameWindow.getLabelsElement(labelPosition + i).setText(String.valueOf(wordPause.charAt(i)));
        }

    }

    public static int getGamerId() {
        return GAMER_ID;
    }

    public static Gamer getGamer1() {
        return gamer1;
    }

    public static Gamer getGamer2() {
        return gamer2;
    }

    public static Ball getBall() {
        return ball;
    }

    public static ArrayList<Wall> getWalls() {
        return walls;
    }

    public static boolean isGamed() {
        return gamed;
    }

    public static void setGamed(boolean flag) {
        gamed = flag;
    }

    public static void upCountCoins(int plus) {
        countCoins += plus;
    }

    public static int getCountCoins() {
        return countCoins;
    }

    public static void setCountCoins(int countCoins) {
        TennisRunner.countCoins = countCoins;
    }

    public static int getRecordCoins() {
        return recordCoins;
    }

    public static void setRecordCoins(int recordCoins) {
        TennisRunner.recordCoins = recordCoins;
    }

    public static boolean isPause() {
        return pause;
    }

    public static void setPause(boolean pause) {
        TennisRunner.pause = pause;
    }
}
