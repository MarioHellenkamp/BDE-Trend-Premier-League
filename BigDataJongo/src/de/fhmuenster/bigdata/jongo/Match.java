package de.fhmuenster.bigdata.jongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jongo.marshall.jackson.oid.Id;

public class Match {
	
	@Id
	private long id;
	private String name;
	private String teamA;
	private String teamB;
	private Date startzeit;
	private double gewinnerA;
	private double unentschieden;
	private double gewinnerB;
	private List<ScoreBet> scores = new ArrayList<ScoreBet>();
	
	public Match() {
		
	}
	
	@Override
	public String toString() {
		return id + "   " + name + " " + startzeit + " " + gewinnerA + " " + unentschieden + " " + gewinnerB + " Scores:" + scores.size();
	}
	
	public List<ScoreBet> getScores() {
		Collections.sort(scores);
		return scores;
	}
	public void addScore(ScoreBet score) {
		scores.add(score);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTeamA() {
		return teamA;
	}
	public void setTeamA(String teamA) {
		this.teamA = teamA;
	}
	public String getTeamB() {
		return teamB;
	}
	public void setTeamB(String teamB) {
		this.teamB = teamB;
	}
	public Date getStartzeit() {
		return startzeit;
	}
	public void setStartzeit(Date startzeit) {
		this.startzeit = startzeit;
	}
	public double getGewinnerA() {
		return gewinnerA;
	}
	public void setGewinnerA(double gewinnerA) {
		this.gewinnerA = gewinnerA;
	}
	public double getUnentschieden() {
		return unentschieden;
	}
	public void setUnentschieden(double unentschieden) {
		this.unentschieden = unentschieden;
	}
	public double getGewinnerB() {
		return gewinnerB;
	}
	public void setGewinnerB(double gewinnerB) {
		this.gewinnerB = gewinnerB;
	}
	
	@Override
	public boolean equals(Object object) {
		if ( this == object ) 
			return true;
		if ( !(object instanceof Match) ) 
			return false;
		
		Match matchObject = (Match) object;
		return (matchObject.id == this.id);
	}

}
