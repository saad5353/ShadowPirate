package files;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.*;
import java.util.concurrent.*;
import java.lang.*;

public class salior {
    private boolean posture; // True right , false left
    private final Image rightImage, leftImage, rightAttackImage, leftAttackImage;
    private Image currentImage;
    private int currHealth;
    private int maxHealth;
    private ArrayList<item> inventory;
    private int currX, currY;
    private ArrayList<Integer> values;

    public salior() {
        this.posture = true;
        this.rightImage = new Image("res\\sailor\\sailorRight.png");
        this.leftImage = new Image("res\\sailor\\sailorLeft.png");
        this.rightAttackImage = new Image("res\\sailor\\sailorHitRight.png");
        this.leftAttackImage = new Image("res\\sailor\\sailorHitLeft.png");
        this.currentImage = this.rightImage;
        this.currHealth = 100;
        this.maxHealth = 100;
        this.inventory = new ArrayList<>();
        this.currX = 0;
        this.currY = 0;
        values = new ArrayList<>();
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public ArrayList<item> getInventory() {
        return this.inventory;
    }

    public void addInventory(item item) {
        this.inventory.add(item);
    }

    public int getCurrHealth() {
        return this.currHealth;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void setCurrHealth(int currHealth) {
        this.currHealth = currHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrX() {
        return this.currX;
    }

    public int getCurrY() {
        return this.currY;
    }

    public void controlMovement(Input input, int topX, int topY, int bottomX, int bottomY, ArrayList<Integer> blockX,
            ArrayList<Integer> blockY, boolean flag, ArrayList<bomb> block) {
        if (flag == false) {
            if (input.isDown(Keys.LEFT) && this.currX > topX) {
                this.currX -= 3;
                if (BlockCollsion(blockX, blockY)) {
                    this.currX += 3;
                }
                this.posture = false;
                this.currentImage = this.leftImage;
            }
            if (input.isDown(Keys.RIGHT) && this.currX < bottomX) {
                this.currX += 3;
                if (BlockCollsion(blockX, blockY)) {
                    this.currX -= 3;
                }
                this.posture = true;
                this.currentImage = this.rightImage;
            }
            if (input.isDown(Keys.UP) && this.currY > topY) {
                this.currY -= 3;
                if (BlockCollsion(blockX, blockY)) {
                    this.currY += 3;
                }
            }
            if (input.isDown(Keys.DOWN) && this.currY < bottomY) {
                this.currY += 3;
                if (BlockCollsion(blockX, blockY)) {
                    this.currY -= 3;
                }
            }
        } else {
            if (input.isDown(Keys.LEFT) && this.currX > topX) {
                this.currX -= 3;
                this.posture = false;
                this.currentImage = this.leftImage;
                BombCollsion(blockX, blockY, block);
            }
            if (input.isDown(Keys.RIGHT) && this.currX < bottomX) {
                this.currX += 3;
                this.posture = true;
                this.currentImage = this.rightImage;
                BombCollsion(blockX, blockY, block);
            }
            if (input.isDown(Keys.UP) && this.currY > topY) {
                this.currY -= 3;
                BombCollsion(blockX, blockY, block);
            }
            if (input.isDown(Keys.DOWN) && this.currY < bottomY) {
                this.currY += 3;
                BombCollsion(blockX, blockY, block);
            }
        }
    }

    public int controlAttack(Input input) {
        if (input.wasPressed(Keys.S)) {
            if (this.posture) {
                this.currentImage = this.rightAttackImage;
                return 1;
            } else {
                this.currentImage = this.leftAttackImage;
                return 3;
            }
        }
        return -1;
    }

    public Image getRightImage() {
        return this.rightImage;
    }

    public Image getLeftImage() {
        return this.leftImage;
    }

    public Image getCurrentImage() {
        return this.currentImage;
    }

    public void setCurrenImage(Image currentImage) {
        this.currentImage = currentImage;
    }

    public boolean getPosture() {
        return this.posture;
    }

    public Image getRightAttackImage() {
        return this.rightAttackImage;
    }

    public Image getLeftAttackImage() {
        return this.leftAttackImage;
    }

    public void setPosition(int x, int y) {
        this.currX = x;
        this.currY = y;
    }

    public void Print() {
        currentImage.draw(currX, currY);
    }

    public boolean BlockCollsion(ArrayList<Integer> blockX, ArrayList<Integer> blockY) {
        for (int i = 0; i < blockX.size(); i++) {
            if (currX + 20 >= blockX.get(i) && currX <= blockX.get(i) + 20
                    && currY + 30 >= blockY.get(i) && currY <= blockY.get(i)) {
                return true;
            }
        }
        return false;
    }

    public void BombCollsion(ArrayList<Integer> blockX, ArrayList<Integer> blockY, ArrayList<bomb> block) {
        for (int i = 0; i < block.size(); i++) {
            if (currX + 20 >= blockX.get(i) && currX <= blockX.get(i) + 20
                    && currY + 30 >= blockY.get(i) && currY <= blockY.get(i) + 20) {
                if (block.get(i).getFlag() == false) {
                    currHealth -= 10;
                    block.get(i).setImage(new Image("res\\explosion.png"));
                    block.get(i).setFlag(true);
                    System.out.println("Sailor Collided with a Bomb. Sailor's Health: " + this.currHealth + "/"
                            + this.maxHealth);
                    return;
                }
            } else {
                continue;
            }
        }
    }

    public boolean findVal(int val) {
        return values.contains(val);
    }

}
