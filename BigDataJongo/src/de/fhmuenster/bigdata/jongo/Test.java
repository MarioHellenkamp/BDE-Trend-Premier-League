package de.fhmuenster.bigdata.jongo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main (String[] args) throws ParseException {
		System.out.println(TeamMap.getKeyByValue("Everton"));
		
		String string = "1424288691000|grim_el|RT @LFC_Aggregator: Predicted Liverpool lineup vs Besiktas: Balotelli to partner Sturridge up front? http://t.co/tKRVNP9mgH #LFC";
		long date = Long.parseLong(string.split("[|]", 3)[0]);
		Date date2 = new Date(date);
		System.out.println(date2);

		System.out.println(string.split("[|]", 3)[2]);
		
		
	}
}
