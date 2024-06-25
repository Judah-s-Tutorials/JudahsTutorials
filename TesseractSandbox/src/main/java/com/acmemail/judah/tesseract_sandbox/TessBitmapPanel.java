package com.acmemail.judah.tesseract_sandbox;

import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class TessBitmapPanel extends JPanel
{
    private static final String dataPathStr     = 
        System.getenv( "TESSDATA_PREFIX" );

    private final JTextArea textArea  = new JTextArea( 20, 35 );
    private final Tesseract tesseract = new Tesseract();

    public TessBitmapPanel()
    {
        tesseract.setDatapath( dataPathStr );
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);

        add( textArea );
    }
    
    public void update( BufferedImage image )
    {
        try
        {
            String  text    = tesseract.doOCR( image );
            textArea.setText( text );
        }
        catch ( TesseractException exc )
        {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(
                null, 
                exc.getMessage(), 
                "Tesseract error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
