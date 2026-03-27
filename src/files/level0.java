package files;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthStyle;

import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

import java.io.*;
import java.util.*;

public class level0 {
    private salior player;
    private final Image background;
    private final Image block;
    private ArrayList<Integer> blockX;
    private ArrayList<Integer> blockY;
    private boolean[] direction;
    private int topX, topY, bottomX, bottomY;
    private Font font, ingameFont;
    private DrawOptions fontColor;
    private ArrayList<pirate> pirates;
    private boolean bulletFlag;
    private final Image bullet;
    private int bulletX, bulletY;
    private float angle;
    private int state;
    private boolean attacked;
    private long startTime, endTime;

    public level0() {
        this.player = new salior();
        this.background = new Image("res\\background0.png");
        this.blockX = new ArrayList<>();
        this.blockY = new ArrayList<>();
        this.topX = 0;
        this.topY = 0;
        this.bottomX = 0;
        this.bottomY = 0;
        this.font = new Font("res\\wheaton.otf", 50);
        this.ingameFont = new Font("res\\wheaton.otf", 30);
        this.fontColor = new DrawOptions();
        this.pirates = new ArrayList<>();
        this.bulletFlag = true;
        this.bullet = new Image("res\\pirate\\pirateProjectile.png");
        this.bulletX = 400;
        this.bulletY = 240;
        this.angle = 0;
        this.state = -1;
        this.attacked = false;
        this.startTime = -1;
        this.endTime = 0;
        this.block = new Image("res\\block.png");
    }

    public salior getPlayer() {
        return this.player;
    }

