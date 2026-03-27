package files;

import bagel.*;

public class sword extends item {
    public sword() {
        super("res\\items\\sword.png", 15, "sword", "res\\items\\swordIcon.png");
    }

    public void Print(int x, int y) {
        getImage().draw(x, y);
    }
}
