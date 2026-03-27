package files;

import bagel.*;

public class potion extends item {
    public potion() {
        super("res\\items\\potion.png", 25, "potion", "res\\items\\potionIcon.png");
    }

    public void Print(int x, int y) {
        getImage().draw(x, y);
    }
}
