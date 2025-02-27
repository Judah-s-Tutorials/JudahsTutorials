package com.judahstutorials.glossary;

import javax.swing.SwingUtilities;

import com.judahstutorials.glossary.Controls.MainFrame;

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
        ConnectionMgr.selectDatabase( ConnectionMgr.PRODUCTION );
        SwingUtilities.invokeLater( () -> new MainFrame() );
    }
    
    public Main()
    {
        // TODO Auto-generated constructor stub
    }

}
