package de.fhmuenster.bigdata;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import uk.ac.wlv.sentistrength.SentiStrength;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.fhmuenster.bigdata.jongo.StatsTeam;
import de.fhmuenster.bigdata.jongo.StatsTweets;
import de.fhmuenster.bigdata.jongo.Tweet;



public class TwitterMatchCount extends Configured implements Tool {
	
	final static private String[] HASHTAGS = {"#AFC","#AVFC","#Clarets","#CFC","#CPFC","#EFC","#HCAFC","#ICFC","#LFC","#MCFC","#MUFC","#NUFC",
		"#QPR","#SAINTSFC","#SCFC","#SAFC","#SWANS","#THFC","#WBAFC","#WHUFC"};
	
	final static private String MATCHER = "!$!";

	static public class TwitterMatchCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		final private static LongWritable ONE = new LongWritable(1);
		private Text tokenValue = new Text();
		private SentiStrength sentiStrength;
		
		private DB db;
		private Jongo jongo;
		private long mapTimestamp;
		private MongoCollection tweetsCollection;
		
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			System.out.println(System.getProperty("user.home"));
			sentiStrength = new SentiStrength();
			String ssthInitialisation[] = {"sentidata", System.getProperty("user.home") + "/SentiStrengthData/", "explain"};
			sentiStrength.initialise(ssthInitialisation); //Initialise
			
