package by.arsy.gameObj;

import by.arsy.adapters.XMLAdapter;
import by.arsy.game.GameWindow;

public class Gamer extends GameObject {

    private final int size;

    public Gamer(int positionY, int positionX, int size) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.size = size;
    }

    public void setPositionX(int positionX) {
        if (positionX >= 0 && positionX < GameWindow.getCOLS() - size + 1) {
            this.positionX = positionX;
        }
    }

    public int getSize() {
        return size;
    }

    public void restart() {
        setPositionX((GameWindow.getCOLS() - XMLAdapter.getStartSizeGamers()) / 2);
    }
}
