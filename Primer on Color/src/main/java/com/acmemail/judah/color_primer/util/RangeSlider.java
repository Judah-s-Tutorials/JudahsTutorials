package com.acmemail.judah.color_primer.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
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
    
    public boolean isLowerSelected()
    {
        boolean result  = false;
        if ( getUI() instanceof RangeSliderUI sliderUI )
        {
            result = sliderUI.isLowerSelected();
        }
        return result;
    }

    public int getUpperValue() {
        return getValue() + getExtent();
    }
    
    public int getLowerValue()
    {
        return getValue();
    }

    public void setUpperValue(int value) {
        int lowerValue = getValue();
        int newExtent = Math.min(Math.max(0, value - lowerValue), getMaximum() - lowerValue);
        setExtent(newExtent);
    }
    
    public void setLowerValue( int value )
    {
        setValue( value );
    }

    @Override
    public void updateUI() {
        setUI(new RangeSliderUI(this));
        updateLabelUIs();
    }

    // Custom UI implementation to handle two thumbs
    private static class RangeSliderUI extends BasicSliderUI {
        private static final int   UNIT_INCR   = 1;
        private static final int   BLOCK_INCR  = 10;

        private Rectangle upperThumbRect;
        private boolean upperThumbSelected;
        // Default to the lower thumb so that keyboard actions have a
        // well-defined target before the user has clicked either thumb.
        private transient boolean lowerPressed = true;

        public RangeSliderUI(RangeSlider b) {
            super(b);
        }

        public boolean isLowerSelected()
        {
            return lowerPressed;
        }

        @Override
        public void installUI(JComponent c) {
            upperThumbRect = new Rectangle();
            super.installUI(c);
        }

        /**
         * Replace the single-thumb Left/Right/PageUp/PageDown/Home/End
         * bindings installed by BasicSliderUI with range-aware versions
         * that act on whichever thumb (lower or upper) is currently
         * selected, per {@link #isLowerSelected()}.
         */
        @Override
        protected void installKeyboardActions(JSlider slider) {
            super.installKeyboardActions(slider);

            final KeyStroke leftKey     = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
            final KeyStroke kpLeftKey   = KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0);
            final String    leftAction  = "rangeLeftAction";

            final KeyStroke rightKey    = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
            final KeyStroke kpRightKey  = KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0);
            final String    rightAction = "rangeRightAction";

            final KeyStroke pageUpKey       = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0);
            final String    pageUpAction    = "rangePageUpAction";

            final KeyStroke pageDownKey     = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0);
            final String    pageDownAction  = "rangePageDownAction";

            final KeyStroke homeKey     = KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0);
            final String    homeAction  = "rangeHomeAction";

            final KeyStroke endKey      = KeyStroke.getKeyStroke(KeyEvent.VK_END, 0);
            final String    endAction   = "rangeEndAction";

            InputMap    inputMap    = slider.getInputMap();
            ActionMap   actionMap   = slider.getActionMap();

            inputMap.put(leftKey, leftAction);
            inputMap.put(kpLeftKey, leftAction);
            inputMap.put(rightKey, rightAction);
            inputMap.put(kpRightKey, rightAction);
            inputMap.put(pageUpKey, pageUpAction);
            inputMap.put(pageDownKey, pageDownAction);
            inputMap.put(homeKey, homeAction);
            inputMap.put(endKey, endAction);

            RangeSlider rangeSlider = (RangeSlider) slider;

            actionMap.put(leftAction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (isLowerSelected())
                        decrementLower(rangeSlider, UNIT_INCR);
                    else
                        decrementUpper(rangeSlider, UNIT_INCR);
                }
            });

            actionMap.put(rightAction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (isLowerSelected())
                        incrementLower(rangeSlider, UNIT_INCR);
                    else
                        incrementUpper(rangeSlider, UNIT_INCR);
                }
            });

            actionMap.put(pageUpAction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (isLowerSelected())
                        incrementLower(rangeSlider, BLOCK_INCR);
                    else
                        incrementUpper(rangeSlider, BLOCK_INCR);
                }
            });

            actionMap.put(pageDownAction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (isLowerSelected())
                        decrementLower(rangeSlider, BLOCK_INCR);
                    else
                        decrementUpper(rangeSlider, BLOCK_INCR);
                }
            });

            actionMap.put(homeAction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (isLowerSelected())
                        rangeSlider.setLowerValue(rangeSlider.getMinimum());
                    else
                        rangeSlider.setUpperValue(rangeSlider.getLowerValue());
                }
            });

            actionMap.put(endAction, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (isLowerSelected())
                        rangeSlider.setLowerValue(rangeSlider.getUpperValue());
                    else
                        rangeSlider.setUpperValue(rangeSlider.getMaximum());
                }
            });
        }

        private static void incrementUpper(RangeSlider slider, int amount) {
            int currVal = slider.getUpperValue();
            int testVal = currVal + amount;
            int maxVal  = slider.getMaximum();
            int actVal  = Math.min(testVal, maxVal);
            slider.setUpperValue(actVal);
        }

        private static void decrementUpper(RangeSlider slider, int amount) {
            int currVal = slider.getUpperValue();
            int testVal = currVal - amount;
            int minVal  = slider.getLowerValue();
            int actVal  = Math.max(testVal, minVal);
            slider.setUpperValue(actVal);
        }

        private static void incrementLower(RangeSlider slider, int amount) {
            int currVal = slider.getLowerValue();
            int testVal = currVal + amount;
            int maxVal  = slider.getUpperValue();
            int actVal  = Math.min(testVal, maxVal);
            slider.setLowerValue(actVal);
        }

        private static void decrementLower(RangeSlider slider, int amount) {
            int currVal = slider.getLowerValue();
            int testVal = currVal - amount;
            int minVal  = slider.getMinimum();
            int actVal  = Math.max(testVal, minVal);
            slider.setLowerValue(actVal);
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
            // True only while a drag that began with a press on one of
            // the two thumbs is in progress. Kept separate from
            // lowerPressed, which intentionally persists across track
            // clicks to report keyboard-action target; this flag must
            // NOT persist, since mouseDragged must never move a thumb
            // unless the drag actually started on that thumb.
            private boolean dragging;

            @Override
            public void mousePressed(MouseEvent e) {
                if (!slider.isEnabled()) return;

                currentMouseX = e.getX();
                currentMouseY = e.getY();

                if (slider.isRequestFocusEnabled()) {
                    slider.requestFocus();
                }

                boolean hitLower = thumbRect.contains(e.getPoint());
                boolean hitUpper = upperThumbRect.contains(e.getPoint());

                // If both are overlapping, prioritize based on movement intent or click side
                if (hitLower && hitUpper) {
                    if (e.getX() < thumbRect.x + (thumbRect.width / 2)) {
                        hitUpper = false;
                    } else {
                        hitLower = false;
                    }
                }

                // A click that lands on neither thumb (e.g. on the track)
                // leaves the previous lower/upper selection unchanged,
                // rather than clearing it, since lowerPressed/upperPressed
                // also report which thumb keyboard actions should target.
                // It does NOT start a drag.
                dragging = hitLower || hitUpper;
                if (hitLower) {
                    lowerPressed = true;
                    upperThumbSelected = false;
                    offset = currentMouseX - thumbRect.x;
                } else if (hitUpper) {
                    lowerPressed = false;
                    upperThumbSelected = true;
                    offset = currentMouseX - upperThumbRect.x;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!slider.isEnabled() || !dragging) return;

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