package com.github.samuel281.text.wikipedia.parser;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JacksonXmlRootElement(localName = "page")
public class WikiPage {
    private String title;
    @JacksonXmlProperty(localName = "ns")
    private int namespace;
    private long id;
    @JacksonXmlProperty(localName = "revision")
    private WikiPageRevision revision;
}
