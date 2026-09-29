package com.acmemail.judah.battleship.sandbox;

import ch.x28.inscriptis.Inscriptis;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

public class InscriptisDemo
{
    private static final String content =
        "<html>"
        + "<body>"
        + "<h1>Battleship Setup</h1>"
        + "<p>"
        + "This is the setup phase of the game. "
        + "In phase you can set the dimensions of the game grid, "
        + "specify the names of the players "
        + "design a type of ship, "
        + "and establish the types of the ships "
        + "that must be deloyed by each player. "
        + "Each task is performed by "
        + "entering a comma-separated list of a command "
        + "and its arguments, "
        + "one command per line. "
        + "For example, the command: "
        + "</p>" 
        + "<ul><li>dim,20,30</li>"
        + "</ul>"
        + "Establishes a grid with 20 rows and 30 columns. "
        + "Commands are case-insensitive "
        + "and tolerant of whitespace, "
        + "so the following are equivalent commands: "
        + "</body>"
        + "</html>";
//                
//                "",
//                "\tdim,20,30",
//                "",
    public static void main( String[] args )
    {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse( content );

        // 3. Convert the jsoup Document into a standard W3C Document format
        Document w3cDoc = W3CDom.convert(jsoupDoc);

        // 4. Pass the W3C Document to Inscriptis to process layout and subset CSS styles
        Inscriptis inscriptis = new Inscriptis(w3cDoc);
        String plainText = inscriptis.getText();

        // 5. Print the formatted plain text output
        String wrappedText = WordUtils.wrap(plainText, 60, "\n", true);
        System.out.println("--- Converted, Wrapped Text ---");
        System.out.println(wrappedText);
    }
}
