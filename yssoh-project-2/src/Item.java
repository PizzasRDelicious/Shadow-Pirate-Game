import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Item {
    private final int x;
    private final int y;
    private boolean stillExist;
    public Item(int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.stillExist = true;
    }
    public void update() {
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean getStillExist(){
        return stillExist;
    }
    public void updateStillExist(){
        stillExist = false;
    }
    public int getEffects(){
        return 0;
    }
    public String getLog(){return "";}
    public abstract Image getIconImage();
    public abstract Rectangle getBoundingBox();
}
