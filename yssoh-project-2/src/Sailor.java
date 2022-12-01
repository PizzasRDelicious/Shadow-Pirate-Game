import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Class represents a Sailor
 */
public class Sailor {
    private final static Image SAILOR_LEFT = new Image("res/sailor/sailorLeft.png");
    private final static Image SAILOR_RIGHT = new Image("res/sailor/sailorRight.png");
    private final static Image SAILOR_HIT_LEFT = new Image("res/sailor/sailorHitLeft.png");
    private final static Image SAILOR_HIT_RIGHT = new Image("res/sailor/sailorHitRight.png");
    private final static int MOVE_SIZE = 1; // CHANGE IT BACK TO 1
    private static int maxHealthPoints = 100;
    private static int damagePoints = 15;

    private final static String BOMB_LOG = "Bomb inflicts 10 damage points on Sailor. Sailor's current health:";

    private final static int LADDER_X = 990;
    private final static int LADDER_Y = 630;

    private final static double HEALTH_X = 10;
    private final static double HEALTH_Y = 25;
    private final static int FONT_SIZE = 30;


    private final static int ICON_X = 10;
    private final static int ICON_Y = 40;
    private final static int ICON_Y_INCREMENT = 40;

    private int currHealthPoints;
    private int oldX;
    private int oldY;
    private int x;
    private int y;
    private boolean attack_state;
    private boolean idle_state;
    private static int startAttackTimer;
    private static int startIdleTimer;
    private boolean directionRight;
    private Image currentImage;
    private final static Health health = new Health();

    private static final int MAX_ITEMS = 3;
    private final static Item[] inventory = new Item[MAX_ITEMS];
    private static int inventory_counter = 0;

    /**
     * The constructor for Sailor class
     * @param startX The initial x coordinate of a sailor
     * @param startY The initial y coordinate of a sailor
     */
    public Sailor(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.attack_state = false;
        this.idle_state = false;
        this.directionRight = true;
        this.currHealthPoints = maxHealthPoints;
        this.currentImage = SAILOR_RIGHT;

    }

