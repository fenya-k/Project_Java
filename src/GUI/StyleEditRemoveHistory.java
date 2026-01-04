package GUI;

import javax.swing.*;
import java.awt.*;

public interface StyleEditRemoveHistory {

    Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    Font regularFont = new Font("Segoe UI", Font.PLAIN, 14);

  default void styleButtonEdit(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(139, 179, 95));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    default void styleButtonRemove(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(227, 87, 87));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

     default void styleButtonHistory(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(87, 136, 227));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
