package com.acmemail.judah.battleship;

import java.awt.Component;

import javax.swing.Icon;

public interface JOptionPaneI
{

    Object showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon,
        Object[] selectionValues, Object initialSelectionValue);

    String showInputDialog(Component parent, Object message);

    void showMessageDialog(Component parent, Object message, String title, int type);

}