package files;

import bagel.*;

public class item {
    private final Image image, icon;
    private final int points;
    private final String name;

    public item(String image, int points, String name, String icon) {
        this.image = new Image(image);
        this.points = points;
        this.name = name;
        this.icon = new Image(icon);
    }

    public Image getImage() {
        return image;
    }

    public Image getIcon() {
        return icon;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }
}
