package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Interface defining the visual styling for dialogs that present two distinct,
 * non-standard options to the user.
 * <p>
 * This interface is typically used in History dialogs where the user needs to choose
 * between viewing details of two different related entities (e.g., viewing the Car
 * vs. viewing the Employee associated with a rental).
 * </p>
 */
public interface StyleTwoOptions {

    /**
     * Styles the first option button (Option 1).
     * Applies a <b>Terracotta/Orange</b> background color to distinguish it from
     * standard actions.
     *
     * @param button The JButton to style.
     */
    default void styleButtonOptionOne(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(218, 146, 94));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles the second option button (Option 2).
     * Applies a <b>Mustard/Gold</b> background color to distinguish it from
     * Option 1 and standard actions.
     *
     * @param button The JButton to style.
     */
    default void styleButtonOptionTwo(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(218, 177, 47));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
