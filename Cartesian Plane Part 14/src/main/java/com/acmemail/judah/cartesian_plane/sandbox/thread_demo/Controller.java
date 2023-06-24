package com.acmemail.judah.cartesian_plane.sandbox.thread_demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class Controller
{
    private final List<Class<?>>        classes         = new ArrayList<>();
    private final Map<String,Element>   classMap        = new HashMap<>();
    private final Map<String,Element>   instanceMap     = new HashMap<>();
    private final Map<String,Thread>    threadMap       = new HashMap<>();
    private final ButtonGroup           classGroup      = new ButtonGroup();
    private final ButtonGroup           instanceGroup   = new ButtonGroup();
    private final ButtonGroup           threadGroup     = new ButtonGroup();
    private final Map<String, Consumer<Element>>    funkMap = new HashMap<>();
    
    private Element     selectedClass;
    private Element     selectedParam;
    private LogDisplay  logDisplay      = new LogDisplay();
    
    public static void main( String[] args )
    {
        Controller  controller  = new Controller();
        SwingUtilities.invokeLater( () -> controller.build() );
    }
    
    public Controller()
    {
        classes.add( Hydrogen.class );
        classes.add( Helium.class );
        classes.add( Lithium.class );
        classes.add( Berylium.class );
        
        Class<?>[]  dummyParam  = new Class<?>[0];  
        try
        {
            for ( Class<?> clazz : classes )
            {
                Constructor<?>  ctor    = clazz.getConstructor( dummyParam );
                Element         element = (Element)ctor.newInstance();
                String          className   = element.getName();
                instanceMap.put( className, element );
                classMap.put( className, element );
            }
        }
        catch ( 
            InvocationTargetException 
            | NoSuchMethodException
            | SecurityException
            | IllegalAccessException
            | IllegalArgumentException
            | InstantiationException exc 
        )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        SwingUtilities.invokeLater( () -> logDisplay.build() );
    }
    
    public void build()
    {
        JFrame  frame   = new JFrame( "Thread Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( new ContentPane() );
        frame.pack();
        frame.setVisible( true );
        
    }
    
    public void doRun()
    {
        Thread  thread  = Thread.currentThread();
        String  name    = thread.getName();
        while ( true )
        {
            synchronized( thread )
            {
                System.out.println( name + " waiting" );
                try
                {
                    thread.wait();
                }
                catch ( InterruptedException exc )
                {
                }
                if ( selectedClass == null || selectedParam == null )
                    throw new RuntimeException( "invalid state" );
                System.out.println( name + " waking" );
                selectedClass.getFunk().accept( selectedParam );
                List<ExecLog>   execLog = ExecLog.getLog();
                int             last    = execLog.size() - 1;
                ExecLog         log     = execLog.get( last );
                System.out.println( log );
                logDisplay.update();
            }
        }
    }
    
    private void exec( ActionEvent evt )
    {
        Object   source = evt.getSource();
        if ( !(source instanceof JButton ) )
        {
            String  err = "Invalid object: " + source.getClass().getName();
            throw new RuntimeException( err  );
        }
        String  classText   = getSelection( classGroup ).getText();
        String  paramText   = getSelection( instanceGroup ).getText();
        String  threadText  = getSelection( threadGroup ).getText();
        
        selectedClass = classMap.get( classText );
        selectedParam = instanceMap.get( paramText );
        Thread  thread      = threadMap.get( threadText );
        synchronized ( thread )
        {
            thread.notify();
        }
        logDisplay.update();
    }
    
    private static AbstractButton getSelection( ButtonGroup group )
    {
        AbstractButton  selected    =
            Collections.list( group.getElements() )
            .stream()
            .filter( e -> e.isSelected() )
            .findFirst()
            .orElse( null );
        if ( selected == null )
            throw new RuntimeException( "no button selectd in group" );
        return selected;
    }
    
    @SuppressWarnings("serial")
    private class ContentPane extends JPanel
    {
        public ContentPane()
        {
            super( new BorderLayout() );
            
            add( new ButtonPanel(), BorderLayout.SOUTH );
            
            JPanel  center  = new JPanel( new GridLayout( 1, 3 ) );
            Border  border  = 
                BorderFactory.createEmptyBorder( 10, 15, 10, 15 );
            center.setBorder( border );
            center.add( new ClassGroup() );
            center.add( new InstanceGroup() );
            center.add( new ThreadGroup() );
            add( center, BorderLayout.CENTER );
        }
    }
    
    @SuppressWarnings("serial")
    private class ClassGroup extends JPanel
    {
        public ClassGroup()
        {
            super( new GridLayout( classes.size(), 1 ) );
            for ( String className : classMap.keySet() )
            {
                JRadioButton    button      = new JRadioButton( className );
                classGroup.add( button );
                add( button );
            }
            
            Enumeration<AbstractButton> buttons = classGroup.getElements();
            buttons.nextElement().setSelected( true );
            
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            Border  titled  = 
                BorderFactory.createTitledBorder( border, "Class" );
            setBorder( titled );
        }
    }
    
    @SuppressWarnings("serial")
    private class InstanceGroup extends JPanel
    {
        public InstanceGroup()
        {
            super( new GridLayout( instanceMap.size(), 1 ) );
            for ( Element ele : instanceMap.values() )
            {
                JRadioButton    button  = new JRadioButton( ele.getName() );
                instanceGroup.add( button );
                add( button );
            }
            
            Enumeration<AbstractButton> buttons = instanceGroup.getElements();
            buttons.nextElement().setSelected( true );
            
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            Border  titled  = 
                BorderFactory.createTitledBorder( border, "Object" );
            setBorder( titled );
        }
    }
    
    @SuppressWarnings("serial")
    private class ThreadGroup extends JPanel
    {
        private static final String[]   names   =
        {
            "Thread 1",
            "Thread 2",
            "Thread 3",
            "Thread 4",
        };
        
        public ThreadGroup()
        {
            super( new GridLayout( instanceMap.size(), 1 ) );
            for ( String name : names )
            {
                JRadioButton    button  = new JRadioButton( name );
                threadGroup.add( button );
                add( button );
                
                Thread  thread  = new Thread( () -> doRun(), name );
                threadMap.put( name, thread );
                thread.start();
            }
            
            Enumeration<AbstractButton> buttons = threadGroup.getElements();
            buttons.nextElement().setSelected( true );
            
            Border  border  = BorderFactory.createLineBorder( Color.BLACK );
            Border  titled  = 
                BorderFactory.createTitledBorder( border, "Thread" );
            setBorder( titled );
        }
    }
    
    @SuppressWarnings("serial")
    private class ButtonPanel extends JPanel
    {
        public ButtonPanel()
        {
            JButton exec    = new JButton( "Execute" );
            JButton log     = new JButton( "Show Log" );
            JButton exit    = new JButton( "Exit" );
            
            add( exec );
            add( log );
            add( exit );
            
            exit.addActionListener( e -> System.exit( 0 ) );
            exec.addActionListener( Controller.this::exec );
        }
    }
}
