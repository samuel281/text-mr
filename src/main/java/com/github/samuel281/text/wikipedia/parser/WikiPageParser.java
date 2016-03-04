package com.github.samuel281.text.wikipedia.parser;

import java.io.IOException;

public interface WikiPageParser {
    WikiPage parse(String pageFragment) throws IOException;
}
