package files;

public class elixer extends item {
    public elixer() {
        super("res\\items\\elixir.png", 35, "elixer", "res\\items\\elixirIcon.png");
    }

    public void Print(int x, int y) {
        getImage().draw(x, y);
    }
}
