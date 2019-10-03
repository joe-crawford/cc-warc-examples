package org.commoncrawl.examples.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IndexerReducer extends Reducer<Text, Text, Text, Text> {
	private Text result = new Text();
	
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		for (Text v : values) {
			sb.append(" " + v.toString());
		}
		result.set(sb.toString());
		context.write(key, result);
	}
}
