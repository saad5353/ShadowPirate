package files;

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthStyle;

import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;

import java.io.*;
import java.util.*;

public class level1 {
    private salior player;
    private final Image background, treasure;
    private ArrayList<bomb> block;
    private ArrayList<Integer> blockX;
    private ArrayList<Integer> blockY;
    private int topX, topY, bottomX, bottomY;
    private Font font, ingameFont;
    private DrawOptions fontColor;
    private ArrayList<pirate> pirates;
    private ArrayList<blackbeard> blackbeards;
    private boolean bulletFlag, bbBulletFlag, bbattacked;
    private final Image bullet;
    private int bulletX, bulletY, treasureX, treasureY;
    private float angle, bbangle;
    private int state, damage, bbstate;
    private boolean attacked, elixerFlag, swordFlag, potionFlag;
    private long startTime, endTime, endTime1, startTime1;
    private int swordX, swordY, elixerX, elixerY, potionX, potionY;
    private sword sword;
    private elixer elixer;
    private potion potion;
    private ArrayList<Image> items;
    private final Image bbBullet;
    private int bbBulletX, bbBulletY;
    private int counter;
    private boolean[] direction;
    private boolean bbDirection;

    public level1() {
        this.player = new salior();
        this.counter = 0;
        this.blackbeards = new ArrayList<blackbeard>();
        this.items = new ArrayList<>();
        this.treasure = new Image("res\\treasure.png");
        this.background = new Image("res\\background1.png");
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
        this.angle = bbangle = 0;
        this.state = bbstate = -1;
        this.attacked = false;
        this.startTime = startTime1 = -1;
        this.endTime = endTime1 = 0;
        this.swordX = this.swordY = this.elixerX = this.elixerY = this.potionX = this.potionY = 0;
        sword = new sword();
        elixer = new elixer();
        potion = new potion();
        elixerFlag = swordFlag = potionFlag = false;
        damage = 10;
        this.block = new ArrayList<bomb>();
        this.bbBullet = new Image("res\\blackbeard\\blackbeardProjectile.png");
        this.bbBulletX = 400;
        this.bbBulletY = 240;
        this.bbBulletFlag = true;
        this.bbattacked = false;
        this.bbDirection = true;
    }

    public salior getPlayer() {
        return this.player;
    }

    public void ReadCSV() {
        try {
            Scanner scanner = new Scanner(new File("res/level1.csv"));
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
                } else if (tokens[0].equals("Treasure")) {
                    treasureX = Integer.parseInt(tokens[1]);
                    treasureY = Integer.parseInt(tokens[2]);
                } else if (tokens[0].equals("Sword")) {
                    swordX = Integer.parseInt(tokens[1]);
                    swordY = Integer.parseInt(tokens[2]);
                } else if (tokens[0].equals("Elixir")) {
                    elixerX = Integer.parseInt(tokens[1]);
                    elixerY = Integer.parseInt(tokens[2]);
                } else if (tokens[0].equals("Potion")) {
                    potionX = Integer.parseInt(tokens[1]);
                    potionY = Integer.parseInt(tokens[2]);
                } else if (tokens[0].equals("Blackbeard")) {
                    blackbeard blackbeard = new blackbeard();
                    blackbeard.setPosition(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    blackbeards.add(blackbeard);
                }
            }
            scanner.close();
            player.addInventory(sword);
            player.addInventory(elixer);
            player.addInventory(potion);
            treasureX += 20;
            elixerFlag = swordFlag = potionFlag = true;
            for (int i = 0; i < blockX.size(); i++) {
                block.add(new bomb(blockX.get(i), blockY.get(i)));
            }
            direction = new boolean[blockX.size()];
            for (int i = 0; i < direction.length; i++) {
                direction[i] = true;
            }
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
        counter++;
        background.draw(1024 / 2, 768 / 2);
        if (counter % 25 == 0) {
            for (int i = 0; i < block.size(); i++) {
                if (block.get(i).getFlag() == true)
                    block.get(i).setPosition(-100, -100);
            }
        }
        for (int i = 0; i < block.size(); i++) {
            block.get(i).Print();
        }
        player.Print();
        treasure.draw(treasureX, treasureY);
        printInventory();
        showIcons();
        moveEnemies();
        BBCollidewithBlocks();
        EnemyCollidewithBlocks();
        long time2 = endTime1 - startTime1;
        for (int i = 0; i < blackbeards.size(); i++) {
            blackbeards.get(i).setPosture(player.getCurrX(), player.getCurrY());
            if (blackbeards.get(i).calculateDistance(player.getCurrX(), player.getCurrY()) < 200) {
                if (blackbeards.get(i).getHealth() > 0) {
                    if (bbBulletFlag == true) {
                        if (time2 > 3000) {

                            fireBuletBlackBeard(blackbeards.get(i).getCurrX(), blackbeards.get(i).getCurrY(),
                                    player.getCurrX(),
                                    player.getCurrY());
                            endTime1 = 0;
                        }
                    }
                }
            }

            if (blackbeards.get(i).getHealth() > 0) {
                blackbeards.get(i).Print();
            }
        }
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
        options.setRotation(bbangle);
        if (bbBulletFlag == false) {
            bbBullet.draw(bbBulletX, bbBulletY, options);
            startTime1 = System.currentTimeMillis();
        }
        BulletMovement();
        BulletMovementBlackBeard();
        BulletAttack();
        BulletAttackBlackBeard();
        endTime = System.currentTimeMillis();
        endTime1 = System.currentTimeMillis();
    }

