package org.commoncrawl.examples.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.commoncrawl.warc.WARCFileInputFormat;

/**
 * Simple inverted indexer for Common Crawl WET (extracted plain text) files.
 *
 * @author Joe Crawford (joe-crawford), based on the WETWordCount example by Stephen Merity (Smerity)
 */
public class WETIndexer extends Configured implements Tool {
	private static final Logger LOG = Logger.getLogger(WETIndexer.class);

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new WETIndexer(), args);
		System.exit(res);
	}

	@Override
	public int run(String[] arg0) throws Exception {
		Configuration conf = getConf();
		Job job = new Job(conf);
		job.setJarByClass(WETIndexer.class);
		job.setNumReduceTasks(1);
		
		String inputPath = "data/*.warc.wet.gz";
		LOG.info("Input path: " + inputPath);
		FileInputFormat.addInputPath(job, new Path(inputPath));
		
		String outputPath = "/tmp/cc/";
		FileSystem fs = FileSystem.newInstance(conf);
		if (fs.exists(new Path(outputPath))) {
			fs.delete(new Path(outputPath), true);
		}
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		
		job.setInputFormatClass(WARCFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    job.setMapperClass(IndexerMap.IndexerMapper.class);
	    job.setReducerClass(IndexerReducer.class);
		
	    if (job.waitForCompletion(true)) {
	    	return 0;
	    } else {
	    	return 1;
	    }
	}
}
