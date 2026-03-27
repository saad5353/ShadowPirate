package files;

import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.*;
import java.util.concurrent.*;
import java.lang.*;

public class blackbeard {
    private final Image leftImage, rigtImage, leftImageInvincible, rigtImageInvincible, projectile;
    private int health, maxHealth;
    private int state; // 0=attack, 1=coolDown, 2=invincible
    private int currX, currY;
    private Font font;
    private DrawOptions fontColor;
    private Image currentImage;

    public blackbeard() {
        health = maxHealth = 90;
        currX = 0;
        currY = 0;
        leftImage = new Image("res\\blackbeard\\blackbeardLeft.png");
        rigtImage = new Image("res\\blackbeard\\blackbeardRight.png");
        leftImageInvincible = new Image("res\\blackbeard\\blackbeardHitLeft.png");
        rigtImageInvincible = new Image("res\\blackbeard\\blackbeardHitRight.png");
        projectile = new Image("res\\blackbeard\\blackbeardProjectile.png");
        font = new Font("res\\wheaton.otf", 15);
        fontColor = new DrawOptions();
        state = 0;
        currentImage = leftImage;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setCurrenImage(Image currentImage) {
        this.currentImage = currentImage;
    }

    public Image getLeftImage() {
        return leftImage;
    }

    public Image getRigtImage() {
        return rigtImage;
    }

    public Image getLeftImageInvincible() {
        return leftImageInvincible;
    }

    public Image getRigtImageInvincible() {
        return rigtImageInvincible;
    }

    public int getHealth() {
        return health;
    }

    public int getState() {
        return state;
    }

    public int getCurrX() {
        return currX;
    }

    public int getCurrY() {
        return currY;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setPosition(int x, int y) {
        currX = x;
        currY = y;
    }

    public void setCurrX(int x) {
        currX = x;
    }

    public void setCurrY(int y) {
        currY = y;
    }

    public void Print() {
        currentImage.draw(currX, currY);
        if ((int) (((float) health / (float) maxHealth) * 100) > 35) {
            String Health = (int) (((float) health / (float) maxHealth) * 100) + "%";
            font.drawString(Health, currX, currY - 30, fontColor.setBlendColour(0, 0.8, 0.2));
        } else {
            String Health = (int) (((float) health / (float) maxHealth) * 100) + "%";
            font.drawString(Health, currX, currY - 30, fontColor.setBlendColour(0.9, 0.6, 0));
        }
    }

    public void setPosture(int x, int y) {
        if (x > currX) {
            if (currentImage.equals(rigtImageInvincible)) {
                currentImage = rigtImageInvincible;
            } else {
                currentImage = rigtImage;
            }
        } else if (x < currX) {
            if (currentImage.equals(leftImageInvincible)) {
                currentImage = leftImageInvincible;
            } else {
                currentImage = leftImage;
            }
        }
    }

    public int calculateDistance(int x1, int y1) {
        int x2 = currX;
        int y2 = currY;
        int d = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return d;
    }

}
