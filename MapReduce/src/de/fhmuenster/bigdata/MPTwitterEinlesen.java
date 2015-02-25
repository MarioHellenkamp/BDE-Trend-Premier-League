package de.fhmuenster.bigdata;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MPTwitterEinlesen extends Configured implements Tool {

   static public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
	  
	   final static private String[] HASHTAGS = {"#AFC","#AVFC","#Clarets","#CFC","#CPFC","#EFC","#HCAFC","#ICFC","#LFC","#MCFC","#MUFC","#NUFC",
			"#QPR","#SAINTSFC","#SCFC","#SAFC","#SWANS","#THFC","#WBAFC","#WHUFC"};
	   
      final private static LongWritable ONE = new LongWritable(1);
      private Text tokenValue = new Text();

      @Override
      protected void map(LongWritable offset, Text text, Context context) throws IOException, InterruptedException {
         Integer minutes = Integer.parseInt(text.toString());
      }
      
  	public void startTwitterStream(int minutes, final Context context) throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setOAuthConsumerKey("9vc2NZtxSjzUYWjqI8o1KexMx");
	    cb.setOAuthConsumerSecret("34Qg019UIBEEkLgfAcxR8F17TGEbjAV1SzXGzHuq2iAqIyJ4mj");
	    cb.setOAuthAccessToken("26720675-H9m1i0VHeb2vyhliKrVXEeNKPbaQLxly7H7wb60tN");
	    cb.setOAuthAccessTokenSecret("mwmrxtBhbGOzyEo287wMiMcriJyOKMHDa1sBjTfExD9Zm");
	    //cb.setJSONStoreEnabled(true);
	    cb.setIncludeEntitiesEnabled(true);
	    
	    StatusListener listener = createListeners(context);
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    FilterQuery query = new FilterQuery().track(HASHTAGS);
	    twitterStream.addListener(listener);
	    twitterStream.filter(query);
	    
	    try { Thread.sleep(minutes * 60 * 1000);} catch (Exception e) {};
	}
	
	private StatusListener createListeners(final Context context) {
		return new StatusListener() {
	        // The onStatus method is executed every time a new tweet comes in.
	        public void onStatus(Status status) {
	        	String tweet = status.getText().replace("\n", "").replace("\r", "");
	        	String line = status.getCreatedAt().getTime() + "|" + status.getUser().getScreenName() + "|" + tweet + "\n";

				//sb.append(line);
	        }
			@Override
			public void onException(Exception arg0) {}
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}
			@Override
			public void onScrubGeo(long arg0, long arg1) {}
			@Override
			public void onStallWarning(StallWarning arg0) {}
			@Override
			public void onTrackLimitationNotice(int arg0) {}
		};
	}
   }

   static public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
      private LongWritable total = new LongWritable();

      @Override
      protected void reduce(Text token, Iterable<LongWritable> counts, Context context)
            throws IOException, InterruptedException {
         long n = 0;
         for (LongWritable count : counts)
            n += count.get();
         total.set(n);
         context.write(token, total);
      }
   }

   public int run(String[] args) throws Exception {
      Configuration configuration = getConf();

      Job job = new Job(configuration, "Word Count");
      job.setJarByClass(MPTwitterEinlesen.class);

      job.setMapperClass(WordCountMapper.class);
      job.setCombinerClass(WordCountReducer.class);
      job.setReducerClass(WordCountReducer.class);

      job.setInputFormatClass(TextInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);

      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(LongWritable.class);

      return job.waitForCompletion(true) ? 0 : -1;
   }

   public static void main(String[] args) throws Exception {
      System.exit(ToolRunner.run(new MPTwitterEinlesen(), args));
   }
}
