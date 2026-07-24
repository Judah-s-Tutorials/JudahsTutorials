package com.acmemail.judah.color_primer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSliderUI;

public class RangeSlider extends JSlider {

    private static final long serialVersionUID = 1L;

    public RangeSlider(int min, int max) {
        super(min, max);
        initSlider();
    }

    private void initSlider() {
        setOrientation(JSlider.HORIZONTAL);
    }

    @Override
    public int getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(int value) {
        int oldValue = getValue();
        if (oldValue == value) {
            return;
        }

        // Ensure lower value does not cross upper value
        int oldExtent = getExtent();
        int newValue = Math.min(Math.max(getMinimum(), value), oldValue + oldExtent);
        int newExtent = oldExtent + oldValue - newValue;

        getModel().setRangeProperties(newValue, newExtent, getMinimum(), getMaximum(), 
            getValueIsAdjusting());
    }

    public int getUpperValue() {
        return getValue() + getExtent();
    }

    public void setUpperValue(int value) {
        int lowerValue = getValue();
        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);
        setExtent(newExtent);
    }

    @Override
    public void updateUI() {
        setUI(new RangeSliderUI(this));
        updateLabelUIs();
    }

    // Custom UI implementation to handle two thumbs
    private static class RangeSliderUI extends BasicSliderUI {
        private Rectangle upperThumbRect;
        private boolean upperThumbSelected;
        private transient boolean lowerPressed;
        private transient boolean upperPressed;

        public RangeSliderUI(RangeSlider b) {
            super(b);
        }

        @Override
        public void installUI(JComponent c) {
            upperThumbRect = new Rectangle();
            super.installUI(c);
        }

        @Override
        protected TrackListener createTrackListener(JSlider slider) {
            return new RangeTrackListener();
        }

        @Override
        protected void calculateThumbSize() {
            super.calculateThumbSize();
            upperThumbRect.setSize(thumbRect.width, thumbRect.height);
        }

        @Override
        protected void calculateThumbLocation() {
            super.calculateThumbLocation();
            if (slider.getSnapToTicks()) {
                // Snap locations handled by default model logic if enabled
            }

            // Calculate lower thumb position
            int valuePosition = xPositionForValue(slider.getValue());
            thumbRect.x = valuePosition - (thumbRect.width / 2);
            thumbRect.y = trackRect.y + (trackRect.height / 2) - (thumbRect.height / 2);

            // Calculate upper thumb position
            RangeSlider rangeSlider = (RangeSlider) slider;
            int upperValuePosition = xPositionForValue(rangeSlider.getUpperValue());
            upperThumbRect.x = upperValuePosition - (upperThumbRect.width / 2);
            upperThumbRect.y = trackRect.y + (trackRect.height / 2) - (upperThumbRect.height / 2);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            // Redraw upper thumb over the track
            paintUpperThumb(g);
        }

        @Override
        public void paintTrack(Graphics g) {
            super.paintTrack(g);
            
            // Draw selected range highlight over the default track
            Color oldColor = g.getColor();
            g.setColor(UIManager.getColor("Slider.highlight"));
            
            int lowerX = thumbRect.x + (thumbRect.width / 2);
            int upperX = upperThumbRect.x + (upperThumbRect.width / 2);
            int cy = trackRect.y + (trackRect.height / 2) - 2;
            
            g.fillRect(lowerX, cy, upperX - lowerX, 4);
            g.setColor(oldColor);
        }

        @Override
        public void paintThumb(Graphics g) {
            // Paint lower thumb
            super.paintThumb(g);
        }

        private void paintUpperThumb(Graphics g) {
            Rectangle clipRect = g.getClipBounds();
            if (clipRect.intersects(upperThumbRect)) {
                // Re-use standard thumb painting logic targeting the upper thumb area
                Rectangle savedRect = thumbRect;
                thumbRect = upperThumbRect;
                super.paintThumb(g);
                thumbRect = savedRect;
            }
        }

        // Inner class to intercept mouse events for both thumbs
        private class RangeTrackListener extends TrackListener {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!slider.isEnabled()) return;

                currentMouseX = e.getX();
                currentMouseY = e.getY();

                if (slider.isRequestFocusEnabled()) {
                    slider.requestFocus();
                }

                lowerPressed = thumbRect.contains(e.getPoint());
                upperPressed = upperThumbRect.contains(e.getPoint());

                // If both are overlapping, prioritize based on movement intent or click side
                if (lowerPressed && upperPressed) {
                    if (e.getX() < thumbRect.x + (thumbRect.width / 2)) {
                        upperPressed = false;
                    } else {
                        lowerPressed = false;
                    }
                }

                if (lowerPressed) {
                    upperThumbSelected = false;
                    offset = currentMouseX - thumbRect.x;
                } else if (upperPressed) {
                    upperThumbSelected = true;
                    offset = currentMouseX - upperThumbRect.x;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!slider.isEnabled()) return;

                currentMouseX = e.getX();
                currentMouseY = e.getY();

                int halfThumb = thumbRect.width / 2;
                int thumbLeft = e.getX() - offset;
//                int trackLeft = trackRect.x;
//                int trackRight = trackRect.x + trackRect.width - 1;

                int newValue = valueForXPosition(thumbLeft + halfThumb);

                if (upperThumbSelected) {
                    ((RangeSlider) slider).setUpperValue(newValue);
                } else {
                    slider.setValue(newValue);
                }
                slider.repaint();
            }
        }
    }
}