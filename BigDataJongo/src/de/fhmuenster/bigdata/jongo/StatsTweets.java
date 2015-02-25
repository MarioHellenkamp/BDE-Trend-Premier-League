package de.fhmuenster.bigdata.jongo;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class StatsTweets {

	@Id @ObjectId // auto
	private String id;
	
	private boolean alltime;
	
	private long tweets;
	private Map<String,Long> words = new HashMap<String, Long>();
	private Date enddatum;
	private Map<String, Long> partien = new HashMap<String, Long>();
	

	public Map<String, Long> getPartien() {
		ValueComparator bvc =  new ValueComparator(partien);
        TreeMap<String,Long> sorted_map = new TreeMap<String,Long>(bvc);
        sorted_map.putAll(partien);
        return sorted_map;
	}
	public void addPartie(String hashtags, long add) {
		Long wert = partien.get(hashtags);
		if (wert != null) 
			partien.put(hashtags, wert+add);
		else
			partien.put(hashtags, add);
	}
	public boolean isAlltime() {
		return alltime;
	}
	public void setAlltime(boolean alltime) {
		this.alltime = alltime;
	}
	public Date getEnddatum() {
		return enddatum;
	}
	public void setEnddatum(Date enddatum) {
		this.enddatum = enddatum;
	}

	public String getId() {
		return id;
	}

	public long getTweets() {
		return tweets;
	}
	public void setTweets(long tweets) {
		this.tweets = tweets;
	}
	
	public Map<String, Long> getWords() {
		return words;
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

class ValueComparator implements Comparator<String> {

    Map<String, Long> base;
    public ValueComparator(Map<String, Long> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
