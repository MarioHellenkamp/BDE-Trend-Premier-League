package de.fhmuenster.bigdata.jongo;

import java.util.Date;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class Tweet {

	@Id @ObjectId
	private String id;	
	
	private String tweet;
	private Date mapTimestamp;
	private String user;
	
	private String sentiString;
	private int posSenti;
	private int negSenti;

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getSentiString() {
		return sentiString;
	}

	public void setSentiString(String sentiString) {
		this.sentiString = sentiString;
	}

	public Date getMapTimestamp() {
		return mapTimestamp;
	}

	public void setMapTimestamp(Date mapTimestamp) {
		this.mapTimestamp = mapTimestamp;
	}

	public int getPosSenti() {
		return posSenti;
	}

	public void setPosSenti(int posSenti) {
		this.posSenti = posSenti;
	}

	public int getNegSenti() {
		return negSenti;
	}

	public void setNegSenti(int negSenti) {
		this.negSenti = negSenti;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
}