    public boolean checkForWIN(Input input) {
        if (player.getCurrX() >= treasureX && player.getCurrY() <= treasureY + 10) {
            Drawing.drawRectangle(0, 0, 1280, 768, new Colour(0, 0, 0));
            double l1 = font.getWidth("CONGRATULATIONS!");
            font.drawString("CONGRATULATIONS!", l1 / 1.5, Window.getHeight() / 2.0);
            System.out.println("Sailor Found Treasure");
            if (input.wasPressed(Keys.ESCAPE)) {
                System.exit(0);
            }
            return true;
        }
        return false;
    }

    public void printInventory() {
        for (int i = 0; i < player.getInventory().size(); i++) {
            Image temp = player.getInventory().get(i).getImage();
            if (temp.equals(sword.getImage()) && swordFlag == true) {
                temp.draw(swordX, swordY);
            } else if (temp.equals(elixer.getImage()) && elixerFlag == true) {
                temp.draw(elixerX, elixerY);
            } else if (temp.equals(potion.getImage()) && potionFlag == true) {
                temp.draw(potionX, potionY);
            }
        }
    }

    public void grabItems() {
        for (int i = 0; i < player.getInventory().size(); i++) {
            Image temp = player.getInventory().get(i).getImage();
            int x = 0, y = 0;
            if (temp.equals(sword.getImage()) && swordFlag == true) {
                x = swordX;
                y = swordY;
            } else if (temp.equals(elixer.getImage()) && elixerFlag == true) {
                x = elixerX;
                y = elixerY;
            } else if (temp.equals(potion.getImage()) && potionFlag == true) {
                x = potionX;
                y = potionY;
            }
            if (player.getCurrX() >= x && player.getCurrX() <= x + 50 && player.getCurrY() >= y - 20
                    && player.getCurrY() <= y + 30) {
                if (temp.equals(sword.getImage()) && swordFlag == true) {
                    swordFlag = false;
                    items.add(sword.getIcon());
                    damage += 15;
                    System.out.println(
                            "Sailor finds Sword. New Health: " + player.getCurrHealth() + "/" + player.getMaxHealth());
                } else if (temp.equals(elixer.getImage()) && elixerFlag == true) {
                    elixerFlag = false;
                    items.add(elixer.getIcon());
                    player.setMaxHealth(player.getMaxHealth() + 35);
                    player.setCurrHealth(player.getCurrHealth() + 35);
                    System.out.println(
                            "Sailor finds Elixer. New Health: " + player.getCurrHealth() + "/" + player.getMaxHealth());
                } else if (temp.equals(potion.getImage()) && potionFlag == true) {
                    potionFlag = false;
                    items.add(potion.getIcon());
                    if (player.getCurrHealth() + 25 > player.getMaxHealth()) {
                        player.setCurrHealth(player.getMaxHealth());
                    } else {
                        player.setCurrHealth(player.getCurrHealth() + 25);
                    }
                    System.out.println(
                            "Sailor finds Potion. New Health: " + player.getCurrHealth() + "/" + player.getMaxHealth());
                }
            }
        }
    }

