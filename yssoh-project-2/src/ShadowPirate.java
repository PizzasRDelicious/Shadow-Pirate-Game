import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2022
 *
 * Please fill your name below
 * @author Soh Yong Qi
 */
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final Image BACKGROUND_IMAGE_0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_1 = new Image("res/background1.png");
    private final static String WORLD_FILE_0 = "res/level0.csv";
    private final static String WORLD_FILE_1 = "res/level1.csv";
    private static int currLevel = 0;

    private boolean gameOn;
    private boolean gameEnd;
    private boolean gameWin;
    private boolean next_level;
    private static int nextLevelCounter= 0;
    private static int timerCounter = 0;
//    private boolean instructionLvl1;

    private final static String START_MESSAGE = "PRESS SPACE TO START";
    private final static String INSTRUCTION_MESSAGE = "PRESS S TO ATTACK";
    private final static String INSTRUCTION_0 = "USE ARROW KEYS TO FIND LADDER";
    private final static String INSTRUCTION_1 = "FIND THE TREASURE";

    private final static String END_MESSAGE = "GAME OVER";
    private final static String LEVEL_COMPLETE = "LEVEL COMPLETE!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";


    private final static int INSTRUCTION_OFFSET = 70;
    private final static int FONT_SIZE = 55;
    private final static int FONT_Y_POS = 402;
    private final Font FONT = new Font("res/wheaton.otf", FONT_SIZE);

    private static int leftBoundary;
    private static int topBoundary;
    private static int rightBoundary;
    private static int bottomBoundary;

    private final static int MAX_ARRAY_SIZE = 49;
    private final static Block[] blocks = new Block[MAX_ARRAY_SIZE];
    private final static Bomb[] bombs = new Bomb[MAX_ARRAY_SIZE];
    private final static Pirate[] pirates = new Pirate[MAX_ARRAY_SIZE];
    private final static Item[] items = new Item[3];
    private final ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private Sailor sailor;
    private Treasure treasure;
    private BlackBeard blackbeard;

    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        gameOn = true;
        gameEnd = false;
        gameWin = false;
        next_level = false;

        if (currLevel == 0){
            readCSV(WORLD_FILE_0);
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            String line;
            if ((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                if (sections[0].equals("Sailor")){
                    sailor = new Sailor(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }

            }

            int current = 0;
            int current_pirate = 0;
            int current_item = 0;
            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                if (sections[0].equals("Block")){
                    if (currLevel == 0){
                        blocks[current] = new Block(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        current++;
                    }
                    else if (currLevel == 1){
                        bombs[current] = new Bomb(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        current++;
                    }
                }
                else if (sections[0].equals("TopLeft")){
                    leftBoundary = Integer.parseInt(sections[1]);
                    topBoundary = Integer.parseInt(sections[2]);
                }
                else if (sections[0].equals("BottomRight")){
                    rightBoundary = Integer.parseInt(sections[1]);
                    bottomBoundary = Integer.parseInt(sections[2]);
                }
                else if (sections[0].equals("Treasure")){
                    treasure = new Treasure(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
                else if (sections[0].equals("Pirate")){
                    pirates[current_pirate] = new Pirate(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    current_pirate++;
                }
                else if (sections[0].equals("Blackbeard")){
                    blackbeard = new BlackBeard(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }

                // Reading and creating Items
                if (sections[0].equals("Potion")){
                    items[current_item] = new Potion(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    current_item++;
                }
                else if (sections[0].equals("Elixir")){
                    items[current_item] = new Elixir(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    current_item++;
                }else if (sections[0].equals("Sword")){
                    items[current_item] = new Sword(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    current_item++;
                }

//                    ;
//
//                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {

        // Checks if it's in an instruction screen (gameOn)
        if(gameOn && !next_level && !gameEnd && !gameWin){
            drawInstructionScreen(input, currLevel);
        }
        else if (gameOn && next_level && !gameEnd&& !gameWin){
            drawLevelComplete();

            if  (nextLevelCounter / (60.0/1000)> 3000) {
                currLevel = 1;
                next_level = false;
                readCSV(WORLD_FILE_1);
            }
            nextLevelCounter++;
        }

        // Level 0
        if (currLevel == 0 && !gameOn && !gameEnd && !gameWin){
            BACKGROUND_IMAGE_0.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            for (Block block : blocks) {
                block.update(timerCounter);
            }
            for (Pirate pirate : pirates){
                if (pirate != null){
                    pirate.update(blocks, timerCounter, sailor, projectiles);
                    pirate.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);
                }
            }
            // Testing here
            for (Projectile projectile : projectiles){
                if (projectile != null){
                    projectile.update();
                    projectile.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);
                    projectile.checkHitSailor(sailor);
                }
            }

            sailor.update(input, blocks, timerCounter, pirates, blackbeard);
            sailor.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);



            if (sailor.hasReachLadder()){
                next_level = true;
                gameOn = true;
            }
        }

        // Level 1
        else if (currLevel == 1 && !gameOn && !gameEnd && !gameWin){

            BACKGROUND_IMAGE_1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            for (Bomb bomb : bombs) {
                if (bomb != null){
                    bomb.update(timerCounter);
                }
            }
            for (Pirate pirate : pirates){
                if (pirate != null){
                    pirate.update(bombs, timerCounter, sailor, projectiles);
                    pirate.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);
                }
            }
            for (Item item : items){
                item.update();
            }
            // Testing here
            for (Projectile projectile : projectiles){
                if (projectile != null){
                    projectile.update();
                    projectile.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);
                    projectile.checkHitSailor(sailor);
                }
            }
            blackbeard.update(bombs, timerCounter, sailor, projectiles);
            blackbeard.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);

            sailor.update(input, bombs, timerCounter, pirates, blackbeard);
            sailor.checkCollisionsItem(items);
            sailor.isOutOfBound(leftBoundary,topBoundary,rightBoundary,bottomBoundary);
            treasure.update(timerCounter);
            if (sailor.checkTreasure(treasure)) {
                gameWin = true;
            }

        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        if (sailor.isDead()){
            gameEnd = true;
            drawEndScreen();
        }
        if (gameWin){
            drawWinScreen();
        }

        timerCounter ++;
    }

    /**
     * Method used to draw the start screen instructions
     */
    private void drawInstructionScreen(Input input, int currLevel){
        FONT.drawString(START_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(START_MESSAGE)/2.0)),
                FONT_Y_POS);
        FONT.drawString(INSTRUCTION_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(INSTRUCTION_MESSAGE)/2.0)),
                (FONT_Y_POS + INSTRUCTION_OFFSET));
        if (currLevel == 0) {
            FONT.drawString(INSTRUCTION_0, (Window.getWidth() / 2.0 - (FONT.getWidth(INSTRUCTION_0) / 2.0)),
                    (FONT_Y_POS + INSTRUCTION_OFFSET * 2));
        }
        else if (currLevel == 1){
            FONT.drawString(INSTRUCTION_1, (Window.getWidth() / 2.0 - (FONT.getWidth(INSTRUCTION_1) / 2.0)),
                    (FONT_Y_POS + INSTRUCTION_OFFSET * 2));
        }

        if (input.wasPressed(Keys.SPACE)){
            gameOn = false;
        }
    }
    private void drawLevelComplete(){
        FONT.drawString(LEVEL_COMPLETE,(Window.getWidth()/2.0 - (FONT.getWidth(LEVEL_COMPLETE)/2.0)),
                FONT_Y_POS);
        projectiles.clear();
        sailor.resetCurrHealth();
    }
    /**
     * Method used to draw end screen messages
     */
    private void drawEndScreen(){
        FONT.drawString(END_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(END_MESSAGE)/2.0)), FONT_Y_POS);
    }
    /**
    * Method used to draw win screen
    */
    private void drawWinScreen(){
        FONT.drawString(WIN_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(WIN_MESSAGE)/2.0)), FONT_Y_POS);
    }
}
