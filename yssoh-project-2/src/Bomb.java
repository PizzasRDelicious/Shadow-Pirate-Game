import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Bomb extends StationaryObject{
    private final static Image BOMB = new Image("res/bomb.png");
    private final static Image EXPLODED = new Image("res/explosion.png");
    private boolean exploded = false;
    private boolean stillExists = true;
    private boolean damageTaken = false;
    private final static int DAMAGE_POINTS = 10;
    private int startTimer;

    public Bomb(int startX, int startY) {
        super(startX,startY);
    }

    /**
     * Method that performs state update
     */
    @Override
    public void update(int timerCounter) {
        // Checks if bomb has exploded and still exists
        if (!exploded && stillExists){
            BOMB.drawFromTopLeft(this.getX(), this.getY());
        }
        else if (exploded && stillExists){
            EXPLODED.drawFromTopLeft(this.getX(),this.getY());
        }
        // Timer ensures that the bomb explodes for 500ms before it stops existing
        if (startTimer != 0){
            if (timePassed(timerCounter) == 500.0){
                stillExists = false;
            }
        }
    }

    @Override
    public Rectangle getBoundingBox() {
        return BOMB.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }

    @Override
    public boolean getStillExist(){
        return stillExists;
    }

    @Override
    public int getDAMAGE_POINTS(){
        return DAMAGE_POINTS;
    }

    @Override
    public boolean getDamageTaken(){
        return damageTaken;
    }

    @Override
    public void updateDamageTaken(){
        damageTaken = true;
    }

    @Override
    public void updateExploded(int TimerCounter){
        exploded = true;
        if (this.startTimer == 0){
            this.startTimer = TimerCounter;
        }
    }

    /**
     * Method that calculates time passed in ms
     */
    private double timePassed(int timerCounter){
        return ((timerCounter - startTimer)/(60.0/1000));
    }
}
