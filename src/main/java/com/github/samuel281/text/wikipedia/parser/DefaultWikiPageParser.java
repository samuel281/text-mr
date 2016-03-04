package com.github.samuel281.text.wikipedia.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * The type Default wiki page parser.
 *
 * <p>Parsing page article xml fragment to return @link com.github.samuel281.text.wikipedia.parser.WikiPage</p>
 */
public class DefaultWikiPageParser implements WikiPageParser {
    private final boolean inline;
    private final PlainTextConverter plainTextConverter;
    private final WikiModel wikiModel;
    private final ObjectMapper xmlMapper = new XmlMapper();
    private static final String toSanitizeRegex = "(&lt;.*&gt;|\\{\\{.*\\}\\}|&quot;)";


    public DefaultWikiPageParser() {
        this(Locale.KOREAN, false);
    }

    /**
     * Instantiates a new Default wiki page parser.
     *
     * @param locale the locale
     * @param inline returned text field will be a single line.
     */
    public DefaultWikiPageParser(Locale locale, boolean inline) {
        this.inline = inline;
        plainTextConverter = new PlainTextConverter();
        wikiModel = new WikiModel("/${image}", "/${title}");
    }

    public WikiPage parse(String pageFragment) throws IOException {
        WikiPage page = xmlMapper.readValue(pageFragment, WikiPage.class);

        if (StringUtils.isNotBlank(page.getRevision().getText())) {
            sanitizeText(page);
            if (inline) {
                inlineText(page);
            }
        }
        return page;
    }

    private void inlineText(WikiPage page) {
        String inlined = page.getRevision().getText().replaceAll("^\\n$","").replaceAll("\\n", " ");
        page.getRevision().setText(inlined);
    }

    private void sanitizeText(WikiPage page) {
        String sanitized = wikiModel.render(plainTextConverter,page.getRevision().getText(), false).replaceAll(toSanitizeRegex, "");
        page.getRevision().setText(sanitized);
    }
}
