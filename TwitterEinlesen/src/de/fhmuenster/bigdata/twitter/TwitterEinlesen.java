package de.fhmuenster.bigdata.twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterEinlesen {
	final static private String[] HASHTAGS = {"#AFC","#AVFC","#Clarets","#CFC","#CPFC","#EFC","#HCAFC","#ICFC","#LFC","#MCFC","#MUFC","#NUFC",
		"#QPR","#SAINTSFC","#SCFC","#SAFC","#SWANS","#THFC","#WBAFC","#WHUFC"};
	final static private StringBuffer sb = new StringBuffer();


	public static void main(String[] args) throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setOAuthConsumerKey("9vc2NZtxSjzUYWjqI8o1KexMx");
	    cb.setOAuthConsumerSecret("34Qg019UIBEEkLgfAcxR8F17TGEbjAV1SzXGzHuq2iAqIyJ4mj");
	    cb.setOAuthAccessToken("26720675-H9m1i0VHeb2vyhliKrVXEeNKPbaQLxly7H7wb60tN");
	    cb.setOAuthAccessTokenSecret("mwmrxtBhbGOzyEo287wMiMcriJyOKMHDa1sBjTfExD9Zm");
	    //cb.setJSONStoreEnabled(true);
	    cb.setIncludeEntitiesEnabled(true);
	    
	    StatusListener listener = createListeners();
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    FilterQuery query = new FilterQuery().track(HASHTAGS);
	    twitterStream.addListener(listener);
	    twitterStream.filter(query);
	    
	    int minutes = Integer.parseInt(args[0]);
	    try { Thread.sleep(minutes * 60 * 1000);} catch (Exception e) {};
	    String filename = writeToLocalFile();
	    saveToHDFS(filename);
	}
	
	private static StatusListener createListeners() {
		return new StatusListener() {
	        // The onStatus method is executed every time a new tweet comes in.
	        public void onStatus(Status status) {
	        	String tweet = status.getText().replace("\n", "").replace("\r", "");
	        	String line = status.getCreatedAt().getTime() + "|" + status.getUser().getScreenName() + "|" + tweet + "\n";
	        	sb.append(line);
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
	
	private static String writeToLocalFile() throws Exception {
		BufferedWriter writer = null;
            //create a temporary file
			String filename ="/tmp/t" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File(filename);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(sb.toString());
            writer.close();
            return filename;
	}

	private static void saveToHDFS(String filename) throws Exception {
	String line;
	try {
	Process p = Runtime.getRuntime().exec("hadoop fs -put " + filename + " /user/cloudera/twitter.txt");
	BufferedReader bri = new BufferedReader
	        (new InputStreamReader(p.getInputStream()));
	      BufferedReader bre = new BufferedReader
	        (new InputStreamReader(p.getErrorStream()));
	      while ((line = bri.readLine()) != null) {
	        System.out.println(line);
	      }
	      bri.close();
	      while ((line = bre.readLine()) != null) {
	        System.out.println(line);
	      }
	      bre.close();
	      p.waitFor();
	      System.out.println("Done.");
	      System.exit(0);
	    }
	    catch (Exception err) {
	      err.printStackTrace();
	    }
	}
}
