package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Interface defining the visual styling for the main action buttons in table views.
 * <p>
 * This interface provides a consistent Look and Feel (L&F) for the three primary
 * operations available in management tables:
 * <ul>
 * <li><b>Edit:</b> Modifying an existing record.</li>
 * <li><b>Remove:</b> Deleting a record (or Returning a rental).</li>
 * <li><b>History:</b> Viewing the transaction history of an entity.</li>
 * </ul>
 * </p>
 */
public interface StyleEditRemoveHistory {

    Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    Font regularFont = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Styles a button to represent an "Edit" or "Update" action.
     * Applies a <b>Green</b> background color, white text, and a hand cursor.
     *
     * @param button The JButton to style.
     */
    default void styleButtonEdit(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(139, 179, 95));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles a button to represent a "Remove", "Delete", or "Return" action.
     * Applies a <b>Red</b> background color to indicate a destructive or critical action.
     *
     * @param button The JButton to style.
     */
    default void styleButtonRemove(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(227, 87, 87));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Styles a button to represent a "View History" or informational action.
     * Applies a <b>Blue</b> background color to distinguish it from modification actions.
     *
     * @param button The JButton to style.
     */
    default void styleButtonHistory(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(87, 136, 227));
        button.setForeground(new Color(251, 244, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
