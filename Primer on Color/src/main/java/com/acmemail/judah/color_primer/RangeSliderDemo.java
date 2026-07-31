package com.acmemail.judah.color_primer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.acmemail.judah.color_primer.util.RangeSlider;

public class RangeSliderDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Range Slider Test Window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(450, 150);
            frame.setLayout(new GridBagLayout());

            // Instantiate slider with global min/max constraints
            RangeSlider rangeSlider = new RangeSlider(0, 100);
            rangeSlider.setValue(20);       // Bottom restriction
            rangeSlider.setUpperValue(80);  // Top restriction
            rangeSlider.setPreferredSize(new Dimension(350, 50));

            // Dynamic output label
            JLabel valueLabel = new JLabel("Selected Range: 20 to 80");
            rangeSlider.addChangeListener(e -> {
                valueLabel.setText("Selected Range: " + rangeSlider.getValue() + " to " + rangeSlider.getUpperValue());
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10,10,10,10);
            frame.add(rangeSlider, gbc);
            
            gbc.gridy = 1;
            frame.add(valueLabel, gbc);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}