import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block extends StationaryObject{
    private final static Image BLOCK = new Image("res/block.png");

    public Block(int startX, int startY) {
        super(startX, startY);
    }

    /**
     * Method that performs state update
     */
    @Override
    public void update(int timerCounter) {
        BLOCK.drawFromTopLeft(this.getX(), this.getY());
    }

    @Override
    public Rectangle getBoundingBox() {
        return BLOCK.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }
}
