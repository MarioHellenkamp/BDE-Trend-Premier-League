package de.fhmuenster.bigdata.jongo;

public class ScoreBet implements Comparable<ScoreBet> {
	
	private String score;
	private int a;
	private int b;
	private double quote;
	
	private ScoreBet() {
		
	}
	
	public ScoreBet(String score, String quote) {
		this.score = score;
		this.quote = Double.parseDouble(quote);
		String[] tmp = score.split(" - ");
		a = Integer.parseInt(tmp[0]);
		b = Integer.parseInt(tmp[1]);
	}

	public String getScore() {
		return score;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public double getQuote() {
		return quote;
	}

	@Override
	public int compareTo(ScoreBet o) {
		return Double.compare(quote, o.getQuote());
	}

}
