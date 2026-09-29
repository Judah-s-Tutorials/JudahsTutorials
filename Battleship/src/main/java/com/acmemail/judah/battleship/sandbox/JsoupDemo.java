package com.acmemail.judah.battleship.sandbox;

import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

import ch.x28.inscriptis.Inscriptis;

public class JsoupDemo
{
    public static void main(String[] args) {
        String html = "<html><body style='max-width:10em;'>"
            + "<h1>Hello</h1>"
            + "<p>This is <b>bold</b> text."
            + "</p>"
            + "<ol> "
            + "<li>And some more text...</li>"
            + "<li>And some more text...</li>"
            + "<li>And some more text...</li>"
            + "<li>And some more text...</li>"
            + "<li>And some more text...</li>"
            + "<li>And some more text...</li>"
            + "<li>And some more text...</li>"
            + "</ol>"
            + "</body></html>";
        
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse( html );

        // 3. Convert the jsoup Document into a standard W3C Document format
        Document w3cDoc = W3CDom.convert(jsoupDoc);

        // 4. Pass the W3C Document to Inscriptis to process layout and subset CSS styles
        Inscriptis inscriptis = new Inscriptis(w3cDoc);
        String plainText = inscriptis.getText();

        // 5. Print the formatted plain text output
        String wrappedText = WordUtils.wrap(plainText, 100, "\n", true);
        System.out.println("--- Converted, Wrapped Text ---");
        System.out.println(wrappedText);
    }
}
