
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
/**
 * Parent class represents a Pirate
 */
public class Pirate extends Enemy{
    private final static Image PIRATE_LEFT = new Image("res/pirate/pirateLeft.png");
    private final static Image PIRATE_RIGHT = new Image("res/pirate/pirateRight.png");
    private final static Image PIRATE_HIT_LEFT = new Image("res/pirate/pirateHitLeft.png");
    private final static Image PIRATE_HIT_RIGHT = new Image("res/pirate/pirateHitRight.png");

    private final static int PIRATE_MAX_HEALTH = 45;
    private final static int INVINCIBLE_TIMER = 1500;
    private final static int COOLDOWN_TIMER = 3000;
    private final static int PIRATE_RANGE = 100;
    private final static int PROJECITLE_DAMAGE = 10;
    private final static double PROJECTILE_SPEED = 0.4;


    private final Health health = new Health();
    private int currHealthPoints;
    private int startInvincibleTimer;
    private int startCooldownTimer;

    private Image currentImage;

    /**
     * The constructor for Pirate class
     * @param startX The initial x coordinate of a pirate
     * @param startY The initial y coordinate of a pirate
     */
    public Pirate(int startX, int startY) {
        super(startX, startY);
        this.currentImage = PIRATE_RIGHT;
        this.currHealthPoints = PIRATE_MAX_HEALTH;
    }
    /**
     * Method that updates pirate coordinates and health points
     * @param blocks The block array parsed in to check collision
     * @param timerCounter The world counter used for timed operations
     * @param sailor The sailor object parsed in
     * @param projectiles The projectiles ArrayList
     */
    @Override
    public void update(StationaryObject[] blocks, int timerCounter, Sailor sailor, ArrayList<Projectile> projectiles) {
        // Move pirate based on its random move direction
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

        // Check pirate's invincible state and ensure it remains for 1500ms
        if (invincibleState){
            if (timePassed(timerCounter, startInvincibleTimer) == INVINCIBLE_TIMER){
                invincibleState = false;
            }
            if (directionRight){
                currentImage = PIRATE_HIT_RIGHT;
            }
            else {
                currentImage = PIRATE_HIT_LEFT;
            }
        }
        else {
            if (directionRight){
                currentImage = PIRATE_RIGHT;
            }
            else {
                currentImage = PIRATE_LEFT;
            }
        }

        if (stillExist){
            checkInRange(sailor, timerCounter, projectiles);
            if (timePassed(timerCounter, startCooldownTimer) == COOLDOWN_TIMER){
                cooldownState = false;
            }

            currentImage.drawFromTopLeft(getX(), getY());
            checkCollisions(blocks);
            health.renderHealthPoints(currHealthPoints,PIRATE_MAX_HEALTH,getX(), getY()-6, 15);
        }
    }
    /**
     * Method that gets the bounding box of enemy
     * @return Rectangle The bounding box of enemy
     */
    public Rectangle getBoundingBox() {
        return currentImage.getBoundingBoxAt(new Point(this.getX(), this.getY()));
    }

    /**
     * Method that reduces enemy's health when taking damage and sets invincibility state
     * @param damagePoints The damage point of enemy
     * @param timerCounter The world counter used for timed operations
     */
    @Override
    public void reduceHealth(int damagePoints, int timerCounter){
        if (!invincibleState){
            startInvincibleTimer = timerCounter;
            currHealthPoints -= damagePoints;
            invincibleState = true;
            System.out.println(String.format
                    ("Sailor inflicts %d damage points on Pirate. Pirate's current health: %d/%d",
                            damagePoints, currHealthPoints,PIRATE_MAX_HEALTH));
        }
        if (currHealthPoints<= 0){
            stillExist = false;
        }
    }

    /**
     * Method that checks for collisions between enemy and blocks and bombs
     * @param blocks The blocks array parsed in to check for collision
     */
    @Override
    protected void checkCollisions(StationaryObject[] blocks) {
        // check collisions and print log
        Rectangle sailorBox = currentImage.getBoundingBoxAt(new Point(getX(), getY()));
        for (StationaryObject current : blocks) {
            if (current != null) {
                Rectangle blockBox = current.getBoundingBox();
                if (sailorBox.intersects(blockBox) && current.getStillExist() && !collided) {
                    collided = true;
                    moveBack();
                }
            }
        }
        collided = false; //can only collide with one block at a time (prevents getting stuck)
    }

    @Override
    protected void checkInRange(Sailor sailor, int timerCounter, ArrayList<Projectile> projectiles){
        Point pirateRangePoint = new Point((getX()+20)-PIRATE_RANGE/2.0, (getY()+29)-PIRATE_RANGE/2.0);
        Rectangle pirateRangeBox = new Rectangle(pirateRangePoint,100,100);
        Rectangle sailorBox = sailor.getBoundingBox();

        if (pirateRangeBox.intersects(sailorBox) && !cooldownState){
            startCooldownTimer = timerCounter;
            cooldownState = true;
            shoot(sailor, projectiles);
        }

    }
    @Override
    protected void shoot(Sailor sailor, ArrayList<Projectile> projectiles){
        projectiles.add(new Projectile (
                PROJECTILE_SPEED, PROJECITLE_DAMAGE, sailor, getBoundingBox(), "Pirate"));
        projectileCounter++;
    }
}