    /**
     * Method that updates sailor coordinates and state
     * @param input The keyboard input parsed
     * @param blocks The block array parsed in to check collision
     * @param timerCounter The world counter used for timed operations
     * @param pirates The pirates array parsed in
     * @param blackBeard The blackbeard object parsed in
     */
    public void update(Input input, StationaryObject[] blocks, int timerCounter,
                       Pirate[] pirates, BlackBeard blackBeard) {
        // store old coordinates every time the sailor moves
        if (input.isDown(Keys.UP)) {
            setOldPoints();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)) {
            setOldPoints();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)) {
            setOldPoints();
            move(-MOVE_SIZE, 0);
            directionRight = false;
        } else if (input.isDown(Keys.RIGHT)) {
            setOldPoints();
            move(MOVE_SIZE, 0);
            directionRight = true;
        }
        if (input.wasPressed(Keys.S) && !attack_state && !idle_state) {
            updateAttackState(timerCounter);
        }
        if (attack_state) {
            if (timePassed(timerCounter, startAttackTimer) == 1000) {
                attack_state = false;
                updateIdleState(timerCounter);
            }
            if (directionRight) {
                currentImage = SAILOR_HIT_RIGHT;
            } else {
                currentImage = SAILOR_HIT_LEFT;
            }
        } else {
            if (directionRight) {
                currentImage = SAILOR_RIGHT;
            } else {
                currentImage = SAILOR_LEFT;
            }
        }
        if (idle_state) {
            if (timePassed(timerCounter, startIdleTimer) == 2000) {
                idle_state = false;
            }
        }

        currentImage.drawFromTopLeft(x, y);
        checkCollisions(blocks, timerCounter);
        checkAttackingEnemy(pirates, blackBeard, timerCounter);

        health.renderHealthPoints(currHealthPoints,maxHealthPoints,HEALTH_X,HEALTH_Y,FONT_SIZE);
    }

    /**
     * Method that checks for collisions between sailor and blocks and bombs
     */
    private void checkCollisions(StationaryObject[] blocks, int timerCounter) {
        // check collisions and print log
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (StationaryObject current : blocks) {
            if (current != null) {
                Rectangle blockBox = current.getBoundingBox();

                if (sailorBox.intersects(blockBox) && current.getStillExist()) {
                    if (current.getClass().getSimpleName().equals("Bomb") && !current.getDamageTaken()) {
                        current.updateExploded(timerCounter);
                        currHealthPoints = currHealthPoints - current.getDAMAGE_POINTS();
                        System.out.println(String.format("%s %d/%d", BOMB_LOG ,currHealthPoints,maxHealthPoints));
                        current.updateDamageTaken();
                    }
                    moveBack();
                }
            }
        }
    }

    public void checkCollisionsItem(Item[] items) {
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Item item : items) {
                Rectangle itemBox = item.getBoundingBox();
                if (item.getStillExist() && sailorBox.intersects(itemBox)) {
                    if (item.getClass().getSimpleName().equals("Potion")) {
                        currHealthPoints += item.getEffects();
                        if (currHealthPoints >= maxHealthPoints){
                            currHealthPoints = maxHealthPoints;
                        }
                        System.out.println(String.format("%s %d/%d", item.getLog(), currHealthPoints,maxHealthPoints));
                    } else if (item.getClass().getSimpleName().equals("Elixir")) {
                        maxHealthPoints += item.getEffects();
                        currHealthPoints = maxHealthPoints;
                        System.out.println(String.format("%s %d/%d", item.getLog(), currHealthPoints,maxHealthPoints));

                    } else if (item.getClass().getSimpleName().equals("Sword")) {
                        damagePoints += item.getEffects();
                        System.out.println(String.format("%s %d", item.getLog(), damagePoints));
                    }
                    inventory[inventory_counter] = item;
                    inventory_counter++;
                    item.updateStillExist();
                }
            }
        updateInventory(inventory);
        }

    /**
     * Method that moves the sailor given the direction
     */
    private void move(int xMove, int yMove) {
        x += xMove;
        y += yMove;
    }

    /**
     * Method that stores the old coordinates of the sailor
     */
    private void setOldPoints() {
        oldX = x;
        oldY = y;
    }

    /**
     * Method that moves the sailor back to its previous position
     */
    private void moveBack() {
        x = oldX;
        y = oldY;
    }

    /**
     * Method that starts the attack timer
    **/
    private void updateAttackState(int timerCounter){
        attack_state = true;
        startAttackTimer = timerCounter;
    }
    private void updateIdleState(int timerCounter){
        idle_state = true;
        startIdleTimer = timerCounter;
    }

    /**
     * Method that checks if sailor's health is <= 0
     */
    public boolean isDead() {return currHealthPoints <= 0;}

    /**
     * Method that checks if sailor has reached the ladder
     */
    public boolean hasReachLadder(){
        return (x >= LADDER_X) && (y > LADDER_Y);
    }

    /**
     * Method that checks if sailor has intersected with the treasure
     */
    public boolean checkTreasure(Treasure treasure){
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        Rectangle treasureBox = treasure.getBoundingBox();
        if (sailorBox.intersects(treasureBox)){
            return true;
        }
        return false;
    }



    /**
     * Method that checks if sailor has gone out-of-bound
     */
    public void isOutOfBound(int leftBoundary, int topBoundary, int rightBoundary, int bottomBoundary) {
        if ((y > bottomBoundary) || (y < topBoundary) || (x < leftBoundary) || (x > rightBoundary)) {
            moveBack();
        }
    }

    /**
     * Method that calculates time passed in ms
     */
    private double timePassed(int timerCounter, int startTimer){
        return ((timerCounter - startTimer)/(60.0/1000));
    }

    /**
     * Method that displays inventory
     */
    private void updateInventory(Item[] inventory){
        int display_counter = 0;
        for (Item item: inventory){
            if (item != null){
                item.getIconImage().drawFromTopLeft(ICON_X,ICON_Y + ICON_Y_INCREMENT * display_counter);
                display_counter++;
            }
        }
    }
    /**
     * Method that checks for collisions between sailor and blocks and bombs
     */
    private void checkAttackingEnemy(Pirate[] pirates, BlackBeard blackBeard, int timerCounter) {
        // check collisions and print log
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(x, y));
        for (Pirate pirate : pirates) {
            if (pirate != null) {
                Rectangle pirateBox = pirate.getBoundingBox();
                if (sailorBox.intersects(pirateBox) && pirate.getStillExist() && attack_state) {
                    pirate.reduceHealth(damagePoints, timerCounter);

                    }

                }
            }
        if (blackBeard != null){
            Rectangle blackbeardBox = blackBeard.getBoundingBox();
            if (sailorBox.intersects(blackbeardBox) && blackBeard.getStillExist() && attack_state) {
                blackBeard.reduceHealth(damagePoints, timerCounter);
            }
        }
    }

    public Rectangle getBoundingBox() {
        return currentImage.getBoundingBoxAt(new Point(x+20,y+29));
    }

    public void setCurrHealthPoints(int damagePoints) {
        this.currHealthPoints -= damagePoints;
    }
    public void resetCurrHealth(){
        this.currHealthPoints = maxHealthPoints;
    }
    public int getCurrHealthPoints(){
        return currHealthPoints;
    }
    public int getMaxHealthPoints(){
        return maxHealthPoints;
    }
}