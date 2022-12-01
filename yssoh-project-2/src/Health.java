import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

public class Health {

    private final static int ORANGE_BOUNDARY = 65;
    private final static int RED_BOUNDARY = 35;

    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    /**
     * Method that renders the current health as a percentage on screen
     */
    public void renderHealthPoints(int currHealthPoints, int maxHealthPoints, double healthX, double healthY, int fontSize){
        Font FONT = new Font("res/wheaton.otf", fontSize);
        double percentageHP = ((double) currHealthPoints/maxHealthPoints) * 100;
        if (percentageHP > ORANGE_BOUNDARY){
            COLOUR.setBlendColour(GREEN);
        }
        else if (percentageHP <= RED_BOUNDARY){
            COLOUR.setBlendColour(RED);
        }
        else if (percentageHP <= ORANGE_BOUNDARY){
            COLOUR.setBlendColour(ORANGE);
        }

        FONT.drawString(Math.round(percentageHP) + "%", healthX, healthY, COLOUR);
    }
}
