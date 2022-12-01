import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Potion extends Item{
    private final static Image POTION = new Image("res/items/potion.png");
    private final static Image POTION_ICON = new Image("res/items/potionIcon.png");
    private final static int CURR_HEALTH_INCREASE = 25;
    private final static String POTION_LOG = "Sailor finds Potion. Sailor’s current health:";

    public Potion(int startX, int startY){
        super(startX,startY);
    }

    @Override
    public void update() {
        if (getStillExist()){
            POTION.drawFromTopLeft(this.getX(), this.getY());
        }
    }
    @Override
    public Image getIconImage(){return POTION_ICON;}
    @Override
    public String getLog(){return POTION_LOG;}

    public Rectangle getBoundingBox() {
        return POTION.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }
    public int getEffects(){
        return CURR_HEALTH_INCREASE;
    }
}
