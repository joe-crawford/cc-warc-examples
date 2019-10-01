package org.commoncrawl.examples.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;

public class IndexerMap {
	private static final Logger LOG = Logger.getLogger(IndexerMap.class);
	protected static enum MAPPERCOUNTER {
		RECORDS_IN,
		EMPTY_PAGE_TEXT,
		EXCEPTIONS,
		NON_PLAIN_TEXT
	}
	
	private static String[] indexTerms = IndexTermsUtils.getTerms("index_terms.txt");

	protected static class IndexerMapper extends Mapper<Text, ArchiveReader, Text, Text> {
		private StringTokenizer tokenizer;
		private Text outKey = new Text();
		private Text outVal = new Text();

		@Override
		public void map(Text key, ArchiveReader value, Context context) throws IOException {
			for (ArchiveRecord r : value) {
				HashMap<String, Integer> counts = new HashMap<String, Integer>();
				for (String w : indexTerms) {
					counts.put(w, 0);
				}
				try {
					ArchiveRecordHeader header = r.getHeader();
					if (header.getMimetype().equals("text/plain")) {
						context.getCounter(MAPPERCOUNTER.RECORDS_IN).increment(1);
						LOG.debug(r.getHeader().getUrl() + " -- " + r.available());
						// Convenience function that reads the full message into a raw byte array
						byte[] rawData = IOUtils.toByteArray(r, r.available());
						String content = new String(rawData);
						// Grab each word from the document
						tokenizer = new StringTokenizer(content);
						if (!tokenizer.hasMoreTokens()) {
							context.getCounter(MAPPERCOUNTER.EMPTY_PAGE_TEXT).increment(1);
						} else {
							while (tokenizer.hasMoreTokens()) {
								String token = tokenizer.nextToken();
								boolean match = Arrays.stream(indexTerms).anyMatch(token::equals);
								if (match) {
									counts.put(token, counts.get(token)+1);
								}
							}
						}
						for (String w : indexTerms) {
							int c = counts.get(w);
							if (c>0) {
								outKey.set(w);
								outVal.set(c+","+header.getUrl());
								context.write(outKey, outVal);
							}
						}
					} else {
						context.getCounter(MAPPERCOUNTER.NON_PLAIN_TEXT).increment(1);
					}
				}
				catch (Exception ex) {
					LOG.error("Caught Exception", ex);
					context.getCounter(MAPPERCOUNTER.EXCEPTIONS).increment(1);
				}
			}
		}
	}
}
