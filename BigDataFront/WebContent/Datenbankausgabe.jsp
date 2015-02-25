<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="de.fhmuenster.bigdata.JSP.*"%>
<%@ page import="de.fhmuenster.bigdata.jongo.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Datenbankausgabe</title>
<body>

	<%
		String l_matchid = new String();
		try{
		l_matchid = session.getAttribute("match").toString();
		}catch(Exception e){
			l_matchid = "keine Auswahl";	
		}
		ConnectMongoDB t1 = new ConnectMongoDB();
		//List<Match> retVal = new ArrayList<Match>();
		//retVal = t1.getMatches();
		Match retVal = ConnectMongoDB.getMatch(l_matchid); 
	%>
	SpielID: <%= l_matchid %>
	<!-- Table for all matches-->
	<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0">

		<!--  erzeuge Kopf der Tabelle  -->
		<TR BGCOLOR="#406E6E">
			<TD WIDTH="150"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;ID</TD>
			<TD WIDTH="150"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Name</TD>
			<TD WIDTH="100"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Startzeit</TD>
			<TD WIDTH="75"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Quote Team A</TD>
			<TD WIDTH="75"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Quote Team B</TD>
			<TD WIDTH="75"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Quote unentschieden</TD>
		</TR>

		<%
			/*request.getParameter("auswahl");
			for (int i = 0; i < retVal.size(); i++) {
				if ((retVal.get(i).getId() + "").equals(l_matchid)) {*/
		%>

		<!--  Ausgabe über alle Matches-->

		<TR>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;<%=retVal.getId()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;<%=retVal.getName()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=retVal.getStartzeit()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=retVal.getGewinnerA()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=retVal.getGewinnerB()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=retVal.getUnentschieden()%></TD>
		</TR>
		<%
			//List<ScoreBet> ScoreBet = new ArrayList<ScoreBet>();
					//ScoreBet = t1.getMatchScores(retVal.get(i).getId());
					List<ScoreBet> ScoreBet = ConnectMongoDB.getMatch(retVal.getName()).getScores();
					System.out.println("Name: " + retVal.getName());
		%>
		<TR BGCOLOR="#406E6E">
			<TD WIDTH="150"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT></TD>
			<TD WIDTH="150"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Spielergebnis</TD>
			<TD WIDTH="100"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Team A</TD>
			<TD WIDTH="75"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Team B</TD>
			<TD WIDTH="75"><FONT FACE="Arial,Helvetica" SIZE="2"
				COLOR="#FFFFFF"></FONT>&nbsp;Quote</TD>

		</TR>
		<%
			for (int j = 0; j < ScoreBet.size(); j++) {
		%>
		<TR>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;<%=ScoreBet.get(j).getScore()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=ScoreBet.get(j).getA()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=ScoreBet.get(j).getB()%></TD>
			<TD><FONT FACE="Arial,Helvetica" SIZE="2"></Font>&nbsp;&nbsp;<%=ScoreBet.get(j).getQuote()%></TD>

		</TR>
		<%
			//}
				//}
			}
		%>
	</TABLE>
</body>

<p style="background: transparent">
</html>