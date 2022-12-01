import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * Class represents Blackbeard
 * */
public class BlackBeard extends Enemy{
    private final static Image BLACKBEARD_LEFT = new Image("res/blackbeard/blackbeardLeft.png");
    private final static Image BLACKBEARD_RIGHT = new Image("res/blackbeard/blackbeardRight.png");
    private final static Image BLACKBEARD_HIT_LEFT = new Image("res/blackbeard/blackbeardHitLeft.png");
    private final static Image BLACKBEARD_HIT_RIGHT = new Image("res/blackbeard/blackbeardHitRight.png");

    private final static int BLACKBEARD_MAX_HEALTH = 90;
    private final static int INVINCIBLE_TIMER = 1500;
    private final static int COOLDOWN_TIMER = 1500;
    private final static int BLACKBEARD_RANGE = 200;
    private final static int PROJECITLE_DAMAGE = 20;
    private final static double PROJECTILE_SPEED = 0.8;

    private final Health health = new Health();
    private int currHealthPoints;
    private int startInvincibleTimer;
    private int startCooldownTimer;

    private Image currentImage;

    /** A constructor for the BlackBeard class
     * @param startX The initial x coordinate of Blackbeard
     * @param startY The initial y coordinate of Blackbeard
     */
    public BlackBeard(int startX, int startY) {
        super(startX, startY);
        this.currentImage = BLACKBEARD_RIGHT;
        this.currHealthPoints = BLACKBEARD_MAX_HEALTH;
    }

    /** Method that updates blackbeards coordinates and health points
     * @param blocks The blocks array parsed in to check collision
     * @param timerCounter The world timer used for timed operations
     * @param sailor The sailor object parsed in
     * @param projectiles The projectiles ArrayList parsed in to check for sailor taking damage
     */
    @Override
    public void update(StationaryObject[] blocks, int timerCounter, Sailor sailor, ArrayList<Projectile> projectiles) {
        // Move blackbeard based on its random move direction
        if (getMoveDirection() == 0){ //Move up
            setOldPoints();
            move(0, -getSpeed());
        }
        else if (getMoveDirection() == 1){ //Move down
            setOldPoints();
            move(0, getSpeed());
        }
        else if (getMoveDirection() == 2){ //Move left
            setOldPoints();
            move(-getSpeed(), 0);
            directionRight = false;
        }
        else if (getMoveDirection() == 3){ //Move right
            setOldPoints();
            move(getSpeed(), 0);
            directionRight = true;
        }
        // Check blackbeard's invincible state and ensure it remains for 1500ms
        if (invincibleState){
            if (timePassed(timerCounter, startInvincibleTimer) == INVINCIBLE_TIMER){
                invincibleState = false;
            }
            if (directionRight){
                currentImage = BLACKBEARD_HIT_RIGHT;
            }
            else {
                currentImage = BLACKBEARD_HIT_LEFT;
            }
        }
        else {
            if (directionRight){
                currentImage = BLACKBEARD_RIGHT;
            }
            else {
                currentImage = BLACKBEARD_LEFT;
            }
        }
        // Checks if Blackbeard still exists and draws it
        if (stillExist){
            checkInRange(sailor, timerCounter, projectiles);
            if (timePassed(timerCounter, startCooldownTimer) == COOLDOWN_TIMER){
                cooldownState = false;
            }
            currentImage.drawFromTopLeft(getX(), getY());
            checkCollisions(blocks);
            health.renderHealthPoints(currHealthPoints,BLACKBEARD_MAX_HEALTH,getX(), getY()-6, 15);
        }

    }

    /**
     * Method that gets the bounding box of blackbeard
     * @return Rectangle This returns the bounding box of blackbeard
     */
    @Override
    public Rectangle getBoundingBox() {
        return currentImage.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }

    /***
     * Method that reduces blackbeard's health when taking damage and sets invincibility state
     * @param damagePoints The damage point of blackbeard
     * @param timerCounter The world counter used for timed operations
     */
    @Override
    public void reduceHealth(int damagePoints, int timerCounter){
        if (!invincibleState){
            startInvincibleTimer = timerCounter;
            currHealthPoints -= damagePoints;
            invincibleState = true;
            System.out.println(String.format
                    ("Sailor inflicts %d damage points on Blackbeard. Blackbeard's current health: %d/%d",
                            damagePoints, currHealthPoints,BLACKBEARD_MAX_HEALTH));
        }
        if (currHealthPoints<= 0){
            stillExist = false;
        }
    }

    /**
     * Method that checks for collisions between blackbeard and blocks and bombs
     * @param blocks The blocks array parsed in to check for collision
     */
    @Override
    protected void checkCollisions(StationaryObject[] blocks) {
        // check collisions and print log
        Rectangle blackBeardBox = currentImage.getBoundingBoxAt(new Point(getX(), getY()));
        for (StationaryObject current : blocks) {
            if (current != null) {
                Rectangle blockBox = current.getBoundingBox();
                if (blackBeardBox.intersects(blockBox) && current.getStillExist() && !collided) {
                    collided = true;
                    moveBack();
                }
            }
        }
        collided = false; //can only collide with one block at a time (prevents getting stuck)
    }

    /**
     * Method that checks if sailor is in the attack range of blackbeard
     * @param sailor The sailor object parsed in
     * @param timerCounter The world timer used for timed operations
     * @param projectiles The projectiles ArrayList used to add projectiles into
     */
    @Override
    protected void checkInRange(Sailor sailor, int timerCounter, ArrayList<Projectile> projectiles){
        Point blackbeardRangePoint = new Point((getX()+20)-BLACKBEARD_RANGE/2.0, (getY()+29)-BLACKBEARD_RANGE/2.0);
        Rectangle blackbeardRangeBox = new Rectangle(blackbeardRangePoint,200,200);
        Rectangle sailorBox = sailor.getBoundingBox();

        // If in the attack range, creates a projectile and starts the cooldown state
        if (blackbeardRangeBox.intersects(sailorBox) && !cooldownState){
            startCooldownTimer = timerCounter;
            cooldownState = true;
            shoot(sailor, projectiles);
        }
    }

    /**
     * Method that adds a new projectile into projectiles
     * @param sailor The sailor object parsed in
     * @param projectiles The projectiles ArrayList to add a new projectile into
     */
    @Override
    protected void shoot(Sailor sailor, ArrayList<Projectile> projectiles){
         projectiles.add(new Projectile (
                 PROJECTILE_SPEED, PROJECITLE_DAMAGE, sailor, getBoundingBox(), "Blackbeard"));
    }
}