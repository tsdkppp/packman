package controller;

import view.HighScoresView;
import view.MainMenuView;

import javax.swing.*;

public class MenuController {

    private final MainMenuView view;

    public MenuController() {
        view = new MainMenuView();
        hook();
    }

    private void hook() {
        view.getNewGameButton().addActionListener(e -> showBoardChooser());
        view.getHighScoresButton().addActionListener(e -> new HighScoresView(view));
        view.getExitButton().addActionListener(e -> System.exit(0));
    }

    private void showBoardChooser() {
        JPanel root = new JPanel();
        root.add(new JLabel("Which board would you choose?"));

        JPanel buttons = new JPanel(new java.awt.FlowLayout());
        for (BoardPreset p : BoardPreset.values()) {
            JButton b = new JButton(p.buttonText());
            b.addActionListener(ev -> {
                new GameController(p);
                view.dispose();
            });
            buttons.add(b);
        }
        root.add(buttons);

        JOptionPane.showMessageDialog(
                view, root, "Boards", JOptionPane.QUESTION_MESSAGE);
    }
}
