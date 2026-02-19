package com.acmemail.judah.sandbox;

import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.Root;

public class LineGeneratorDemoMain
{
    public static void main(String[] args)
    {
        JPanel  canvas  = new LineGeneratorDemoPanel();
        Root    root    = new Root( canvas );
        root.start();
    }
}
