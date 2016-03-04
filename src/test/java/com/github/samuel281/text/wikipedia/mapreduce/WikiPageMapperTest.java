package com.github.samuel281.text.wikipedia.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.text.wikipedia.XmlInputFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WikiPageMapperTest {
    MapDriver<LongWritable, Text, NullWritable, Text> mapDriver;
    RecordReader reader;

    @Before
    public void setUp() throws URISyntaxException, IOException, InterruptedException {
        mapDriver = MapDriver.newMapDriver(new WikiPageMapper());

        Configuration conf = new Configuration(false);
        conf.set("fs.default.name", "file:///");
        conf.set(XmlInputFormat.START_TAG_KEY,"<page>");
        conf.set(XmlInputFormat.END_TAG_KEY, "</page>");

        File kowikiFile = new File(this.getClass().getResource("/kowiki-latest-pages-articles-sample.xml").getFile());
        Path kowikiPath = new Path(this.getClass().getResource("/kowiki-latest-pages-articles-sample.xml").toURI());
        FileSplit split = new FileSplit(kowikiPath, 0, kowikiFile.length(), (String[]) null);

        InputFormat inputFormat = ReflectionUtils.newInstance(XmlInputFormat.class, conf);
        TaskAttemptContext context = new TaskAttemptContextImpl(conf, new TaskAttemptID());
        reader = inputFormat.createRecordReader(split, context);
        reader.initialize(split, context);
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
    }

    @Test
    public void map() throws IOException, InterruptedException {
        while (reader.nextKeyValue()) {
            LongWritable key = (LongWritable) reader.getCurrentKey();
            Text value = (Text) reader.getCurrentValue();
            mapDriver.addInput(key, value);

        }

        List<Pair<NullWritable, Text>> mapOutput =  mapDriver.run();
        assertEquals(4, mapOutput.size());
    }

}