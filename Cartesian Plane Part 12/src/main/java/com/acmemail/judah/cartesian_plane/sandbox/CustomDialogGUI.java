package com.acmemail.judah.cartesian_plane.sandbox;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.acmemail.judah.cartesian_plane.CartesianPlane;
import com.acmemail.judah.cartesian_plane.app.CommandExecutor;
import com.acmemail.judah.cartesian_plane.app.DialogInputApp;
import com.acmemail.judah.cartesian_plane.graphics_utils.Root;
import com.acmemail.judah.cartesian_plane.input.Command;
import com.acmemail.judah.cartesian_plane.input.CommandReader;
import com.acmemail.judah.cartesian_plane.input.Equation;
import com.acmemail.judah.cartesian_plane.input.EquationMap;
import com.acmemail.judah.cartesian_plane.input.ParsedCommand;

public class CustomDialogGUI
{
    // https://www.mzrg.com/math/graphs.shtml
    public static final String[] optionList =
    {
        "parabola",
        "hyperbola",
        "conic section",
        "parametric rose",
        "polar rose",
        "polar butterfly",
        "mzrg squiggle",
        "mzrg sunburst"
    };
    
    public static final int OK_STATUS       = 0;
    public static final int CANCEL_STATUS   = 1;

    private final JDialog dialog;
    private JList<String> jList;

    private int closeStatus     = -1;
    private int selectedItem    = 0;
    
    public static void main( String[] args )
    {
        CustomDialogGUI gui = new CustomDialogGUI();
        gui.setVisible( true );
    }

    public CustomDialogGUI()
    {
        dialog  = new JDialog( (Dialog)null, "Custom JDialog Demo" );
        dialog.setModal( true );
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setContentPane( new ContentPane() );
        dialog.pack();
    }
    
    public int getStatus()
    {
        return closeStatus;
    }
    
    public String getSelection()
    {
        String  name    = "";
        if ( selectedItem >= 0 )
            name    = jList.getSelectedValue();
        return name;
    }
    
    public void setVisible( boolean visible )
    {
        dialog.setVisible( true );
    }
    
    private class ContentPane extends JPanel
    {
        public ContentPane()
        {
            super( new BorderLayout() );
            add( new ListPanel(), BorderLayout.CENTER );
            add( new ButtonPanel(), BorderLayout.SOUTH );
        }
    }
    
    private class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            JButton okButton        = new JButton( "OK" );
            JButton cancelButton    = new JButton( "Cancel" );
            add( okButton );
            add( cancelButton );
            
            okButton.addActionListener( e -> setAndClose( OK_STATUS ) );
            cancelButton
                .addActionListener( e -> setAndClose( CANCEL_STATUS ) );
        }
    }

    private class ListPanel extends JPanel
    {
        public ListPanel()
        {
            jList = new JList<String>( optionList );
            jList.setSelectedIndex( selectedItem );
            add( jList );
            jList.addListSelectionListener( 
                e -> selectedItem = e.getFirstIndex()
            );
        }
    }
    
    private void setAndClose( int closeStatus )
    {
        this.closeStatus = closeStatus;
        dialog.setVisible( false );
    }
}
