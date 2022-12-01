import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Treasure extends StationaryObject{
    private final static Image TREASURE = new Image("res/treasure.png");

    public Treasure(int startX, int startY) {
        super(startX, startY);
    }
    /**
     * Method that performs state update
     */
    @Override
    public void update(int timerCounter) {
        TREASURE.drawFromTopLeft(this.getX(), this.getY());
    }

    public Rectangle getBoundingBox() {
        return TREASURE.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }
}

