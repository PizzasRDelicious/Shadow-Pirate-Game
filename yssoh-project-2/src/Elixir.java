import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Elixir extends Item{
    private final static Image ELIXIR = new Image("res/items/elixir.png");
    private final static Image ELIXIR_ICON = new Image("res/items/elixirIcon.png");
    private final static int MAX_HEALTH_INCREASE = 35;
    private final static String ELIXIR_LOG = "Sailor finds Elixir. Sailor’s current health:";

    public Elixir(int startX, int startY){
        super(startX,startY);
    }

    @Override
    public void update() {
        if (getStillExist()){
            ELIXIR.drawFromTopLeft(this.getX(), this.getY());
        }
    }
    @Override
    public Image getIconImage(){
        return ELIXIR_ICON;
    }
    @Override
    public String getLog(){return ELIXIR_LOG;}

    public Rectangle getBoundingBox() {
        return ELIXIR.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }
    public int getEffects(){
        return MAX_HEALTH_INCREASE;
    }
}