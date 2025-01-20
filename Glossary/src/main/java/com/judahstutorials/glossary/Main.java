package com.judahstutorials.glossary;

import javax.swing.SwingUtilities;

import Controls.MainFrame_draft;

public class Main
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater( () -> new MainFrame_draft() );
    }
    
    public Main()
    {
        // TODO Auto-generated constructor stub
    }

}
