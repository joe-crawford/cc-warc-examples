# Common Crawl inverted index example #

This repository contains a simple example of how you might build an inverted index of documents from the Common Crawl dataset, by parsing Common Crawl WET files with Apache Hadoop. It's based on the Hadoop examples provided by Common Crawl.

### Usage ###

To build an inverted index, compile the Maven project as a JAR file and pass this as an argument to Hadoop, indicating that the WETIndexer class should be executed.

For example:

```bash
$ /usr/local/hadoop/bin/hadoop jar cc-warc-examples-0.3-SNAPSHOT-jar-with-dependencies.jar org.commoncrawl.examples.mapreduce.WETIndexer
```
In the directory where you run the above command, you need to include the WET files (possibly Gzipped) in a `data/` subdirectory, and you need to include the index terms used to create the index in an `index_terms.txt` file (as one term per line in plain text). The output index will be placed in directory `/tmp/cc`.

---

(From the original cc-warc-examples repository:)

![Common Crawl Logo](http://commoncrawl.org/wp-content/uploads/2016/12/logocommoncrawl.png)

# Common Crawl WARC Examples

This repository contains both wrappers for processing WARC files in Hadoop MapReduce jobs and also Hadoop examples to get you started.

There are three examples for Hadoop processing:

+ [WARC files] HTML tag frequency counter using raw HTTP responses
+ [WAT files] Server response analysis using response metadata
+ [WET files] Classic word count example using extracted text

All three assume initially that the files are stored locally in the data subdirectory but can be trivially modified to pull them down from Common Crawl's Amazon S3 bucket. To acquire the files, you can use [AWS CLI](https://aws.amazon.com/cli/), [S3Cmd](http://s3tools.org/s3cmd) or similar.

    mkdir data
    s3cmd get s3://commoncrawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/warc/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.gz data/
    s3cmd get s3://commoncrawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/wet/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.wet.gz data/
or

    mkdir data
    aws s3 --no-sign-request cp s3://commoncrawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/warc/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.gz data/
    aws s3 --no-sign-request cp s3://commoncrawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/wet/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.wet.gz data/

To build and run:

    mvn package
    <path-to-hadoop>/bin/hadoop jar target/cc-warc-examples-0.2-SNAPSHOT-jar-with-dependencies.jar org.commoncrawl.examples.mapreduce.WETWordCount
    
All three examples place output in the directory `/tmp/cc`.
      
# License

MIT License, as per `LICENSE`