			mapTimestamp = System.currentTimeMillis();
			db = new MongoClient().getDB("matches");
			jongo = new Jongo(db);
			tweetsCollection = jongo.getCollection("tweets");
		}

		@Override
		protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
			Tweet tweet = new Tweet();
			String[] split = line.toString().split("[|]", 3);
			try {
				tweet.setMapTimestamp(new Date(Long.parseLong(split[0])));
				tweet.setUser(split[1]);
				tweet.setTweet(split[2]);
				tweet.setMapTimestamp(new Date(mapTimestamp));
			} catch (Exception e) {
				return;
			}
	
			String oneHashtag = "";
			TreeSet<String> tags = new TreeSet<String>();
			for (String hashtag: HASHTAGS) {
				if (tweet.getTweet().toUpperCase().contains(hashtag)) {
					tokenValue.set(MATCHER + "COUNT:" + hashtag);
					context.write(tokenValue, ONE);
					tags.add(hashtag);
					oneHashtag = hashtag;
				}
			}
			
			if (tags.size() == 2) {
				String hashtags = "";
				for (String tag: tags)
					hashtags+= tag + " ";
				tokenValue.set(MATCHER + "COUNT-PARTIE:" + hashtags);
				context.write(tokenValue, ONE);
			} else if (tags.size() == 1) {
				// Einmal zählen für Vorkommen ohne Kombination
				tokenValue.set(MATCHER + "COUNT-ONLY:" + oneHashtag);
				context.write(tokenValue, ONE);
				
				String sentiText = sentiStrength.computeSentimentScores(tweet.getTweet().toString());
				Pattern regex = Pattern.compile("\\[sentence: (.*?)\\]");
				Matcher matcher = regex.matcher(sentiText);
				tweet.setSentiString(sentiText);
				int pos = 0, neg = 0, ii = 0;
				while (matcher.find()) {
					ii++;
					String[] sentiScores = matcher.group(1).split(",");
					pos+=Integer.parseInt(sentiScores[0]);
					neg+=Integer.parseInt(sentiScores[1]);
				}
				tweet.setPosSenti(pos-ii);
				tweet.setNegSenti(neg+ii);
				int result = pos + neg;
				if (result != 0) {
					tokenValue.set(MATCHER + "SUM-SENTI:" + oneHashtag);
					context.write(tokenValue, new LongWritable(result));
					
					if (result > 0)
						tokenValue.set(MATCHER + "COUNT-SENTIPOS:" + oneHashtag);
					if (result < 0)
						tokenValue.set(MATCHER + "COUNT-SENTINEG:" + oneHashtag);
					context.write(tokenValue, ONE);
				}
			}
		
			countWords(tweet.getTweet().toString(), tags, oneHashtag, context);
			
			tokenValue.set(MATCHER + "COUNT-ALL:");
			context.write(tokenValue, ONE);
			tweetsCollection.save(tweet);
		}
		
		private void countWords(String text, Set<String> tags, String oneHashtag, Context context) throws IOException, InterruptedException {
			if (tags.size() == 1) {
				for (String token : text.toString().split("\\s+")) {
					tokenValue.set(token);
					context.write(tokenValue, ONE);
					tokenValue.set(MATCHER + "WORD:" + oneHashtag + ":" + token);
					context.write(tokenValue, ONE);
				}
			} else {
				for (String token : text.toString().split("\\s+")) {
					tokenValue.set(token);
					context.write(tokenValue, ONE);
				}
			}
		}
		
		@Override
		protected void cleanup(Context context) {
			// Nothing
		}

	}

	static public class TwitterMatchCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
		
		private DB db;
		private Jongo jongo;
		private MongoCollection statAlltimeCollection;
				
		private long reduceTimestamp;

		//private Map<String, StatsMatch> tagToStatsMatch = new HashMap<String, StatsMatch>();
		private Map<String, StatsTeam> tagToStatsTeam = new HashMap<String, StatsTeam>();
		private StatsTweets statsTweets = new StatsTweets();
		private StatsTweets statsTweetsAlltime;
		
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			reduceTimestamp = System.currentTimeMillis();
			db = new MongoClient().getDB("matches");
			jongo = new Jongo(db);
			
			statAlltimeCollection = jongo.getCollection("statAlltime");
			statsTweetsAlltime = statAlltimeCollection.findOne().as(StatsTweets.class);
			
			if (statsTweetsAlltime == null) {
				statsTweetsAlltime = new StatsTweets();
				statsTweetsAlltime.setAlltime(true);
			}
				
			for (String teamHashtag: HASHTAGS) {
				StatsTeam statT = new StatsTeam();
				statT.setHashtag(teamHashtag);
				statT.setEnddatum(new Date(reduceTimestamp));
				tagToStatsTeam.put(teamHashtag, statT);
			}
			statsTweets.setEnddatum(new Date(reduceTimestamp));
		}
	
		@Override
		protected void reduce(Text token, Iterable<LongWritable> counts, Context context)
				throws IOException, InterruptedException {
			long n = 0;
			for (LongWritable count : counts)
				n += count.get();
			setKeyValue(token.toString(), n);
			context.write(token, new LongWritable(n));
		}
		
		private void setKeyValue(String key, long value) {
			try {
				if (key.length() > 2 && key.substring(0, 3).equals(MATCHER)) {
					String keyMP = key.substring(3);
					String[] split = keyMP.split(":");
					
					if (split[0].equals("COUNT-ALL")) {
						statsTweets.setTweets(value);
						statsTweetsAlltime.setTweets(statsTweetsAlltime.getTweets() + value);
						return;
					}
					
					String[] hashtags = split[1].split(" ");
					
					if (split[0].equals("COUNT") && hashtags.length == 1)
						tagToStatsTeam.get(hashtags[0]).setTweetsCount(value);
					
					else if (split[0].equals("COUNT-ONLY"))
						tagToStatsTeam.get(hashtags[0]).setTweetsCountOnly(value);
					
					else if (split[0].equals("SUM-SENTI"))
						tagToStatsTeam.get(hashtags[0]).setSentiSum(value);
					
					else if (split[0].equals("COUNT-SENTIPOS")) 
						tagToStatsTeam.get(hashtags[0]).setCountPosSenti(value);
					
					else if (split[0].equals("COUNT-SENTINEG")) 
						tagToStatsTeam.get(hashtags[0]).setCountNegSenti(value);
					
					else if (split[0].equals("COUNT-PARTIE")) {
						statsTweets.addPartie(split[1], value);
						statsTweetsAlltime.addPartie(split[1], value);
					}
					else if (split[0].equals("WORD") && !split[2].contains("#")) {
						String myString = split[2].toLowerCase().replaceAll("[^a-zA-Z 0-9]", "");
						tagToStatsTeam.get(split[1]).addWord(myString, value);
					}
				} else {
					String myString = key.toLowerCase().replaceAll("[^a-zA-Z 0-9]", "");
					statsTweets.addWord(myString, value);
					statsTweetsAlltime.addWord(myString, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Hier muss das speichern in die MOngoDB erfolgen
		@Override
		protected void cleanup(Context context) {
			System.out.println(context.getTaskAttemptID());
			if (context.getTaskAttemptID().toString().contains("_r_")) {
			for (Entry<String, StatsTeam> entry : tagToStatsTeam.entrySet()) {
				MongoCollection teamCollection = jongo.getCollection(entry.getKey().substring(1));
				teamCollection.save(entry.getValue());
			}
			System.out.println(context.getTaskAttemptID());
			MongoCollection statTweetsCollection = jongo.getCollection("statTweets");
			statTweetsCollection.save(statsTweets);
			
			statAlltimeCollection.save(statsTweetsAlltime);
			}
		}
	}

	public int run(String[] args) throws Exception {
		Configuration configuration = getConf();

		Job job = new Job(configuration, "Word Count");
		job.setJarByClass(TwitterMatchCount.class);

		job.setMapperClass(TwitterMatchCountMapper.class);
		job.setCombinerClass(TwitterMatchCountReducer.class);
		job.setReducerClass(TwitterMatchCountReducer.class);
		
		// Input
        FileInputFormat.addInputPath(job, new Path("/user/cloudera/twitter.txt"));
        job.setInputFormatClass(TextInputFormat.class);
 
        // Output
        /*FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setOutputFormatClass(TextOutputFormat.class);*/

        job.setOutputFormatClass(NullOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		job.setNumReduceTasks(1);
		return job.waitForCompletion(true) ? 0 : -1;
	}

	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new TwitterMatchCount(), args));
	}
}