package files;

import bagel.*;

public class bomb {
    private Image image;
    private int currX, currY;
    private boolean flag;

    public bomb(int x, int y) {
        this.image = new Image("res\\bomb.png");
        this.currX = x;
        this.currY = y;
        flag = false;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void Print() {
        this.image.draw(this.currX, this.currY);
    }

    public void setPosition(int x, int y) {
        this.currX = x;
        this.currY = y;
    }

}
