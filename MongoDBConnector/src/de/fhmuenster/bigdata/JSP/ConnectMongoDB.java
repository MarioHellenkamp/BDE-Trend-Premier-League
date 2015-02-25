package de.fhmuenster.bigdata.JSP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.fhmuenster.bigdata.jongo.Match;
import de.fhmuenster.bigdata.jongo.ScoreBet;
import de.fhmuenster.bigdata.jongo.StatsTeam;
import de.fhmuenster.bigdata.jongo.StatsTweets;
import de.fhmuenster.bigdata.jongo.TeamMap;

public class ConnectMongoDB {

	public static void main(String[] args) throws Exception {

	}

	/**
	 * Gibt die Liste vorhandener Spiele zurück
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<Match> getMatches() throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");

		MongoCursor<Match> all = matchCollection.find().as(Match.class);
		List<Match> retVal = new ArrayList<Match>();

		for (Match m : all) {
			retVal.add(m);
		}

		return (retVal);
	}

	/**
	 * Gibt ein Spiel anhand von der Spiel-ID zurück
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Match getMatch(String id) throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");

		MongoCursor<Match> all = matchCollection.find().as(Match.class);
		List<Match> retVal = new ArrayList<Match>();
		Match singleMatch = new Match();
		for (Match m : all) {
			retVal.add(m);
		}
		for (int i = 0; i < retVal.size(); i++) {
			if ((retVal.get(i).getName() + "").equals(id)) {
				singleMatch = retVal.get(i);
			}
		}
		return (singleMatch);
	}

	/**
	 * Gibt eine Liste von Spielen anhand eines Teamnamen
	 * 
	 * @param team
	 * @return
	 * @throws Exception
	 */
	public static List<Match> getMatches(String team) throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");

		MongoCursor<Match> all = matchCollection.find().as(Match.class);
		List<Match> retVal = new ArrayList<Match>();
		List<Match> onlymatch = new ArrayList<Match>();

