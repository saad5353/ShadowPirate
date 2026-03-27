import bagel.*;
import bagel.Input.*;
import bagel.util.Colour;
import files.*;
import java.util.concurrent.*;
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private level0 Level0;
    private level1 Level1;

    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        this.Level0 = new level0();
        this.Level1 = new level1();
        Level0.ReadCSV();
        Level1.ReadCSV();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    private Font font = new Font("res\\wheaton.otf", 50);
    private boolean flag = false;
    private long startTime = -1;
    private long endTime = 0;
    private int status = -1;
    private int temp = 0;
    private boolean StartFlag = false, attackFlag = false, level2 = false, ontime = false;

    @Override
    public void update(Input input) {
        if (StartFlag == false) {
            Drawing.drawRectangle(0, 0, 1280, 768, new Colour(0, 0, 0));
            StartFlag = Level0.startScreen(input);
        }
        if (StartFlag == true && level2 == false) {
            Level0.Print();
            Level0.getPlayer().controlMovement(input, Level0.getTopX(), Level0.getTopY(),
                    Level0.getBottomX(),
                    Level0.getBottomY(), Level0.getBlockX(), Level0.getBlockY(), level2,
                    Level1.getBombs());
            status = Level0.getPlayer().controlAttack(input);
            if (status != -1) {
                startTime = System.currentTimeMillis();
                temp = status;
            }
            if (attackFlag == false) {
                attackFlag = Level0.PlayerAttackPirate();
            }
            Level0.GameOver(input);
            long time = endTime - startTime;
            if (time > 2500) {
                if (temp == 1) {
                    Level0.getPlayer().setCurrenImage(Level0.getPlayer().getRightImage());
                }
                if (temp == 2) {
                    Level0.getPlayer().setCurrenImage(Level0.getPlayer().getLeftImage());
                }
                endTime = 0;
                attackFlag = false;
            }
            endTime = System.currentTimeMillis();
            level2 = Level0.checkForWIN(input);

            if (input.wasPressed(Keys.ESCAPE)) {
                Window.close();
            }
        }
        if (level2 == true) {
            Level1.Print();
            Level1.getPlayer().controlMovement(input, Level1.getTopX(), Level1.getTopY(), Level1.getBottomX(),
                    Level1.getBottomY(), Level1.getBlockX(), Level1.getBlockY(), level2, Level1.getBombs());
            status = Level1.getPlayer().controlAttack(input);
            if (status != -1) {
                startTime = System.currentTimeMillis();
                temp = status;
            }
            if (attackFlag == false) {
                attackFlag = Level1.PlayerAttackPirate();
            }
            if (attackFlag == false) {
                attackFlag = Level1.PlayerAttackBlackbeard();
            }
            Level1.GameOver(input);
            long time = endTime - startTime;
            if (time > 2500) {
                if (temp == 1) {
                    Level1.getPlayer().setCurrenImage(Level1.getPlayer().getRightImage());
                }
                if (temp == 2) {
                    Level1.getPlayer().setCurrenImage(Level1.getPlayer().getLeftImage());
                }
                endTime = 0;
                attackFlag = false;
            }
            endTime = System.currentTimeMillis();
            Level1.checkForWIN(input);
            Level1.grabItems();
            if (input.wasPressed(Keys.ESCAPE)) {
                Window.close();
            }
        }
    }

}
