package com.github.samuel281.text.wikipedia;

import com.github.samuel281.text.wikipedia.parser.DefaultWikiPageParser;
import com.github.samuel281.text.wikipedia.parser.WikiPage;
import com.github.samuel281.text.wikipedia.parser.WikiPageParser;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class DefaultWikiPageParserTest {
    private String pageFragment;
    @Before
    public void setUp() throws IOException {
        List<String> lines = Lists.newArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/kowiki-page-fragment-sample.xml")));
        while (br.ready()) {
            lines.add(br.readLine());
        }

        pageFragment = Joiner.on("\n").join(lines);
    }

    @Test
    public void parseTest() throws IOException {
        WikiPageParser wikiPageParser = new DefaultWikiPageParser();
        WikiPage page = wikiPageParser.parse(pageFragment);
        assertEquals("지미 카터", page.getTitle());
        assertTrue(StringUtils.isNotBlank(page.getRevision().getText()));
        assertTrue(StringUtils.contains(page.getRevision().getText(), "\n"));
    }

    @Test
    public void parseInlineText() throws IOException {
        WikiPageParser wikiPageParser = new DefaultWikiPageParser(Locale.KOREAN, true);
        WikiPage page = wikiPageParser.parse(pageFragment);
        assertEquals("지미 카터", page.getTitle());
        assertTrue(StringUtils.isNotBlank(page.getRevision().getText()));
        assertFalse(StringUtils.contains(page.getRevision().getText(), "\n"));
    }
}