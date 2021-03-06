package ru.voronoy.webscraper;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Web page parser
 */
public class Parser {

    private static final HTMLEditorKit.Parser parser = new ParserDelegator();

    public Document parse(String text) throws IOException {
        Routines.checkArgument(text);
        Reader reader = new StringReader(text);
        Callback callback = new Callback();
        parser.parse(reader, callback, true);
        return callback.getDocument();
    }

    private static class Callback extends HTMLEditorKit.ParserCallback {
        private final Document document = new Document();

        @Override
        public void handleText(char[] data, int pos) {
            super.handleText(data, pos);
            document.addSentences(Routines.prepareSentences(new String(data)));
        }

        private Document getDocument() {
            return document;
        }
    }
}
