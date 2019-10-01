package org.commoncrawl.examples.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IndexerReducer extends Reducer<Text, Text, Text, Text> {
	private Text result = new Text();
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		String accumulate = "";
		for (Text v : values) {
			accumulate += (" " + v.toString());
		}
		result.set(accumulate);
		context.write(key, result);
	}
}
