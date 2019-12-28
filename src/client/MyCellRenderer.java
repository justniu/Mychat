package client;

import common.User;

import javax.swing.*;
import java.awt.*;

public class MyCellRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 3460394416991636990L;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String user = (String)value;

        setText(user);
        setIcon(User.getHeadIcon());
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}