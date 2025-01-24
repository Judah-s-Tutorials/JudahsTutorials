package com.judahstutorials.glossary.Controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.judahstutorials.glossary.ConnectionMgr;
import com.judahstutorials.glossary.Definition;

@SuppressWarnings("serial")
public class QueryDialog extends JDialog
{
    private static final String             likeStr =
        "SELECT * FROM definition "
        + "WHERE term LIKE ? " 
        + "ORDER BY term, SEQ_NUM";
    private static final String             allStr  =
        "SELECT * FROM definition "
        + "ORDER BY term, SEQ_NUM";
    
    private final JTextField        likeField   = new JTextField( 10 );
    private final DefaultListModel<String> resultModel   =
        new DefaultListModel<>();
    private final JList<String>     resultList  = 
        new JList<>( resultModel );
    private final List<Definition>  results     = new ArrayList<>();
    
    private int choice      = JOptionPane.OK_OPTION;
    private int selection   = -1;
    
    public QueryDialog( JFrame parent)
    {
        super( parent, true );
        setTitle( "Query Definition_draft Table" );
        JPanel  pane    = new JPanel( new BorderLayout() );
        pane.add( getLikePanel(), BorderLayout.NORTH );
        pane.add( getScrolledList(), BorderLayout.CENTER );
        pane.add( getControlPanel(), BorderLayout.SOUTH );
        setContentPane( pane );
        setLocation( 200, 200 );
        pack();
    }
    
    public int showDialog()
    {
        setVisible( true );
        return choice;
    }
    
    public Definition getSelection()
    {
        Definition  def     = null;
        if ( selection > 0 && selection < results.size() )
            def = results.get( selection );
        return def;
    }

    private JPanel getLikePanel()
    {
        JPanel      panel   = new JPanel();
        BoxLayout   layout  = new BoxLayout( panel, BoxLayout.X_AXIS );
        Border      border  =
            BorderFactory.createLineBorder( Color.BLACK, 2 );
        panel.setLayout( layout );
        panel.setBorder( border );
        JLabel  likeLabel   = new JLabel( "Like: " );
        panel.add( likeLabel );
        panel.add( likeField );
        likeField.addActionListener( this::execute );
        return panel;
    }
    
    private JPanel getControlPanel()
    {
        final int   OK_OPTION       = JOptionPane.OK_OPTION;
        final int   CANCEL_OPTION   = JOptionPane.CANCEL_OPTION;
        JPanel  panel           = new JPanel();
        JButton queryButton     = new JButton( "Execute" );
        JButton okButton        = new JButton( "OK" );
        JButton cancelButton    = new JButton( "Cancel" );
        queryButton.addActionListener( this::execute );
        okButton.addActionListener( e -> close( OK_OPTION ) );
        cancelButton.addActionListener( e -> close( CANCEL_OPTION ) );
        
        panel.add( queryButton );
        panel.add( okButton );
        panel.add( cancelButton );
        return panel;
    }
    
    private JScrollPane getScrolledList()
    {
        JScrollPane pane = new JScrollPane();
        pane.setViewportView( resultList );
        
        resultList.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt)
            {
                if ( evt.getClickCount() == 2 )
                    close( JOptionPane.OK_OPTION );
            }
        });

        return pane;
    }
    
    private void execute( ActionEvent evt )
    {
        try
        {
            String              like        = likeField.getText();
            PreparedStatement   statement   = null;
            if ( like.isEmpty() )
                statement = ConnectionMgr.getPreparedStatement( allStr );
            else
            {
                statement = ConnectionMgr.getPreparedStatement( likeStr );
                statement.setString( 1, like );
            }
            ResultSet       resultSet   = statement.executeQuery();
            resultModel.removeAllElements();
            results.clear();
            while ( resultSet.next() )
            {
                Definition  def     = new Definition( resultSet );
                String      term    = def.getTermDisplay();
                resultModel.addElement( term );
                results.add( def );
            }
            resultSet.close();
            ConnectionMgr.closeConnection();
        }
        catch ( SQLException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
    }
    
    private void close( int status )
    {
        choice = status;
        selection = resultList.getSelectedIndex();
        setVisible( false );
    }
}
