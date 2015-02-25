package de.fhmuenster.bigdata.wettenparser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.fhmuenster.bigdata.jongo.Match;
import de.fhmuenster.bigdata.jongo.ScoreBet;


public class Parser {
	Document dom;

	public List<Match> getMatches(String filename) {
		parseXmlFile(filename);
		return parseDocument();
	}
	
	private void parseXmlFile(String filename){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(filename);
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private List<Match> parseDocument(){
		List<Match> matchList = new ArrayList<Match>();
		
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("event");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				Element el = (Element)nl.item(i);
				if (el.getAttribute("name").equals("Eng. Premier League")) {
					NodeList matches = el.getChildNodes();
					if(matches != null && matches.getLength() > 0) {
						for(int j = 0 ; j < matches.getLength();j++) {
							Match match = new Match();
							Element matchDom = (Element)matches.item(j);
							match.setName(matchDom.getAttribute("name"));
							String[] teams;
							try {
								DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
								String strDate = matchDom.getAttribute("start_date").replace('T', ' ');
								teams = matchDom.getAttribute("name").split(" - ");
								match.setTeamA(teams[0]);
								match.setTeamB(teams[1]);
								Date d  = formatter.parse( strDate );
								match.setStartzeit(d);
								match.setId(Long.parseLong(matchDom.getAttribute("id")));
							} catch (Exception e) {
								continue;
							}
							NodeList bet = matchDom.getChildNodes();
							NodeList bets = bet.item(0).getChildNodes();
							for (int k=0; k < bets.getLength(); k++) {
								Element betType = (Element)bets.item(k);
								if (betType.getAttribute("code").equals("Ftb_Mr3")) {
									NodeList quoten = betType.getChildNodes();
									Element quote = (Element)quoten.item(0);
									match.setGewinnerA(Double.parseDouble(quote.getAttribute("odd")));
									quote = (Element)quoten.item(1);
									match.setUnentschieden(Double.parseDouble(quote.getAttribute("odd")));
									quote = (Element)quoten.item(2);
									match.setGewinnerB(Double.parseDouble(quote.getAttribute("odd")));
								} else if (betType.getAttribute("code").equals("Ftb_Csc")) {
									NodeList scores = betType.getChildNodes();
									for (int l=0; l < scores.getLength(); l++) {
										Element score = (Element)scores.item(l);
										match.addScore(new ScoreBet(score.getAttribute("name"), score.getAttribute("odd")));
									}
								}
							}
							matchList.add(match);
						}
					}
				}
			}
		}
		return matchList;
	}

	public static void main(String[] args) throws Exception {
		/*URL website = new URL("http://xml.cdn.betclic.com/odds_en.xml");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("odds_en.xml");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);*/
		
		/*System.setProperty( "proxySet", "true" );
		System.setProperty( "proxyHost", "10.60.17.102" );
		System.setProperty( "proxyPort", "8080" );*/
		 String fileName = "/tmp/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()); //The file that will be saved on your computer
		 URL link = new URL("http://xml.cdn.betclic.com/odds_en.xml"); //The file that you want to download
		 
		 //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.60.17.102", 8080));
		 
		 //Code to download
		 InputStream in = new BufferedInputStream(link.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 System.out.println("?????");
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();
		 System.out.println("!!!!!!!!!");
		 FileOutputStream fos = new FileOutputStream(fileName);
		 fos.write(response);
		 fos.close();
		
		
		System.out.println("test");
		
		Parser dpe = new Parser();
		List<Match> matches = dpe.getMatches(fileName);
		
		dpe.saveInMongoDB(matches);
		List<Match> newMatches = dpe.loadMongoDB();
		
		for (Match m: newMatches) {
			System.out.println(m);
		}
	}
	
	public void saveInMongoDB(List<Match> matches) throws Exception {
		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");
		
		for (Match m: matches) {
			matchCollection.save(m);
		}
	}
	
	public List<Match> loadMongoDB() throws Exception {
		DB db = new MongoClient().getDB("matches");
		Jongo jongo = new Jongo(db);
		MongoCollection matchCollection = jongo.getCollection("matches");

		MongoCursor<Match> all = matchCollection.find().as(Match.class);
		List<Match> retVal = new ArrayList<Match>();
		
		for (Match m: all) {
			retVal.add(m);
		}
		return retVal;
	}
}
