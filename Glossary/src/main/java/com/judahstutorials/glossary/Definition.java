package com.judahstutorials.glossary;

import static com.judahstutorials.glossary.GConstants.*;
import static com.judahstutorials.glossary.GConstants.SLUG;
import static com.judahstutorials.glossary.GConstants.TERM;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Definition
{
    private final String            term;
    private final Integer           seqNum;
    private final String            slug;
    private final String            description;
    private final List<String>  seeAlso     = new ArrayList<>();

    public Definition( Element def )
    {
        term = getTerm( def );
        seqNum = getSeqNum( def );
        slug = getSlug( def );
        description = getDescription( def );
        getSeeAlso( def );
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
        bldr.append( ", {" ).append( seeAlso.toString() ).append( "}" );
        return bldr.toString();
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
//        if ( node.getNodeType() != Node.TEXT_NODE )
//            throw new FormatException( "text node not found" );
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
//            if ( node.getNodeType() != Node.TEXT_NODE )
//                throw new FormatException( "text node not found" );
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
//            if ( node.getNodeType() != Node.TEXT_NODE )
//                throw new FormatException( "text node not found" );
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
//        if ( node.getNodeType() != Node.TEXT_NODE )
//            throw new FormatException( "text node not found" );
        String      desc        = node.getTextContent();
        if ( desc == null || desc.isEmpty() )
            throw new FormatException( "invalid description \"" + desc + "\"" );
        return desc;
    }
    
    private void getSeeAlso( Element def )
    {
        NodeList    nodeList    = def.getElementsByTagName( SEE_ALSO );
        int         numNodes    = nodeList.getLength();
        for ( int inx = 0 ; inx < numNodes ; ++inx )
        {
            Node        node        = nodeList.item( inx );
//            if ( node.getNodeType() != Node.TEXT_NODE )
//                throw new FormatException( "text node not found" );
            String      seeAlso     = node.getTextContent();
            if ( seeAlso == null || seeAlso.isEmpty() )
                throw new FormatException( "invalid seeAlso \"" + seeAlso + "\"" );
            this.seeAlso.add( seeAlso );
        }
    }
}
