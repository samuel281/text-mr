package com.github.samuel281.text.wikipedia.mapreduce;

import com.github.samuel281.text.wikipedia.parser.WikiPageParser;
import com.github.samuel281.text.wikipedia.parser.DefaultWikiPageParser;
import com.github.samuel281.text.wikipedia.parser.WikiPage;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Locale;

/**
 * The type Wiki page mapper.
 * @author samuel281
 *
 * <p>Mapping wiki page articles xml to id, namespace, title, comment and sanitized text delimited by DELIM_KEY</p>
 */
public class WikiPageMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
    public static final String INLINE_KEY = "com.github.samuel281.text.wikipedia.inline";
    public static final String LOCALE_KEY = "com.github.samuel281.text.wikipedia.locale";
    public static final String DELIM_KEY = "com.github.samuel281.text.wikipedia.delimiter";

    private final Locale DEFAULT_LOCALE = Locale.KOREAN;
    private final boolean DEFAULT_INLINE = true;
    private final String DEFAULT_DELIM = "\t";

    private WikiPageParser wikiPageParser;
    private String delimiter;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        boolean isInlineText = context.getConfiguration().getBoolean(INLINE_KEY, DEFAULT_INLINE);
        Locale locale = Locale.forLanguageTag(context.getConfiguration().get(LOCALE_KEY, DEFAULT_LOCALE.getDisplayName()));
        delimiter = context.getConfiguration().get(DELIM_KEY, DEFAULT_DELIM);
        wikiPageParser = new DefaultWikiPageParser(locale, isInlineText);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        WikiPage page = null;
        try {
            page = wikiPageParser.parse(value.toString());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.err.println(value.toString());
        }

        if (page == null) {
            return;
        }

        context.write(NullWritable.get(), new Text(toTuple(page)));
    }

    private String toTuple(WikiPage page) {
        StringBuffer sb = new StringBuffer();
        sb.append(page.getId());
        sb.append(delimiter);
        sb.append(page.getNamespace());
        sb.append(delimiter);
        sb.append(page.getTitle());
        sb.append(delimiter);
        sb.append(page.getRevision().getComment());
        sb.append(delimiter);
        sb.append(page.getRevision().getText());
        return sb.toString();
    }
}
