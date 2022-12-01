import bagel.util.Rectangle;

public abstract class StationaryObject {
    private final int x;
    private final int y;
    public StationaryObject(int startX, int startY){
        this.x = startX;
        this.y = startY;
    }
    public void update(int timerCounter) {
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void updateExploded(int TimerCounter){
    }
    public boolean getStillExist(){
        return true;
    }
    public int getDAMAGE_POINTS(){
        return 0;
    }
    public boolean getDamageTaken(){
        return true;
    }
    public void updateDamageTaken(){
    }
    public abstract Rectangle getBoundingBox();

}
