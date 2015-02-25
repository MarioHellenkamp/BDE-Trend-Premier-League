package de.fhmuenster.bigdata.jongo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class StatsTeam {

	@Id @ObjectId // auto
	private String key;
	
	private String hashtag;
	private Date enddatum;
	
	private boolean alltime;
	
	// counts
	private long tweetsCount;
	private long tweetsCountOnly;
	
	private long SentiSum;
	// TODO: Einzelne SentiWerte
	private long countPosSenti;
	private long countNegSenti;
	// TODO Wörter zählen
	private HashMap<String, Long> words = new HashMap<String, Long>();
	
	public boolean isAlltime() {
		return alltime;
	}
	public void setAlltime(boolean alltime) {
		this.alltime = alltime;
	}
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public Date getEnddatum() {
		return enddatum;
	}
	public void setEnddatum(Date enddatum) {
		this.enddatum = enddatum;
	}
	public String getKey() {
		return key;
	}
	public long getTweetsCount() {
		return tweetsCount;
	}
	public void setTweetsCount(long tweets) {
		this.tweetsCount = tweets;
	}
	public long getTweetsCountOnly() {
		return tweetsCountOnly;
	}
	public void setTweetsCountOnly(long tweetsOnly) {
		tweetsCountOnly = tweetsOnly;
	}
	
	public long getSentiSum() {
		return SentiSum;
	}
	public void setSentiSum(long sentiSum) {
		SentiSum = sentiSum;
	}
	public long getCountPosSenti() {
		return countPosSenti;
	}
	public void setCountPosSenti(long countPosSenti) {
		this.countPosSenti = countPosSenti;
	}
	public long getCountNegSenti() {
		return countNegSenti;
	}
	public void setCountNegSenti(long countNegSenti) {
		this.countNegSenti = countNegSenti;
	}

	public Map<String, Long> getWordsSorted() {
		ValueComparator bvc =  new ValueComparator(words);
        TreeMap<String,Long> sorted_map = new TreeMap<String,Long>(bvc);
        sorted_map.putAll(words);
        return sorted_map;
	}
	
	public void addWord(String word, long add) {
		Long wert = words.get(word);
		if (wert != null) 
			words.put(word, wert+add);
		else
			words.put(word, add);
	}

}
