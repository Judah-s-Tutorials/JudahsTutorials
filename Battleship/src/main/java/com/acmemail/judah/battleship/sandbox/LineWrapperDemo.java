package com.acmemail.judah.battleship.sandbox;

import java.util.List;
import java.util.function.Supplier;

import com.acmemail.judah.battleship.util.LineWrapper;

public class LineWrapperDemo
{
    private static String   LINE_SEP    = System.lineSeparator();
    public static void main(String[] args)
    {
        List<Supplier<String>>  allDemos    =
            List.of(
                () -> twoStanzas(),
                () -> oneLongParagraph(),
                () -> twoLongParagraphs(),
                () -> longWords()
            );
        allDemos.forEach(s -> {
            System.out.println( "********** Begin" );
            System.out.println( s.get() );
            System.out.println( "********** End" );
        });

    }

    private static String twoStanzas()
    {
        List<String>    lines   =
            List.of(
                "Two stanzas of a poem, even numbered lines indented:",
                "",
                "The sun was shining on the sea,",
                "\tShining with all his might:",
                "He did his very best to make",
                "\tThe billows smooth and bright —",
                "And this was odd, because it was",
                "\tThe middle of the night.",
                "",
                "The moon was shining sulkily,",
                "\tBecause she thought the sun",
                "..."
            );
        String          result      = doWrap( 40, lines );
        return result;
    }
    
    private static String oneLongParagraph()
    {
        String          graph   =
            "One long paragraph: "
            + "The unanimous Declaration of the thirteen united States of "
            + "America, When in the Course of human events, it becomes "
            + "necessary for one people to dissolve the political bands which"
            + " have connected them with another, and to assume among the "
            + "powers of the earth, the separate and equal station to which the "
            + "Laws of Nature and of Nature's God entitle them, a decent "
            + "   respect to the opinions of mankind requires that they should"
            + " declare    the causes which impel them to the separation.";
            
        List<String>    lines   = List.of( graph );
        String          result  = doWrap( 40, lines );
        return result;
    }
    
    private static String twoLongParagraphs()
    {
        String          graph1  =
            "There is no strife, no prejudice, no national conflict in outer"
            + " space as yet. Its hazards are hostile to us all. Its conquest"
            + " deserves the best of all mankind, and its opportunity for "
            + "peaceful cooperation may never come again. But why, some say, "
            + "the moon? Why choose this as our goal? And they may well ask"
            + " why climb the highest mountain? Why, 35 years ago, fly the "
            + "Atlantic? Why does Rice play Texas?";
        String          graph2  =
            "We choose to go to the moon. We choose to go to the moon in this"
            + " decade and do the other things, not because they are easy, but"
            + " because they are hard, because that goal will serve to organize "
            + "and measure the best of our energies and skills, because that"
            + " challenge is one that we are willing to accept, one we are "
            + "unwilling to postpone, and one which we intend to win, and the "
            + "others, too."; 
            
        List<String>    lines   =
            List.of( 
                "Two long paragraphs",
                "",
                graph1,
                "",
                graph2
            );
        String          result      = doWrap( 40, lines );
        return result;
    }
    
    
    private static String longWords()
    {
        String          longWord        =
            "0123456789"
            + "-123456789"
            + "-123456789"
            +"-123456789"
            +"-123456789";
        int             lineLimit       = longWord.length() - 5;
            
        List<String>    lines   =
            List.of( 
                "Long word, middle of line: " + longWord,
                "Long word, start of line: ",
                longWord + " (continuation of line)",
                "Really long words ",
                longWord + longWord + longWord + " (continuation of line)",
                "Consecutive long words: ",
                longWord + " " + longWord + " " + longWord 
                    + " (continuation of line)"
            );
        String          result      = doWrap( lineLimit, lines );
        return result;
    }

    
    private static String doWrap( int lineLen, List<String> lines )
    {
        LineWrapper     wrapper     = new LineWrapper( lineLen, lines );
        List<String>    brokenList  = wrapper.getWrappedList();
        String          result      = String.join( LINE_SEP, brokenList );
        return result;
    }
}
