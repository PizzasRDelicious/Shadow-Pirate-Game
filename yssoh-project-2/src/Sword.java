import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sword extends Item{
    private final static Image SWORD = new Image("res/items/sword.png");
    private final static Image SWORD_ICON = new Image("res/items/swordIcon.png");
    private final static int DAMAGE_INCREASE = 15;
    private final static String SWORD_LOG = "Sailor finds Sword. Sailor’s damage points increased to";

    public Sword(int startX, int startY){
        super(startX,startY);
    }

    @Override
    public void update() {
        if (getStillExist()){
            SWORD.drawFromTopLeft(this.getX(), this.getY());
        }
    }
    @Override
    public Image getIconImage(){return SWORD_ICON;}
    @Override
    public String getLog(){return SWORD_LOG;}

    public Rectangle getBoundingBox() {
        return SWORD.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }
    public int getEffects(){
        return DAMAGE_INCREASE;
    }
}