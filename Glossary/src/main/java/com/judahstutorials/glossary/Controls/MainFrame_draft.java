package com.judahstutorials.glossary.Controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition_draft;
import com.judahstutorials.glossary.SeeAlso_draft;

public class MainFrame_draft
{
    private final JFrame            frame       =
        new JFrame( "Glossary Editor" );
    
    private final JTextField        ident       = new JTextField( 20 );
    private final JTextField        term        = new JTextField( 20 );
    private final JTextField        seqNum      = new JTextField( 20 );
    private final JTextField        slug        = new JTextField( 20 );
    private final JTextArea         description = new JTextArea( 24, 40 );
    private final JTextField        addField    = new JTextField( 20 );
    private final JButton           addButton   = new JButton( "Add" );
    private final QueryDialog_draft       queryDialog = new QueryDialog_draft( frame );

    private final DefaultListModel<SeeAlso_draft> seeAlsoModel   =
        new DefaultListModel<>();
    private final JList<SeeAlso_draft>    seeAlso     = 
        new JList<>( seeAlsoModel );
    
    private Definition_draft  currDef;
    
    public MainFrame_draft()
    {
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        JPanel      contentPane = new JPanel( new BorderLayout() );
        contentPane.add( getMainPanel(), BorderLayout.CENTER );
        contentPane.add( getSeeAlsoPanel(), BorderLayout.EAST );
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

    private JPanel getMainPanel()
    {
        JPanel      panel   = new JPanel( new BorderLayout() );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        panel.add( getHeaderPanel(), BorderLayout.NORTH );
        panel.add( description, BorderLayout.CENTER );
        
        ident.setEditable( false );
        ident.addActionListener( this::identEvent );
        return panel;
    }

    private JPanel getHeaderPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.Y_AXIS );
        Border      border      =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        JLabel      defLabel    = new JLabel( "Definition_draft" );
//        defLabel.setAlignmentX( 0f );
//        panel.setAlignmentX( 0f );
        panel.setLayout( layout );
        panel.setBorder( border );
//        panel.setAlignmentX( Component.LEFT_ALIGNMENT );
        panel.add( getTextFieldPanel( ident, "Term ID:" ) );
        panel.add( getTextFieldPanel( term, "Term:     " ) );
        panel.add( getTextFieldPanel( seqNum, "Seq #:    " ) );
        panel.add( getTextFieldPanel( slug, "Slug:      " ) );
        panel.add( defLabel );
        
        ident.setEditable( false );
        ident.addActionListener( this::identEvent );
        return panel;
    }
    
    private JPanel getSeeAlsoPanel()
    {
        JPanel      panel       = new JPanel( new BorderLayout() );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        String  seeAlsoText = "<html><strong>See Also:</strong></html>";
        panel.add( new JLabel( seeAlsoText ), BorderLayout.NORTH );
        panel.add( getAddPanel(), BorderLayout.SOUTH );
        panel.add( getScrolledList(), BorderLayout.CENTER );
        return panel;
    }
    
    private JPanel getTextFieldPanel( JTextField textField, String label )
    {
        JPanel  panel   = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        panel.add( new JLabel( label ) );
        panel.add( textField );
        return panel;
    }
    
    private JScrollPane getScrolledList()
    {
        JScrollPane pane = new JScrollPane();
        pane.setViewportView( seeAlso );
        
        return pane;
    }
    
    private JPanel getAddPanel()
    {
        JPanel      panel       = new JPanel();
        BoxLayout   layout      = new BoxLayout( panel, BoxLayout.X_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setLayout( layout );
        panel.setBorder( border );

        addButton.addActionListener( this::addSeeAlso );
        addField.addActionListener( this::addSeeAlso );
        addField.setEnabled( false );
        addButton.setEnabled( false );
        
        panel.add( new JLabel( "New link: " ) );
        panel.add( addField );
        panel.add( addButton );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        JPanel      panel       = new JPanel();
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setBorder( border );
        
        JButton     newButton       = new JButton( "New" );
        JButton     queryButton     = new JButton( "Query" );
        JButton     insertButton    = new JButton( "Insert" );
        JButton     updateButton    = new JButton( "Update" );
        JButton     exitButton      = new JButton( "Exit" );
        newButton.addActionListener( this::newDef );
        insertButton.addActionListener( this::insertTerm );
        queryButton.addActionListener( this::query );
        exitButton.addActionListener( e -> {
            ConnectionMgr.closeConnection();
            System.exit( 0 );
        });
        
        panel.add( newButton );
        panel.add( queryButton );
        panel.add( insertButton );
        panel.add( updateButton );
        panel.add( exitButton );
        
        return panel;
    }
    
    private void identEvent( ActionEvent evt )
    {
        Object  obj     = evt.getSource();
        if ( obj instanceof JTextField )
        {
            JTextField  field   = (JTextField)obj;
            String      text    = field.getText();
            if ( text.isEmpty() )
            {
                seeAlso.removeAll();
                addField.setEnabled( false );
                addButton.setEnabled( false );
            }
            else
            {
                addField.setEnabled( true );
                addButton.setEnabled( true );
            }
        }
    }
    
    private void insertTerm( ActionEvent evt )
    {
        currDef = new Definition_draft(
            term.getText(),
            seqNum.getText(),
            slug.getText(),
            description.getText()
        );
        Integer ident   = currDef.insert();
        if ( ident != null )
        {
            this.ident.setText( String.valueOf( ident ) );
            this.slug.setText( currDef.getSlug() );
            this.ident.postActionEvent();
        }
    }
    
    private void newDef( ActionEvent evt )
    {
        currDef = new Definition_draft();
        term.setText( "" );
        ident.setText( "" );
        description.setText( "" );
        slug.setText( "" );
        addField.setText( "" );
        seeAlsoModel.removeAllElements();
        ident.postActionEvent();
    }
    
    private void addSeeAlso( ActionEvent evt )
    {
        String  text    = addField.getText();
        Integer termID  = null;
        if ( currDef == null )
            ;
        else if ( text == null )
            ;
        else if ( text.isEmpty() )
            ;
        else if ( (termID = currDef.getID()) == null )
            ;
        else
        {
            SeeAlso_draft next    = new SeeAlso_draft( termID, text );
            Integer rval    = next.insert();
            if ( rval == null )
                postError( "See also insert failed" );
            else
            {
                seeAlsoModel.addElement( next );
                int rowCount    = seeAlsoModel.getSize();
                if ( rowCount > 0 )
                    seeAlso.setSelectedIndex( 0 );
                addField.setText( "" );
            }
        }
    }
    
    private void query( ActionEvent evt )
    {
        Definition_draft  def     = null;
        int choice  = queryDialog.showDialog();
        if ( choice == JOptionPane.OK_OPTION )
            def = queryDialog.getSelection();
        if ( def != null )
        {
            currDef = def;
            int termID  = def.getID();
            ident.setText( String.valueOf( termID ) );
            term.setText( def.getTerm() );
            slug.setText( def.getSlug() );
            seqNum.setText( String.valueOf( def.getSeqNum() ) );
            description.setText( def.getDescription() );
            
            seeAlsoModel.removeAllElements();
            List<SeeAlso_draft>   list    = SeeAlso_draft.getAllFor( termID );
            System.out.println( termID );
            System.out.println( list.size() );
            seeAlsoModel.addAll( list );
            
            ident.postActionEvent();
        }
    }
    
    private static void postError( String err )
    {
        JOptionPane.showMessageDialog( null, err );
    }
    
}
