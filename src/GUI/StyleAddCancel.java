package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Interface defining the visual styling for "Add/Save" and "Cancel" actions across the application.
 * <p>
 * This interface acts as a style guide or "mixin" for dialogs. Classes that implement this
 * interface inherit standard fonts and default methods to style their buttons, ensuring
 * a consistent Look and Feel (L&F) for positive (Save) and negative (Cancel) actions.
 * </p>
 */
public interface StyleAddCancel {

    Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    Font regularFont = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Styles a button to represent a positive action (e.g., Add, Save, Confirm).
     * Applies a <b>Green</b> background color, white text, and a hand cursor.
     *
     * @param button The JButton to style.
     */
    default void styleButtonAdd(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(139, 179, 95));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles a button to represent a negative action (e.g., Cancel, Close, Abort).
     * Applies a <b>Red</b> background color, white text, and a hand cursor.
     *
     * @param button The JButton to style.
     */
    default void styleButtonCancel(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(227, 87, 87));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