    public void showIcons() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(20, ((i + 1) * 35) + 30);
        }
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
                    pirates.get(i).setHealth((pirates.get(i).getHealth() - damage));
                    System.out.println("Sailor Attacked Pirate " + i + ". Pirate Health: "
                            + pirates.get(i).getHealth() + "/" + pirates.get(i).getMaxHealth());
                    temp = pirates.get(i).getCurrentImage();
                    if (temp.equals(pirates.get(i).getLeftImage())) {
                        pirates.get(i).setCurrenImage((pirates.get(i).getLeftImageInvincible()));
                    } else {
                        pirates.get(i).setCurrenImage((pirates.get(i).getRigtImageInvincible()));
                    }
                    startTime = System.currentTimeMillis();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean PlayerAttackBlackbeard() {
        for (int i = 0; i < blackbeards.size(); i++) {
            if (blackbeards.get(i).getHealth() > 0
                    && blackbeards.get(i).calculateDistance(player.getCurrX(), player.getCurrY()) < 30) {
                Image temp = player.getCurrentImage();
                if (temp.equals(player.getLeftAttackImage()) || temp.equals(player.getRightAttackImage())) {
                    blackbeards.get(i).setHealth((blackbeards.get(i).getHealth() - 10));
                    System.out.println("Salior Attacked blackbeard " + i + ". Blackbeard Health: "
                            + blackbeards.get(i).getHealth() + "/" + blackbeards.get(i).getMaxHealth());
                    temp = blackbeards.get(i).getCurrentImage();
                    if (temp.equals(blackbeards.get(i).getLeftImage())) {
                        blackbeards.get(i).setCurrenImage((blackbeards.get(i).getLeftImageInvincible()));
                    } else {
                        blackbeards.get(i).setCurrenImage((blackbeards.get(i).getRigtImageInvincible()));
                    }
                    startTime1 = System.currentTimeMillis();
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

    public void fireBuletBlackBeard(int pX, int pY, int sX, int sY) {
        if (pX == sX && pY != sY) {
            bbangle = 0;
            bbstate = 5;
        } else if (pX != sX && pY == sY) {
            bbangle = (float) Math.toRadians(90);
            bbstate = 6;
        } else if (pX != sX && pY != sY) {
            if (sX < pX && sY < pY) { // UP-LEFT
                bbangle = (float) Math.atan(((float) Math.abs(sY - pY) / (float) Math.abs(sX - pX)));
                bbstate = 1;
            } else if (sX < pX && sY > pY) { // DOWN-LEFT
                bbangle = (float) Math.atan(((float) (sY - pY) / (float) (sX - pX)));
                bbstate = 2;
            } else if (sX > pX && sY < pY) { // UP-RIGHT
                bbangle = (float) Math.atan(((float) Math.abs(pY - sY) / (float) Math.abs(sX - pX)));
                bbangle = (float) (Math.PI - bbangle);
                bbstate = 3;
            } else if (sX > pX && sY > pY) { // DOWN-RIGHT
                bbangle = (float) Math.atan(((float) (sY - pY) / (float) (pX - sX)));
                bbangle = (float) (Math.PI - bbangle);
                bbstate = 4;
            }
        }
        bbBulletX = pX;
        bbBulletY = pY;
        bbBulletFlag = false;
        bbattacked = false;
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

    public void BulletMovementBlackBeard() {
        if (bbBulletX < topX || bbBulletY < topY || bbBulletX > bottomX || bbBulletY > bottomY) {
            bbstate = 0;
            bbBulletFlag = true;

        } else {
            if (bbstate == 1) {
                bbBulletX -= Math.round(5 * Math.cos(bbangle));
                bbBulletY -= Math.round(5 * Math.sin(bbangle));
            } else if (bbstate == 2) {
                bbBulletX -= Math.round(5 * Math.cos(-bbangle));
                bbBulletY += Math.round(5 * Math.sin(-bbangle));
            } else if (bbstate == 3) {
                bbBulletX -= Math.round(5 * Math.cos(bbangle));
                bbBulletY += Math.round(5 * Math.sin(-bbangle));
            } else if (bbstate == 4) {
                bbBulletX -= Math.round(5 * Math.cos(bbangle));
                bbBulletY -= Math.round(5 * Math.sin(bbangle));
            } else if (bbstate == 5) {
                bbBulletX -= 3;
            } else if (bbstate == 6) {
                bbBulletY -= 3;
            }
        }
    }

    public void BulletAttack() {
        if (bulletFlag == false) {
            if (bulletX >= player.getCurrX() && bulletX <= player.getCurrX() + 100
                    && bulletY >= player.getCurrY() && bulletY <= player.getCurrY() + 100) {
                if (attacked == false) {
                    player.setCurrHealth(player.getCurrHealth() - 10);
                    attacked = true;
                    System.out.println("Pirate inflicts 10 damage to Sailor. Sailor's Health: " + player.getCurrHealth()
                            + "/" + player.getMaxHealth());
                }
            }
        }
    }

    public void BulletAttackBlackBeard() {
        if (bbBulletFlag == false) {
            if (bbBulletX >= player.getCurrX() - 20 && bbBulletX <= player.getCurrX() + 20
                    && bbBulletY >= player.getCurrY() - 30 && bbBulletY <= player.getCurrY() + 30) {
                if (bbattacked == false) {
                    player.setCurrHealth(player.getCurrHealth() - 20);
                    bbattacked = true;
                    System.out.println(
                            "Blackbeard inflicts 20 damage to Sailor. Sailor's Health: " + player.getCurrHealth()
                                    + "/" + player.getMaxHealth());
                }
            }
        }
    }

    public ArrayList<bomb> getBombs() {
        return block;
    }

    public void setBombs(ArrayList<bomb> bombs) {
        this.block = bombs;
    }

    public void GameOver(Input input) {
        if (player.getCurrHealth() <= 0) {
            Drawing.drawRectangle(0, 0, 1280, 768, new Colour(0, 0, 0));
            double l1 = font.getWidth("GAME OVER");
            font.drawString("GAME OVER", l1 * 1.5, Window.getHeight() / 2.0);
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
        for (int i = 0; i < blackbeards.size(); i++) {
            if (blackbeards.get(i).getHealth() > 0) {
                int x = blackbeards.get(i).getCurrX();
                int y = blackbeards.get(i).getCurrY();
                if (i % 2 == 0 && direction[i] == true) {
                    blackbeards.get(i).setCurrX(x + 1);
                    if (x >= bottomX - 20) {
                        direction[i] = false;
                    }
                } else if (i % 2 == 0 && direction[i] == false) {
                    blackbeards.get(i).setCurrX(x - 1);
                    if (x <= topX + 20) {
                        direction[i] = true;
                    }
                } else if (i % 2 == 1 && direction[i] == true) {
                    blackbeards.get(i).setCurrY(y + 1);
                    if (y >= bottomY - 20) {
                        direction[i] = false;
                    }
                } else if (i % 2 == 1 && direction[i] == false) {
                    blackbeards.get(i).setCurrY(y - 1);
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
                    // if pirate collides with block, reveerse direction
                    if (x >= blockX.get(j) && x <= blockX.get(j) + 45 && y >= blockY.get(j) - 35
                            && y <= blockY.get(j) + 15) {
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

    public void BBCollidewithBlocks() {
        for (int i = 0; i < blackbeards.size(); i++) {
            if (blackbeards.get(i).getHealth() > 0) {
                int x = blackbeards.get(i).getCurrX();
                int y = pirates.get(i).getCurrY();
                for (int j = 0; j < blockX.size(); j++) {
                    // if pirate collides with block, reveerse direction
                    if (x >= blockX.get(j) && x <= blockX.get(j) + 45 && y >= blockY.get(j) - 35
                            && y <= blockY.get(j) + 15) {
                        if (i % 2 == 0) {
                            bbDirection = !bbDirection;
                        } else {
                            bbDirection = !bbDirection;
                        }
                    }
                }
            }
        }
    }
}
