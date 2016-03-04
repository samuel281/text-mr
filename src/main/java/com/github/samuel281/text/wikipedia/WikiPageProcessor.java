package com.github.samuel281.text.wikipedia;

import com.github.samuel281.text.wikipedia.mapreduce.WikiPageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.text.wikipedia.XmlInputFormat;

@Slf4j
public class WikiPageProcessor extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Configuration(), new WikiPageProcessor(), args);
        System.exit(exitCode);
    }

    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            log.error("Not enough arguments.");
            log.error("<input path> <output path> required.");
            return 1;
        }

        Path inputPath = new Path(args[0]);
        Path outputPath = new Path(args[1]);

        Configuration conf = getConf();
        conf.setBoolean(WikiPageMapper.INLINE_KEY, true);
        conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
        conf.set(XmlInputFormat.END_TAG_KEY, "</page>");
        Job job = Job.getInstance(getConf(), WikiPageProcessor.class.getName());
        job.setJarByClass(WikiPageProcessor.class);
        job.setMapperClass(WikiPageMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(XmlInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setNumReduceTasks(0); //no reduce task
        XmlInputFormat.addInputPath(job, new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
