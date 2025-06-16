package model;

import view.GameView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Upgrade {

    public enum Type { CHERRY, APPLE, STRAWBERRY, HEART , KEY }

    private static final Map<Type, ImageIcon> ICONS = new EnumMap<>(Type.class);
    static {
        load(Type.CHERRY,     "booster-cherry.png");//freezes all ghosts
        load(Type.APPLE,      "apple.png");//gives 100 pt
        load(Type.STRAWBERRY, "strawberry.png");//freezes the timer
        load(Type.HEART,      "heart.png"); //gives additional life
        load(Type.KEY, "key.png"); // gives an ability to eat ghosts

    }
    private static void load(Type t, String file) {
        try {
            var img = ImageIO.read(new
                    File("./pacman-art/other/" + file));
            ICONS.put(t, new ImageIcon(img.getScaledInstance(
                    GameView.CELL, GameView.CELL, java.awt.Image.SCALE_SMOOTH)));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static final Random RNG = new Random();
    public static Upgrade random() {
        var all = Type.values();
        return new Upgrade(all[RNG.nextInt(all.length)]);
    }

    private final Type type;
    public Upgrade(Type t) { type = t; }

    public Type      getType() { return type; }
    public ImageIcon getIcon() { return ICONS.get(type); }
}
