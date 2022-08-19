package by.arsy.gameObj;

import by.arsy.game.GameWindow;

public class Wall extends GameObject {

    private final int placePosition;

    public Wall(int placePosition) {
        this.placePosition = placePosition;
        this.positionY = placePosition / GameWindow.getCOLS();
        this.positionX = placePosition % GameWindow.getCOLS();
    }

    public int getPlacePosition() {
        return placePosition;
    }
}
