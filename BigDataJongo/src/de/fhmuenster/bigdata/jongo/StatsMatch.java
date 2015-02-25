package de.fhmuenster.bigdata.jongo;

import java.util.ArrayList;
import java.util.List;

public class StatsMatch {
	
	//Counts
	private long tweetsPartie; // (hashtag a und b)
	private long tweetsA;
	private long tweetsOnlyA;
	private long tweetsB;
	private long tweetsOnlyB;
	
	private List<SentiScore> sentiScoresA = new ArrayList<SentiScore>();
	private long sentisSumA;
	private List<SentiScore> sentiScoresB = new ArrayList<SentiScore>();
	private long sentiSumB;
	
	public StatsMatch() {}

	public long getTweetsPartie() {
		return tweetsPartie;
	}

	public void addTweetsPartie(long tweetsPartie) {
		this.tweetsPartie += tweetsPartie;
	}

	public long getTweetsA() {
		return tweetsA;
	}

	public void addTweetsA(long tweetsA) {
		this.tweetsA+= tweetsA;
	}

	public long getTweetsOnlyA() {
		return tweetsOnlyA;
	}

	public void addTweetsOnlyA(long tweetsOnlyA) {
		this.tweetsOnlyA+= tweetsOnlyA;
	}

	public long getTweetsB() {
		return tweetsB;
	}

	public void addTweetsB(long tweetsB) {
		this.tweetsB += tweetsB;
	}

	public long getTweetsOnlyB() {
		return tweetsOnlyB;
	}

	public void addTweetsOnlyB(long tweetsOnlyB) {
		this.tweetsOnlyB += tweetsOnlyB;
	}

	public long getSentisSumA() {
		return sentisSumA;
	}

	public void addSentisSumA(long sentisSumA) {
		this.sentisSumA += sentisSumA;
	}

	public long getSentiSumB() {
		return sentiSumB;
	}

	public void addSentiSumB(long sentiSumB) {
		this.sentiSumB += sentiSumB;
	}
	
	public List<SentiScore> getSentiScoresA() {
		return sentiScoresA;
	}
	public void addSentiScoreA(SentiScore sentiScore) {
		this.sentiScoresA.add(sentiScore);
	}
	
	public List<SentiScore> getSentiScoresB() {
		return sentiScoresB;
	}
	public void addSentiScoreB(SentiScore sentiScore) {
		this.sentiScoresB.add(sentiScore);
	}
	
}
