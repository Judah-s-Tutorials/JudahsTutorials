package com.judahstutorials.glossary;

import javax.swing.SwingUtilities;

import com.judahstutorials.glossary.Controls.MainFrame;

public class MainSandbox
{
    /**
     * Application entry point.
     *
     * @param args command line arguments, not used
     *
    */
    public static void main(String[] args)
    {
        ConnectionMgr.selectDatabase( ConnectionMgr.SANDBOX );
        SwingUtilities.invokeLater( () -> new MainFrame() );
    }
    
    public MainSandbox()
    {
        // TODO Auto-generated constructor stub
    }

}
