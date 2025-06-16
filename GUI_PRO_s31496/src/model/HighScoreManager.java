package model;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {

    private static final String FILE = "highscores.dat";

    public static List<HighScore> load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))) {
            return (List<HighScore>) in.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }
    public static void add(String name, int score) {
        List<HighScore> list = load();
        list.add(new HighScore(name, score));
        list.sort((a,b) -> Integer.compare(b.score(), a.score()));

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))) {
            out.writeObject(list);
        } catch (IOException e) { e.printStackTrace(); }
    }
}