    public void ReadCSV() {
        try {
            Scanner scanner = new Scanner(new File("res/level0.csv"));
            String line = scanner.nextLine();
            String[] values = line.split(",");
            player.setPosition(Integer.parseInt(values[1]), Integer.parseInt(values[2]));
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] tokens = line.split(",");
                if (tokens[0].equals("Block")) {
                    blockX.add(Integer.parseInt(tokens[1]));
                    blockY.add(Integer.parseInt(tokens[2]));
                } else if (tokens[0].equals("Pirate")) {
                    pirate pirate = new pirate();
                    pirate.setPosition(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    pirates.add(pirate);
                } else if (tokens[0].equals("TopLeft")) {
                    topX = Integer.parseInt(tokens[1]);
                    topY = Integer.parseInt(tokens[2]);
                } else if (tokens[0].equals("BottomRight")) {
                    bottomX = Integer.parseInt(tokens[1]);
                    bottomY = Integer.parseInt(tokens[2]);
                }
            }
            direction = new boolean[blockX.size()];
            for (int i = 0; i < direction.length; i++) {
                direction[i] = true;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public Image getBackground() {
        return this.background;
    }

    public ArrayList<Integer> getBlockX() {
        return this.blockX;
    }

    public ArrayList<Integer> getBlockY() {
        return this.blockY;
    }

    public boolean startScreen(Input input) {
        double l1 = font.getWidth("PRESS SPACE TO START");
        double l2 = font.getWidth("USE ARROW KEYS TO FIND LADDER");
        double l3 = font.getWidth("PRESS S TO ATTACK");
        font.drawString("PRESS SPACE TO START", l1 / 2.0, Window.getHeight() / 2.0 - 70);
        font.drawString("PRESS S TO ATTACK", l3 / 2.0 + 80, Window.getHeight() / 2.0);
        font.drawString("USE ARROW KEYS TO FIND LADDER", l2 / 6.0,
                Window.getHeight() / 2.0 + 70);
        if (input.wasPressed(Keys.SPACE)) {
            return true;
        }
        return false;
    }

    public void Print() {
        background.draw(1024 / 2, 768 / 2);
        for (int i = 0; i < blockX.size(); i++) {
            block.draw(blockX.get(i), blockY.get(i));
        }
        player.Print();
        moveEnemies();
        EnemyCollidewithBlocks();
        long time = endTime - startTime;
        for (int i = 0; i < pirates.size(); i++) {
            pirates.get(i).setPosture(player.getCurrX(), player.getCurrY());
            if (pirates.get(i).calculateDistance(player.getCurrX(), player.getCurrY()) < 100) {
                if (pirates.get(i).getHealth() > 0) {
                    if (bulletFlag == true) {
                        if (time > 3000) {
                            fireBulet(pirates.get(i).getCurrX(), pirates.get(i).getCurrY(),
                                    player.getCurrX(),
                                    player.getCurrY());
                            endTime = 0;
                        }
                    }
                }
            }
            if (pirates.get(i).getHealth() > 0) {
                pirates.get(i).Print();
            }
        }
        if ((int) (((float) player.getCurrHealth() / (float) player.getMaxHealth()) * 100) > 35) {
            String health = (int) (((float) player.getCurrHealth() / (float) player.getMaxHealth()) * 100) + "%";
            ingameFont.drawString(health, 10, 25, fontColor.setBlendColour(0, 0.8, 0.2));
        } else {
            String health = (int) (((float) player.getCurrHealth() / (float) player.getMaxHealth()) * 100) + "%";
            ingameFont.drawString(health, 10, 25, fontColor.setBlendColour(0.9, 0.6, 0));
        }
        DrawOptions options = new DrawOptions();
        options.setRotation(angle);
        if (bulletFlag == false) {
            bullet.draw(bulletX, bulletY, options);
            startTime = System.currentTimeMillis();
        }
        BulletMovement();
        BulletAttack();
        endTime = System.currentTimeMillis();
    }

    public boolean checkWin(Input input) {
        if (player.getCurrX() >= 990 && player.getCurrY() >= 640) {
            return true;
        }
        return false;
    }

    public boolean checkForWIN(Input input) {
        if (player.getCurrX() >= 990 && player.getCurrY() >= 640) {
            Drawing.drawRectangle(0, 0, 1280, 768, new Colour(0, 0, 0));
            double l1 = font.getWidth("PRESS SPACE TO START");
            double l2 = font.getWidth("FIND TREASURE");
            double l3 = font.getWidth("PRESS S TO ATTACK");
            font.drawString("PRESS SPACE TO START", l1 / 2.0, Window.getHeight() / 2.0 -
                    70);
            font.drawString("PRESS S TO ATTACK", l3 / 2.0 + 80, Window.getHeight() /
                    2.0);
            font.drawString("FIND TREASURE", l2 / 2.0 + 180,
                    Window.getHeight() / 2.0 + 70);
            System.out.println("Sailor Found ladder");
            if (input.wasPressed(Keys.SPACE)) {
                return true;
            } else if (input.wasPressed(Keys.ESCAPE)) {
                System.exit(0);
            }
        }
        return false;
    }

    public int getTopX() {
        return this.topX;
    }

    public int getTopY() {
        return this.topY;
    }

    public int getBottomX() {
        return this.bottomX;
    }

    public int getBottomY() {
        return this.bottomY;
    }

    public boolean PlayerAttackPirate() {
        for (int i = 0; i < pirates.size(); i++) {
            if (pirates.get(i).getHealth() > 0
                    && pirates.get(i).calculateDistance(player.getCurrX(), player.getCurrY()) < 30) {
                Image temp = player.getCurrentImage();
                if (temp.equals(player.getLeftAttackImage()) || temp.equals(player.getRightAttackImage())) {
                    pirates.get(i).setHealth((pirates.get(i).getHealth() - 10));
                    System.out.println("Sailor Attacked Pirate " + i + ". Sailor Health: "
                            + pirates.get(i).getHealth() + "/" + pirates.get(i).getMaxHealth());
                    temp = pirates.get(i).getCurrentImage();
                    if (temp.equals(pirates.get(i).getLeftImage())) {
                        pirates.get(i).setCurrenImage((pirates.get(i).getLeftImageInvincible()));
                    } else if (temp.equals(pirates.get(i).getRigtImage())) {
                        pirates.get(i).setCurrenImage((pirates.get(i).getRigtImageInvincible()));
                    }
                    startTime = System.currentTimeMillis();
                    return true;
                }
            }
        }
        return false;
    }

    public void fireBulet(int pX, int pY, int sX, int sY) {
        if (pX == sX && pY != sY) {
            angle = 0;
            state = 5;
        } else if (pX != sX && pY == sY) {
            angle = (float) Math.toRadians(90);
            state = 6;
        } else if (pX != sX && pY != sY) {
            if (sX < pX && sY < pY) { // UP-LEFT
                angle = (float) Math.atan(((float) Math.abs(sY - pY) / (float) Math.abs(sX - pX)));
                state = 1;
            } else if (sX < pX && sY > pY) { // DOWN-LEFT
                angle = (float) Math.atan(((float) (sY - pY) / (float) (sX - pX)));
                state = 2;
            } else if (sX > pX && sY < pY) { // UP-RIGHT
                angle = (float) Math.atan(((float) Math.abs(pY - sY) / (float) Math.abs(sX - pX)));
                angle = (float) (Math.PI - angle);
                state = 3;
            } else if (sX > pX && sY > pY) { // DOWN-RIGHT
                angle = (float) Math.atan(((float) (sY - pY) / (float) (pX - sX)));
                angle = (float) (Math.PI - angle);
                state = 4;
            }
        }
        bulletX = pX;
        bulletY = pY;
        bulletFlag = false;
        attacked = false;
    }

    public void BulletMovement() {
        if (bulletX < topX || bulletY < topY || bulletX > bottomX || bulletY > bottomY) {
            state = 0;
            bulletFlag = true;

        } else {
            if (state == 1) {
                bulletX -= Math.round(5 * Math.cos(angle));
                bulletY -= Math.round(5 * Math.sin(angle));
            } else if (state == 2) {
                bulletX -= Math.round(5 * Math.cos(-angle));
                bulletY += Math.round(5 * Math.sin(-angle));
            } else if (state == 3) {
                bulletX -= Math.round(5 * Math.cos(angle));
                bulletY += Math.round(5 * Math.sin(-angle));
            } else if (state == 4) {
                bulletX -= Math.round(5 * Math.sin(angle));
                bulletY -= Math.round(5 * Math.sin(angle));
            } else if (state == 5) {
                bulletX -= 3;
            } else if (state == 6) {
                bulletY -= 3;
            }
        }
    }

    public void BulletAttack() {
        if (bulletFlag == false) {
            if (bulletX >= player.getCurrX() - 20 && bulletX <= player.getCurrX() + 20
                    && bulletY >= player.getCurrY() - 20 && bulletY <= player.getCurrY() + 20) {
                if (attacked == false) {
                    player.setCurrHealth(player.getCurrHealth() - 10);
                    attacked = true;
                    System.out.println("Pirate inflicts 10 damage to player. Sailor's Health: " + player.getCurrHealth()
                            + "/" + player.getMaxHealth());
                    bulletFlag = true;
                    state = 0;
                }
            }
        }
    }

    public void GameOver(Input input) {
        if (player.getCurrHealth() <= 0) {
            Drawing.drawRectangle(0, 0, 1280, 768, new Colour(0, 0, 0));
            double l1 = font.getWidth("GAME OVER");
            font.drawString("GAME OVER", l1 * 1.5, Window.getHeight() / 2.0);
            System.out.println("Sailor Died. Game Over");
            if (input.wasPressed(Keys.ESCAPE)) {
                System.exit(0);
            }
        }
    }

    public void moveEnemies() {
        for (int i = 0; i < pirates.size(); i++) {
            if (pirates.get(i).getHealth() > 0) {
                int x = pirates.get(i).getCurrX();
                int y = pirates.get(i).getCurrY();
                if (i % 2 == 0 && direction[i] == true) {
                    pirates.get(i).setCurrX(x + 1);
                    if (x >= bottomX - 20) {
                        direction[i] = false;
                    }
                } else if (i % 2 == 0 && direction[i] == false) {
                    pirates.get(i).setCurrX(x - 1);
                    if (x <= topX + 20) {
                        direction[i] = true;
                    }
                } else if (i % 2 == 1 && direction[i] == true) {
                    pirates.get(i).setCurrY(y + 1);
                    if (y >= bottomY - 20) {
                        direction[i] = false;
                    }
                } else if (i % 2 == 1 && direction[i] == false) {
                    pirates.get(i).setCurrY(y - 1);
                    if (y <= topY + 20) {
                        direction[i] = true;
                    }
                }
            }
        }
    }

    public void EnemyCollidewithBlocks() {
        for (int i = 0; i < pirates.size(); i++) {
            if (pirates.get(i).getHealth() > 0) {
                int x = pirates.get(i).getCurrX();
                int y = pirates.get(i).getCurrY();
                for (int j = 0; j < blockX.size(); j++) {
                    if (x >= blockX.get(j) && x <= blockX.get(j) + 45
                            && y >= blockY.get(j) - 35 && y <= blockY.get(j) + 15) {
                        if (i % 2 == 0) {
                            direction[i] = !direction[i];
                        } else {
                            direction[i] = !direction[i];
                        }
                    }
                }
            }
        }
    }

}