		for (Match m : all) {
			retVal.add(m);
		}
		for (int i = 0; i < retVal.size(); i++) {
			if ((retVal.get(i).getTeamA().equals(team))
					|| (retVal.get(i).getTeamB().equals(team))) {
				onlymatch.add(retVal.get(i));
			} else {

			}
		}
		return (onlymatch);
	}

	/**
	 * Gibt eine Liste aller in der DB vorhandener Teams zurück
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> getTeams() throws Exception {

		TeamMap tm = new TeamMap();
		Map<String, String> teamMap = tm.getall();
		List<String> l_teams = new ArrayList<String>();

		for (Map.Entry<String, String> entry : teamMap.entrySet()) {
			l_teams.add(entry.getValue());
		}

		return (l_teams);
	}

	/**
	 * 
	 * @param id
	 * @return List <ScoreBet>
	 * @throws Exception
	 * 
	 *             Gibt für ein Match die Liste an Spielergebnissen inklusive
	 *             der Quote zurück
	 */
	public static List<ScoreBet> getMatchScores(String id) throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");
		List<ScoreBet> lSB = new ArrayList<ScoreBet>();
		List<Match> retVal = new ArrayList<Match>();

		MongoCursor<Match> all = matchCollection.find().as(Match.class);

		for (Match m : all) {
			retVal.add(m);

		}

		for (int i = 0; i < retVal.size(); i++) {
			if ((retVal.get(i).getName()) == id) {
				lSB.addAll(retVal.get(i).getScores());
			}
		}

		return (lSB);
	}

	/**
	 * Gibt zu einem Spiel eine Liste mit geannten Wörtern zurück
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<String> getWordList() throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");
		List<String> lWords = new ArrayList<String>();
		List<Match> retVal = new ArrayList<Match>();

		MongoCursor<Match> all = matchCollection.find().as(Match.class);

		for (Match m : all) {
			retVal.add(m);

		}

		for (int i = 0; i < retVal.size(); i++) {

			lWords.add(retVal.get(i).getTeamA());
			lWords.add(retVal.get(i).getTeamB());

		}

		return (lWords);
	}

	/**
	 * Gibt zu einem Team und einem Spiel die Tweetstatistik vor dem Spiel
	 * zurück
	 * 
	 * @param teamName
	 * @return
	 * @throws Exception
	 */
	public static List<StatsTeam> getTweetsbefore(String teamName, String id)
			throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		String hashtag = TeamMap.getKeyByValue(teamName);
		ConnectMongoDB CMDB = new ConnectMongoDB();
		Match m1 = CMDB.getMatch(id);
		MongoCollection tweetsCollection = jongo.getCollection(hashtag);

		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date gamedate = m1.getStartzeit();

		Date startdate = new Date(gamedate.getTime() - 172800000);
		Date enddate = new Date(gamedate.getTime() + 300000);

		MongoCursor<StatsTeam> all2 = tweetsCollection.find(
				"{hashtag: {$regex: #},enddatum: {$gte : #, $lt: #}}",
				"#(?i)" + hashtag, startdate, enddate).as(StatsTeam.class);
		List<StatsTeam> retVal = new ArrayList<StatsTeam>();

		for (StatsTeam m : all2) {
			retVal.add(m);
		}
		return (retVal);
	}

	/**
	 * Gibt zu einem Team und einem Spiel die Tweetstatistik nach dem Spiel
	 * zurück
	 * 
	 * @param teamName
	 * @return
	 * @throws Exception
	 */
	public static List<StatsTeam> getTweetsafter(String teamName, String id)
			throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		String hashtag = TeamMap.getKeyByValue(teamName);
		ConnectMongoDB CMDB = new ConnectMongoDB();
		Match m1 = CMDB.getMatch(id);
		MongoCollection tweetsCollection = jongo.getCollection(hashtag);

		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date gamedate = m1.getStartzeit();

		Date startdate = new Date(gamedate.getTime() + 6600000);
		Date enddate = new Date(gamedate.getTime() + 179400000);

		MongoCursor<StatsTeam> all2 = tweetsCollection.find(
				"{hashtag: {$regex: #},enddatum: {$gte : #, $lt: #}}",
				"#(?i)" + hashtag, startdate, enddate).as(StatsTeam.class);
		List<StatsTeam> retVal = new ArrayList<StatsTeam>();

		for (StatsTeam m : all2) {
			retVal.add(m);
		}
		return (retVal);
	}

	/**
	 * Gibt zu einem Team und einem Spiel die Tweetstatistik während dem Spiel
	 * zurück
	 * 
	 * @param teamName
	 * @return
	 * @throws Exception
	 */
	public static List<StatsTeam> getTweetswhile(String teamName, String id)
			throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		String hashtag = TeamMap.getKeyByValue(teamName);
		ConnectMongoDB CMDB = new ConnectMongoDB();
		Match m1 = CMDB.getMatch(id);
		MongoCollection tweetsCollection = jongo.getCollection(hashtag);

		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date gamedate = m1.getStartzeit();

		Date startdate = new Date(gamedate.getTime() + 300000);
		Date enddate = new Date(gamedate.getTime() + 6600000);

		MongoCursor<StatsTeam> all2 = tweetsCollection.find(
				"{hashtag: {$regex: #},enddatum: {$gte : #, $lt: #}}",
				"#(?i)" + hashtag, startdate, enddate).as(StatsTeam.class);
		List<StatsTeam> retVal = new ArrayList<StatsTeam>();

		for (StatsTeam m : all2) {
			retVal.add(m);
		}
		return (retVal);
	}

	/**
	 * Gibt die Summe an Tweets in der Datenbank zurück
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Long getTweetsSum() throws Exception {

		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		long tweetcount = 0;

		MongoCollection tweetsCollection = jongo.getCollection("statAlltime");

		MongoCursor<StatsTweets> all2 = tweetsCollection.find().as(
				StatsTweets.class);
		List<StatsTweets> retVal = new ArrayList<StatsTweets>();

		for (StatsTweets m : all2) {
			retVal.add(m);
		}
		for (int i = 0; i < retVal.size(); i++) {
			tweetcount = tweetcount + retVal.get(i).getTweets();
		}
		return (tweetcount);
	}

	/**
	 * Gibt die Summe an Tweets in der Datenbank zurück
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Long> filterWordList(Map<String, Long> words_List)
			throws Exception {

		Map<String, Long> filtered_words = new HashMap<String, Long>();
		List<String> ls = new ArrayList<String>();
		Boolean insert = true;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"Word_Filter.txt"));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				ls.add(zeile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Iterator<String> it = words_List.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			insert = true;
			if (key.length() >= 3) {
				for (int i = 0; i < ls.size(); i++) {
					if (ls.get(i).equals(key)) {
						insert = false;
					}
				}
			} else {
				insert = false;
			}
			if (insert) {
				filtered_words.put(key, words_List.get(key));
			}

		}

		ValueComparator bvc = new ValueComparator(filtered_words);
		TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);
		sorted_map.putAll(filtered_words);
		return (sorted_map);
	}

	static TreeMap<String, Long> retValWordList_Alltime;

	public static Map<String, Long> WordList_Alltime() throws Exception {
		if (retValWordList_Alltime == null) {
			DB db = new MongoClient().getDB("matches");
			Jongo jongo = new Jongo(db);

			MongoCollection tweetsCollection = jongo
					.getCollection("statAlltime");

			MongoCursor<StatsTweets> all2 = tweetsCollection.find().as(
					StatsTweets.class);
			List<StatsTweets> retVal = new ArrayList<StatsTweets>();
			for (StatsTweets st : all2) {
				retVal.add(st);
			}
			Map<String, Long> words_List = new HashMap<String, Long>();
			for (int i = 0; i < retVal.size(); i++) {
				words_List.putAll(retVal.get(i).getWordsSorted());
			}

			HashMap<String, Long> filtered_words = new HashMap<String, Long>();
			List<String> ls = new ArrayList<String>();
			Boolean insert = true;
			try {
				BufferedReader in = new BufferedReader(new FileReader(
						"Word_Filter.txt"));
				String zeile = null;
				while ((zeile = in.readLine()) != null) {
					ls.add(zeile);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (Iterator<String> it = words_List.keySet().iterator(); it
					.hasNext();) {
				String key = it.next();
				insert = true;
				if (key.length() >= 3) {
					if (key.length() > 4 && key.startsWith("http")) {
						insert = false;
					} else {
						for (int i = 0; i < ls.size(); i++) {
							if (ls.get(i).equalsIgnoreCase(key)) {
								insert = false;
							}
						}
					}

				} else {
					insert = false;
				}
				if (insert) {
					filtered_words.put(key, words_List.get(key));
				}

			}

			ValueComparator bvc = new ValueComparator(filtered_words);
			TreeMap<String, Long> sorted_map = new TreeMap<String, Long>(bvc);
			sorted_map.putAll(filtered_words);
			retValWordList_Alltime = sorted_map;
			return (sorted_map);
		} else {
			return retValWordList_Alltime;
		}
	}
}

class ValueComparator implements Comparator<String> {

	Map<String, Long> base;

	public ValueComparator(Map<String, Long> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}
