import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Class represents the projectile on an enemy
 * */
public class Projectile {
    private final static Image BLACKBEARD_PROJECTILE = new Image("res/blackbeard/blackbeardProjectile.png");
    private final static Image PIRATE_PROJECTILE = new Image("res/pirate/pirateProjectile.png");

    private final DrawOptions DRAW_OPTIONS = new DrawOptions();
    private final double PROJECTILE_SPEED;
    private final int PROJECTILE_DAMAGE;
    private double projectileDirectionX;
    private double projectileDirectionY;
    private Point projectilePoint;
    private double projectileRadians;
    private boolean stillExist;
    private final String ENEMY;
    private Point temp;

    private double x;
    private double y;

    /** A constructor for Projectile class
     * @param projectileSpeed The projectile speed of enemy
     * @param projectileDamage The projectile damage of enemy
     * @param sailor The object sailor parsed in
     * @param enemy The boundary box of enemy
     * @param ENEMY The type of enemy (pirate or blackbeard)
     * */
    public Projectile(double projectileSpeed, int projectileDamage, Sailor sailor, Rectangle enemy, String ENEMY){
        this.PROJECTILE_SPEED = projectileSpeed;
        this.PROJECTILE_DAMAGE = projectileDamage;
        this.x = enemy.bottomRight().x;
        this.y = enemy.bottomRight().y;
        this.stillExist = true;
        this.ENEMY = ENEMY;
        setProjectileDirection(enemy,sailor);
    }

    /**
     * Method that updates the projectile's coordinates and draws it based on the enemy that shot it
    * */
    public void update(){
        x += PROJECTILE_SPEED * projectileDirectionX;
        y += PROJECTILE_SPEED * projectileDirectionY;
        projectilePoint = new Point (x,y);
        if (stillExist){
            if (ENEMY.equals("Blackbeard")){
                BLACKBEARD_PROJECTILE.draw(x,y, DRAW_OPTIONS);
            }
            else if (ENEMY.equals("Pirate")){
                PIRATE_PROJECTILE.draw(x,y, DRAW_OPTIONS);
            }
        }
    }

    /**
     * Method that sets the x and y direction the projectile travels
     * @param enemy The hitbox of the enemy
     * @param sailor The object sailor parsed in
     * */
    public void setProjectileDirection(Rectangle enemy, Sailor sailor){

        Point sailorPoint = sailor.getBoundingBox().centre();
        Point enemyPoint = enemy.centre();
        double length = enemyPoint.distanceTo(sailorPoint);
        projectileDirectionX = (sailorPoint.x - enemyPoint.x)/length;
        projectileDirectionY = (sailorPoint.y - enemyPoint.y)/length;
        projectileRadians = Math.atan2(sailorPoint.y - enemyPoint.y, sailorPoint.x- enemyPoint.x);
        DRAW_OPTIONS.setRotation(projectileRadians);
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
            stillExist = false;
        }
    }
    /**
     * Method that checks if projectile collides with sailor and logs it
     * @param sailor The object sailor parsed in
     * */
    public void checkHitSailor(Sailor sailor){
        Rectangle sailorBox = sailor.getBoundingBox();
        if (stillExist &&  sailorBox.intersects(projectilePoint)){
            stillExist = false;
            sailor.setCurrHealthPoints(PROJECTILE_DAMAGE);
            System.out.println(String.format("%s inflicts %d damage points on Sailor. Sailor’s current health: %d/%d",
                    ENEMY ,PROJECTILE_DAMAGE,sailor.getCurrHealthPoints(),sailor.getMaxHealthPoints()));
        }
    }

}
