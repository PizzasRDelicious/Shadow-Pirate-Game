import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Parent class represents an Enemy
 */
public class Enemy {
    private final static Random rand = new Random();
    private double x;
    private double y;
    private double oldX;
    private double oldY;
    private double enemySpeed;
    private int moveDirection;
    protected boolean collided;
    protected boolean stillExist;
    protected boolean invincibleState;
    protected boolean directionRight;
    protected boolean cooldownState;

    private final static double MIN_SPEED = 0.2;
    private final static double MAX_SPEED = 0.7;
    protected static int projectileCounter = 0;

    /**
     * The constructor for Enemy class
     * @param startX The initial x coordinate of an enemy
     * @param startY The initial y coordinate of an enemy
     */
    public Enemy(int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.enemySpeed = generateRandomSpeed();
        this.moveDirection = generateRandomDirection();
        this.collided = false;
        this.stillExist = true;
        this.invincibleState = false;
        this.directionRight = true;
        this.cooldownState = false;

    }

    /**
     * Method that updates enemy coordinates and health points
     * @param blocks The block array parsed in to check collision
     * @param timerCounter The world counter used for timed operations
     * @param sailor The sailor object parsed in
     * @param projectiles The projectiles ArrayList
     */
    public void update(StationaryObject[] blocks, int timerCounter, Sailor sailor, ArrayList<Projectile> projectiles){
    }

    /**
     * Method that reduces enemy's health when taking damage and sets invincibility state
     * @param damagePoints The damage point of enemy
     * @param timerCounter The world counter used for timed operations
     */
    public void reduceHealth(int damagePoints, int timerCounter){
    }
    /**
     * Method that checks for collisions between enemy and blocks and bombs
     * @param blocks The blocks array parsed in to check for collision
     */
    protected void checkCollisions(StationaryObject[] blocks) {
    }

    /**
     * Method that gets the x coordinate of enemy
     * @return double The x coordinate of enemy
     */
    public double getX(){return x;}
    /**
     * Method that gets the y coordinate of enemy
     * @return double The y coordinate of enemy
     */
    public double getY(){return y;}

    /**
     * Method that gets if the enemy still exists
     * @return boolean The boolean statement if enemy still exists
     */
    public boolean getStillExist() {return stillExist;}

    /**
     * Method that gets enemy move direction
     * @return int The direction enemy is moving
     */
    public int getMoveDirection() {
        return moveDirection;
    }

    /**
     * Method that gets the speed of enemy
     * @return double The speed of enemy
     */
    public double getSpeed(){
        return enemySpeed;
    }

    /**
     * Method that gets the bounding box of enemy
     * @return Rectangle The bounding box of enemy
     */
    public Rectangle getBoundingBox() {return null;}

    /**
     * Method that generates a random speed for enemy
     * @return double The speed of enemy
     */
    private double generateRandomSpeed(){
        return (MIN_SPEED + (MAX_SPEED - MIN_SPEED) * rand.nextDouble());
    }

    /**
     * Method that generates a random direction for enemy
     * @return in The direction of enemy
     */
    private int generateRandomDirection(){
        return (rand.nextInt(4));
    }

    /**
     * Method that moves the sailor given the direction
     * @param xMove The distance to move in x-axis
     * @param yMove The distance to move in y-axis
     */
    protected void move(double xMove, double yMove) {
        x += xMove;
        y += yMove;
    }

    /**
     * Method that stores the old coordinates of the sailor
     */
    protected void setOldPoints() {
        oldX =x;
        oldY =y;
    }
    /**
     * Method that moves the enemy back to its previous position and changes its direction
     */
    protected void moveBack() {
        x = oldX;
        y = oldY;
        changeDirection();
    }

    /**
     * Method that changes direction of enemy
     */
    protected void changeDirection(){
        if (moveDirection ==0 ){
            moveDirection = 1;
        }
        else if (moveDirection == 1){
            moveDirection = 0;
        }
        else if (moveDirection == 2){
            moveDirection = 3;
        }
        else if (moveDirection == 3){
            moveDirection = 2;
        }
    }

    /**
     * Method that checks if enemy has gone out-of-bound
     * @param leftBoundary The left boundary of the level
     * @param topBoundary The top boundary of the level
     * @param rightBoundary The right boundary of the level
     * @param bottomBoundary The bottom boundary of the level
     */
    public void isOutOfBound(int leftBoundary, int topBoundary, int rightBoundary, int bottomBoundary) {
        if ((y > bottomBoundary) || (y < topBoundary) || (x < leftBoundary) || (x > rightBoundary)) {
            moveBack();
        }
    }

    /**
     * Method that calculates time passed in ms
     * @param timerCounter The world counter used for timed operations
     * @param startTimer The start time
     */
    protected double timePassed(int timerCounter, int startTimer){
        return ((timerCounter - startTimer)/(60.0/1000));
    }

    /**
     * Method that checks if sailor is in the attack range of blackbeard
     * @param sailor The sailor object parsed in
     * @param timerCounter The world timer used for timed operations
     * @param projectiles The projectiles ArrayList used to add projectiles into
     */
    protected void checkInRange(Sailor sailor, int timerCounter, ArrayList<Projectile> projectiles){}
    protected void shoot(Sailor sailor, ArrayList<Projectile> projectiles){}
}
