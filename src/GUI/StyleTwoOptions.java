package GUI;

import javax.swing.*;
import java.awt.*;

public interface StyleTwoOptions {

    default void styleButtonOptionOne(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(218, 146, 94));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    default void styleButtonOptionTwo(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(218, 177, 47));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
