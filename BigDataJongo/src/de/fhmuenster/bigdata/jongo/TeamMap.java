package de.fhmuenster.bigdata.jongo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TeamMap {

	public static Map<String, String> teamMap = new HashMap<String, String>();

	static {
		teamMap.put("EFC", "Everton");
		teamMap.put("WBAFC", "West Bromwich Albion");
		teamMap.put("HCAFC", "Hull");
		teamMap.put("AFC", "Arsenal");
		teamMap.put("NUFC", "Newcastle");
		teamMap.put("LFC", "Liverpool");
		teamMap.put("WHUFC", "West Ham");
		teamMap.put("SCFC", "Stoke");
		teamMap.put("QPR", "QPR");
		teamMap.put("SAFC", "Sunderland");
		teamMap.put("CLARETS", "Burnley");
		teamMap.put("MUFC", "Manchester United");
		teamMap.put("LCFC", "Leicester");
		teamMap.put("THFC", "Tottenham");
		teamMap.put("CPFC", "Crystal Palace");
		teamMap.put("CFC", "Chelsea");
		teamMap.put("MCFC", "Manchester City");
		teamMap.put("AVFC", "Aston Villa");
		teamMap.put("SAINTSFC", "Southampton");
		teamMap.put("SWANS", "Swansea");
	}

	public static String getKeyByValue(String value) {
		for (Entry<java.lang.String, java.lang.String> entry : teamMap
				.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String getValueByKey(String key) {
		for (Entry<java.lang.String, java.lang.String> entry : teamMap
				.entrySet()) {
			if (key.equals(entry.getKey())) {
				return entry.getValue();
			}
		}

		return null;
	}

	public Map<String, String> getall() {
		return (teamMap);
	}

}
