package com.github.samuel281.text.wikipedia.parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WikiPageRevision {
    private Long id;
    @JacksonXmlProperty(localName = "parentid")
    private Long parentId;
    private Date timestamp;
    @JacksonXmlElementWrapper(localName = "contributor")
    private Map contributor;
    private String comment;
    private String model;
    private String format;
    private String text;
    private String sha1;
}
