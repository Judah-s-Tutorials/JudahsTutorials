package com.acmemail.judah.graphics.sandbox;

import java.awt.Dimension;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Canvas_part1 extends JPanel
{
    public Canvas_part1( int width, int height )
    {
        Dimension   dim = new Dimension( width, height );
        setPreferredSize( dim );
    }
}
