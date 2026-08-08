package com.acmemail.judah.color_primer.sandbox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SliderKeyOverrideDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SliderKeyOverrideDemo::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("Slider Left-Arrow Override");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSlider slider = new JSlider(0, 100, 50);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JLabel status = new JLabel("Value: " + slider.getValue(), SwingConstants.CENTER);
        slider.addChangeListener(e -> status.setText("Value: " + slider.getValue()));

        overrideLeftArrow(slider, status);

        frame.setLayout(new BorderLayout());
        frame.add(slider, BorderLayout.CENTER);
        frame.add(status, BorderLayout.SOUTH);
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Replaces the default LEFT-arrow behavior (negativeUnitIncrement)
     * with a custom action. Here, instead of decrementing by 1, it
     * decrements by 5 and prints a message.
     */
    private static void overrideLeftArrow(JSlider slider, JLabel status) {
        InputMap inputMap = slider.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = slider.getActionMap();

        // Find the KeyStroke currently bound to LEFT so we know what we're replacing.
        KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");

        // What action name does LEFT currently map to? (For reference/debugging.)
        Object defaultActionKey = inputMap.get(leftKey);
        System.out.println("Default action bound to LEFT: " + defaultActionKey);

        // Bind LEFT to a new action key name.
        String customKey = "customLeftAction";
        inputMap.put(leftKey, customKey);

        // Define the custom action.
        actionMap.put(customKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newValue = Math.max(slider.getMinimum(), slider.getValue() - 5);
                slider.setValue(newValue);
                status.setText("Value: " + slider.getValue() + " (custom left-arrow jump)");
            }
        });

        // Also handle the numpad-left variant so both keys behave consistently.
        KeyStroke kpLeftKey = KeyStroke.getKeyStroke("KP_LEFT");
        inputMap.put(kpLeftKey, customKey);
    }
}