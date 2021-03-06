package ru.voronoy.webscraper.test;

import org.junit.Test;
import ru.voronoy.webscraper.Document;
import ru.voronoy.webscraper.Parser;
import ru.voronoy.webscraper.Routines;
import ru.voronoy.webscraper.WordCounter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class Tests {
    @Test
    public void getBodyTextTest() throws Exception {
        String text = "Body text";
        String htmlText = "<html><body>" + text + "</body></html>";
        Document document = new Parser().parse(htmlText);
        assertEquals(text, document.getFullText());
    }

    @Test
    public void passNullToParserTest() throws Exception {
        try {
            new Parser().parse(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void countWordTest() throws Exception {
        FileReader reader = new FileReader("assets/cnnTest.html");
        char[] buffer = new char[1024];
        StringBuilder builder = new StringBuilder();
        while (true) {
            int read = reader.read(buffer, 0, buffer.length);
            if (read < 0)
                break;
            builder.append(buffer, 0, read);
        }
        Document document = new Parser().parse(builder.toString());
        WordCounter counter = new WordCounter(document);
        assertEquals(4, counter.countWord("testword"));
    }

    @Test
    public void passNullToCountWordsTest() {
        try {
            new WordCounter(new Document()).countWord(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void passNullToWordCountConstructorTest() {
        try {
            new WordCounter(null);
        } catch (IllegalArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void countWholeAmountOfSymbols() throws Exception {
        String text = "Body text";
        String htmlText = "<html><head>" + "Head text" + " </head><body>" + text + "</body></html>";
        Document document = new Parser().parse(htmlText);
        assertEquals(16, document.getCharactersCount());
    }

    @Test
    public void prepareOneSentenceTest() {
        String text = "Some text";
        List<String> sentences = Routines.prepareSentences(text);
        assertEquals(1, sentences.size());
        assertEquals(text, sentences.get(0));
    }

    @Test
    public void prepareTwoSentencesWithDotTest() {
        String sentence1 = "Some text";
        String sentence2 = "The rest part";
        String text = sentence1 + ". " + sentence2;
        List<String> sentences = Routines.prepareSentences(text);
        assertEquals(2, sentences.size());
        assertEquals(sentence1, sentences.get(0));
        assertEquals(sentence2, sentences.get(1));
    }

    @Test
    public void prepareTwoSentencesWithExclamationTest() {
        String sentence1 = "Some text";
        String sentence2 = "The rest part";
        String text = sentence1 + "! " + sentence2;
        List<String> sentences = Routines.prepareSentences(text);
        assertEquals(2, sentences.size());
        assertEquals(sentence1, sentences.get(0));
        assertEquals(sentence2, sentences.get(1));
    }

    @Test
    public void prepareTwoSentencesWithQuestionTest() {
        String sentence1 = "Some text";
        String sentence2 = "The rest part";
        String text = sentence1 + "? " + sentence2;
        List<String> sentences = Routines.prepareSentences(text);
        assertEquals(2, sentences.size());
        assertEquals(sentence1, sentences.get(0));
        assertEquals(sentence2, sentences.get(1));
    }

    @Test
    public void fillUrlsListTest() throws Exception {
        String[] urlStrings = new String[] {"url1", "url2", "url3"};
        String separator = System.getProperty("line.separator");
        File file = new File(System.getProperty("user.home"), "fillUrlTest");
        if (file.exists())
            file.delete();
        file.createNewFile();
        try (FileWriter writer = new FileWriter(file, true)){
            Arrays.stream(urlStrings).forEach(s -> {
                try {
                    writer.append(s);
                    writer.append(separator);
                } catch (IOException e) {
                    e.printStackTrace();
                    fail();
                }
            });
        }
        List<String> urls = new ArrayList<>();
        Routines.retrieveUrlsFromFile(urls, file);
        file.delete();
        assertArrayEquals(urlStrings, urls.toArray());
    }
}
