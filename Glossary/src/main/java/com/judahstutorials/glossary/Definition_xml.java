package com.judahstutorials.glossary;

import static com.judahstutorials.glossary.GConstants.DESCRIPTION;
import static com.judahstutorials.glossary.GConstants.SEE_ALSO;
import static com.judahstutorials.glossary.GConstants.SEQ_NUM;
import static com.judahstutorials.glossary.GConstants.SLUG;
import static com.judahstutorials.glossary.GConstants.TERM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Definition_xml
{
    private static final String endl    = System.lineSeparator();
    
    private String      term;
    private Integer     seqNum;
    private String      slug;
    private String      description;

    private Integer ident   = null;
    
    public Definition_xml()
    {
        
    }

    public Definition_xml( Element def )
    {
        term = getTerm( def );
        seqNum = getSeqNum( def );
        slug = getSlug( def );
        description = getDescription( def );
        getSeeAlso( def );
    }
    
    public String getTerm()
    {
        String  term    = this.term;
        if ( seqNum != null )
            term += "(" + seqNum + ")";
        return term;
    }

    public Integer getSeqNum()
    {
        return seqNum;
    }

    public String getSlug()
    {
        return slug;
    }

    public String getDescription()
    {
        return description;
    }

    public String toString()
    {
        StringBuilder   bldr        = new StringBuilder();
        String          temp        = description.trim();
        int             descLen     = temp.length();
        String          descBegin   = "";
        String          descEnd     = "";
        String          desc        = null;
        if ( descLen <= 10 )
            desc = description;
        else
        {
            descBegin = temp.substring( 0, 5 );
            descEnd = temp.substring( descLen - 5, descLen );
            desc = descBegin + "..." + descEnd;
        }
        bldr.append( "term=" ).append( term );
        bldr.append( ",seqNum=" ).append( seqNum );
        bldr.append( ",slug=" ).append( slug );
        bldr.append( "," ).append( desc );
        return bldr.toString();
    }
    
    public Integer getID()
    {
        return ident;
    }

    private String getTerm( Element def )
    {
        NodeList    nodeList    = def.getElementsByTagName( TERM );
        int         numNodes    = nodeList.getLength();
        if ( numNodes == 0 )
            throw new FormatException( "term element not found" );
        if ( numNodes > 1 )
            throw new FormatException( "multiple term elements founc" );
        Node        node        = nodeList.item( 0 );
        String      term        = node.getTextContent();
        if ( term == null || term.isEmpty() )
            throw new FormatException( "invalid term \"" + term + "\"" );
        return term;
    }
    
    private Integer getSeqNum( Element def )
    {
        NodeList    nodeList    = def.getElementsByTagName( SEQ_NUM );
        int         numNodes    = nodeList.getLength();
        Integer     seqNum      = null;
        if ( numNodes > 1 )
            throw new FormatException( "multiple term elements found" );
        if ( numNodes == 1 )
        {
            Node        node        = nodeList.item( 0 );
            String      text        = node.getTextContent();
            if ( text == null || text.isEmpty() )
            {
                String  msg = "invalid sequence number \"" + text + "\"";
                throw new FormatException( msg );
            }
            try
            {
                seqNum = Integer.parseInt( text );
            }
            catch ( NumberFormatException exc )
            {
                String  msg = "invalid sequence number \"" + text + "\"";
                throw new FormatException( msg );
            }
        }
        return seqNum;
    }
    
    private String getSlug( Element def )
    {
        NodeList    nodeList    = def.getElementsByTagName( SLUG );
        int         numNodes    = nodeList.getLength();
        String      slug        = null;
        if ( numNodes > 1 )
            throw new FormatException( "multiple slug elements found" );
        if ( numNodes == 1 )
        {
            Node        node        = nodeList.item( 0 );
            slug = node.getTextContent();
            if ( slug == null || slug.isEmpty() )
            {
                String  msg = "invalid slug \"" + slug + "\"";
                throw new FormatException( msg );
            }
        }
        return slug;
    }

    private String getDescription( Element def )
    {
        NodeList    nodeList    = def.getElementsByTagName( DESCRIPTION );
        int         numNodes    = nodeList.getLength();
        if ( numNodes == 0 )
            throw new FormatException( "description element not found" );
        if ( numNodes > 1 )
            throw new FormatException( "multiple description elements found" );
        Node        node        = nodeList.item( 0 );
        String      desc        = node.getTextContent();
        if ( desc == null || desc.isEmpty() )
            throw new FormatException( "invalid description \"" + desc + "\"" );
        String  fullDesc    = getDescription( desc );
        
        return fullDesc;
    }
    
    private String getDescription( String input )
    {
        String  output  = input;
        String  test    = input.trim();
        if ( test.startsWith( "@" ) )
        {
            File    file    = new File( input.substring( 1 ) );
            output = getDescription( file );
        }
        return output;
    }
    
    private String getDescription( File file )
    {
        String  desc    = null;
        try ( 
            FileReader  fReader = new FileReader( file );
            BufferedReader bReader = new BufferedReader( fReader );
        )
        {
            StringBuilder   bldr    =
                bReader.lines()
                .map( l -> l + endl )
                .collect(
                    StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append
                );
            desc = bldr.toString();
        }
        catch ( IOException exc )
        {
            String  msg = "Failed to read " + file.getName();
            throw new FormatException( msg, exc );
        }
        
        return desc;
    }
    
    private void getSeeAlso( Element def )
    {
        NodeList    nodeList    = def.getElementsByTagName( SEE_ALSO );
        int         numNodes    = nodeList.getLength();
        for ( int inx = 0 ; inx < numNodes ; ++inx )
        {
            Node        node        = nodeList.item( inx );
            String      seeAlso     = node.getTextContent();
            if ( seeAlso == null || seeAlso.isEmpty() )
                throw new FormatException( "invalid seeAlso \"" + seeAlso + "\"" );
        }
    }
}
