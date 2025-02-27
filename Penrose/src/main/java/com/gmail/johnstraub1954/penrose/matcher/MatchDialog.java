package com.gmail.johnstraub1954.penrose.matcher;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MatchDialog
{
    private static final String         title   = "Select Edge";
    private static final String         prompt  = "Select Matching Edge";
    private static final ModalityType   modal   = 
        ModalityType.APPLICATION_MODAL;
    
    private final JDialog               dialog;
    private final JPanel                optionPanel;
    
    private IntConsumer                 optionMonitor   = null;
    
    private int choice  = JOptionPane.CANCEL_OPTION;
    
    public MatchDialog( Window parent, List<VertexPair[]> list )
    {
        dialog = new JDialog( parent, title, modal );
        JPanel  contentPane = new JPanel( new BorderLayout() );
        optionPanel = getOptionPanel( list );
        contentPane.add( new JLabel( prompt), BorderLayout.NORTH );
        contentPane.add( optionPanel, BorderLayout.CENTER );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        dialog.setContentPane( contentPane );
        dialog.setLocation( 100, 100 );
        dialog.pack();
    }
    
    public int showDialog()
    {
        JRadioButton    firstButton = 
            Stream.of( optionPanel.getComponents() )
                .filter( c -> c instanceof JRadioButton )
                .map( c -> (JRadioButton)c )
                .findFirst().orElse( null );
        if ( firstButton != null )
            firstButton.setSelected( true );
            
        dialog.setVisible( true );
        return choice;
    }
    
    public void setOptionMonitor( IntConsumer monitor )
    {
        optionMonitor = monitor;
    }
    
    private JPanel getOptionPanel( List<VertexPair[]> list )
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.Y_AXIS );
        ButtonGroup group   = new ButtonGroup();
        panel.setLayout( layout );
        for ( VertexPair[] pArr : list )
        {
            VertexPair      pair    = pArr[0];
            double          dist    = pair.getDistance();
            String          label   = String.format( "%6.1f", dist );
            JRadioButton    button  = new JRadioButton( label );
            group.add( button );
            panel.add( button );
            button.addActionListener( this::actionPerformed );
        }
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        panel.setLayout( layout );
        
        JButton     okButton        = new JButton( "OK" );
        JButton     cancelButton    = new JButton( "Cancel" );
        okButton.addActionListener( e -> close( JOptionPane.OK_OPTION ) );
        cancelButton.addActionListener( e -> 
            close( JOptionPane.CANCEL_OPTION ) );
        panel.add( okButton );
        panel.add( cancelButton );
        return panel;
    }
    
    private void close( int choice )
    {
        this.choice = choice;
        dialog.setVisible( false );
    }
    
    private void actionPerformed( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JRadioButton )
        {
            JRadioButton    button  = (JRadioButton)source;
            if ( button.isSelected() )
                select( button );
        }
    }
    
    private void select( JRadioButton button )
    {
        int choice  = 0;
        for ( Component comp : optionPanel.getComponents() )
        {
            if ( comp != button )
                ++choice;
            else
                break;
        }
        optionMonitor.accept( choice );
    }
}
