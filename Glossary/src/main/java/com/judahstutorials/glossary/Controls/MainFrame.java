package com.judahstutorials.glossary.Controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition;

public class MainFrame
{
    public static final String  SEE_ALSO_PANEL      = "seeAlsoPanel";
    public static final String  TERM_ID_FIELD       = "defTermIDField";
    public static final String  TERM_FIELD          = "defTermField";
    public static final String  SEQ_NUM_FIELD       = "defSeqNumField";
    public static final String  SLUG_FIELD          = "defSlugField";
    public static final String  DESC_FIELD          = "defDefinitionText";
    public static final String  NEW_BUTTON          = "defNewButton";
    public static final String  QUERY_BUTTON        = "defQueryButton";
    public static final String  COMMIT_BUTTON       = "defCommitButton";
    public static final String  DELETE_BUTTON       = "defDeleteButton";
    public static final String  CANCEL_BUTTON       = "defCancelButton";
    public static final String  EXIT_BUTTON         = "defExitButton";

    private static final int    textWidth       = 20;
    
    private final JFrame            frame       =
        new JFrame( "Glossary Editor" );
    
    private final JTextField            ident           =
        getFormattedTextField( Integer.valueOf( 0 ) );
    private final JFormattedTextField   term            =
        getFormattedTextField( "" );
    private final JFormattedTextField   seqNum          = 
        getFormattedTextField( "" );
    private final JFormattedTextField   slug            = 
        getFormattedTextField( "" );
    private final JTextArea             description     =
        new JTextArea( 24, 40 );
    private final QueryDialog           queryDialog     = 
        new QueryDialog( frame );
    private final SeeAlsoPanel          seeAlsoPanel    =
        new SeeAlsoPanel();
    
    private final JButton   newButton       = new JButton( "New" );
    private final JButton   queryButton     = new JButton( "Query" );
    private final JButton   deleteButton    = new JButton( "Delete" );
    private final JButton   commitButton    = new JButton( "Commit" );
    private final JButton   cancelButton    = new JButton( "Cancel" );
    private final JButton   exitButton      = new JButton( "Exit" );
    private final List<JComponent>      abledComponents =
        getAbledComponents();
    
    private Definition  currDef;
    
