package com.github.samuel281.text.wikipedia.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.text.wikipedia.XmlInputFormat;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Created by samuel281 on 16. 3. 2..
 */
public class XmlInputFormatTest {
    @Test
    public void testSplit() throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration(false);
        conf.set("fs.default.name", "file:///");
        conf.set(XmlInputFormat.START_TAG_KEY,"<page>");
        conf.set(XmlInputFormat.END_TAG_KEY, "</page>");

        File kowikiFile = new File(this.getClass().getResource("/kowiki-latest-pages-articles-sample.xml").getFile());
        Path kowikiPath = new Path(this.getClass().getResource("/kowiki-latest-pages-articles-sample.xml").toURI());
        FileSplit split = new FileSplit(kowikiPath, 0, kowikiFile.length(), (String[]) null);

        InputFormat inputFormat = ReflectionUtils.newInstance(XmlInputFormat.class, conf);
        TaskAttemptContext context = new TaskAttemptContextImpl(conf, new TaskAttemptID());
        RecordReader reader = inputFormat.createRecordReader(split, context);
        reader.initialize(split, context);

        assertTrue(reader.nextKeyValue());
        reader.close();
    }
}