    public MainFrame()
    {
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        
        seeAlsoPanel.setName( SEE_ALSO_PANEL );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( seeAlsoPanel, BorderLayout.EAST );
        contentPane.add( getControlPanel(), BorderLayout.SOUTH );
        
        frame.setLocation( 200, 100 );
        frame.setContentPane( contentPane );
        frame.pack();
        frame.setVisible( true );
        
        frame.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing( WindowEvent evt )
                {
                    ConnectionMgr.closeConnection();
                }
        });
    }
    
    public Definition getCurrDef()
    {
        return currDef;
    }

    private JPanel getMainPanel()
    {
        ident.setName( TERM_ID_FIELD );
        term.setName( TERM_FIELD );
        seqNum.setName( SEQ_NUM_FIELD );
        slug.setName( SLUG_FIELD );
        seqNum.setValue( Integer.valueOf( 0 ) );
        JPanel      panel   = new JPanel( new BorderLayout() );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        panel.add( getHeaderPanel(), BorderLayout.NORTH );
        
        description.setName( DESC_FIELD );
        description.setLineWrap( true );
        description.addKeyListener( new KeyAdapter() {
            @Override
            public void keyTyped( KeyEvent evt )
            {
                if ( currDef != null )
                    currDef.setDescription( description.getText() );
            }
        });
        JScrollPane scrollPane  = new JScrollPane( description );
        panel.add( scrollPane, BorderLayout.CENTER );
        ident.setEditable( false );
        reset( false );
        return panel;
    }

    private JPanel getHeaderPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border      =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        JLabel      defLabel    = new JLabel( "Definition" );
        panel.setLayout( layout );
        panel.setBorder( border );
        panel.add( getTextFieldPanel( ident, "Term ID:" ) );
        panel.add( getTextFieldPanel( term, "Term:     " ) );
        panel.add( getTextFieldPanel( seqNum, "Seq #:    " ) );
        panel.add( getTextFieldPanel( slug, "Slug:      " ) );
        panel.add( defLabel );
        
        ident.setEditable( false );
        term.addPropertyChangeListener( "value", e ->
            currDef.setTerm( (String)term.getValue() ) 
        );
        slug.addPropertyChangeListener( "value", e ->
            currDef.setSlug( (String)slug.getValue() ) 
        );
        seqNum.addPropertyChangeListener( "value", e ->
            currDef.setSeqNum( (Integer)seqNum.getValue() ) 
        );
        return panel;
    }
    
    private JPanel getTextFieldPanel( JTextField textField, String label )
    {
        JPanel  panel   = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        panel.add( new JLabel( label ) );
        panel.add( textField );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel       = new JPanel();
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        newButton.setName( NEW_BUTTON );
        queryButton.setName( QUERY_BUTTON );
        commitButton.setName( COMMIT_BUTTON );
        deleteButton.setName( DELETE_BUTTON );
        cancelButton.setName( CANCEL_BUTTON );
        exitButton.setName( EXIT_BUTTON );

        newButton.addActionListener( this::newDef );
        queryButton.addActionListener( this::query );
        commitButton.addActionListener( this::commit );
        deleteButton.addActionListener( this::delete );
        cancelButton.addActionListener( e -> reset( false ) );
        deleteButton.setEnabled( false );
        exitButton.addActionListener( e -> {
            ConnectionMgr.closeConnection();
            System.exit( 0 );
        });
        
        panel.add( newButton );
        panel.add( queryButton );
        panel.add( deleteButton );
        panel.add( commitButton );
        panel.add( cancelButton );
        panel.add( exitButton );
        
        return panel;
    }
    
    private JFormattedTextField getFormattedTextField( Object initialVal )
    {
        JFormattedTextField textField   = new JFormattedTextField();
        textField.setValue( initialVal );
        textField.setColumns( textWidth );
        return textField;
    }
    
    private List<JComponent> getAbledComponents()
    {
        List<JComponent>    list    = 
            Arrays.asList( 
                ident, 
                term, 
                seqNum, 
                slug, 
                description, 
                commitButton,
                cancelButton,
                seeAlsoPanel
        );
        List<JComponent>    ulist   =
            Collections.unmodifiableList( list );
        return ulist;
    }
    
    private void commit( ActionEvent evt )
    {
        currDef.setDescription( description.getText() );
        currDef.commit();
        ConnectionMgr.closeConnection();
        if ( currDef.isMarkedForDelete() )
        {
            currDef = null;
            reset( false );
        }
        seeAlsoPanel.setDefinition( currDef );
        frame.repaint();
    }
    
    private void delete( ActionEvent evt )
    {
        Object  source  = evt.getSource();
        if ( source instanceof JButton )
        {
            JButton button      = (JButton)source;
            boolean newState    = !currDef.isMarkedForDelete();
            currDef.markForDelete( newState );
            if ( newState )
            {
                button.setBackground( Color.RED );
            }
            else
            {
                button.setBackground( null );
            }
            frame.repaint();
        }
    }
    
    private void deleteDef( ActionEvent evt )
    {
        if ( currDef != null && currDef.getID() != null )
        {
            boolean currState   = currDef.isMarkedForDelete();
            currDef.markForDelete( !currState );
        }
    }
    
    private void newDef( ActionEvent evt )
    {
        currDef = new Definition();
        reset( true );
        seeAlsoPanel.setDefinition( currDef );
    }
    
    private void query( ActionEvent evt )
    {
        Definition  def     = null;
        int choice  = queryDialog.showDialog();
        if ( choice == JOptionPane.OK_OPTION )
            def = queryDialog.getSelection();
        if ( def != null )
        {
            currDef = def;
            int termID  = def.getID();
            reset( true );
            ident.setText( String.valueOf( termID ) );
            term.setText( def.getTerm() );
            slug.setText( def.getSlug() );
            seqNum.setValue( def.getSeqNum() );
            description.setText( def.getDescription() );
            
            seeAlsoPanel.setDefinition( currDef );
        }
    }
    
    private void enableInput( boolean enable )
    {
        ident.setEnabled( enable );
        term.setEnabled( enable );
        slug.setEnabled( enable );
        description.setEnabled( enable );
    }
    
    private void reset( boolean live )
    {
        deleteButton.setBackground( null );
        if ( currDef != null && currDef.getID() != null ) 
        {
            deleteButton.setEnabled( true );
            if ( currDef.isMarkedForDelete() )
                deleteButton.setBackground( Color.RED );
        }
        else
            deleteButton.setEnabled( false );
        
        ident.setText( "" );
        term.setValue( "" );
        slug.setValue( "" );
        seqNum.setText( "" );
        description.setText( "" );
        abledComponents.forEach( c -> c.setEnabled( live ) );
        seeAlsoPanel.setDefinition( null );
    }
}
