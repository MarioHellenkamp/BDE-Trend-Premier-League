<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="de.fhmuenster.bigdata.JSP.*"%>
<%@ page import="de.fhmuenster.bigdata.jongo.*"%>
<%@ page import="org.mcavallo.opencloud.*"%>
<%@ page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<title>Big Data Projekt</title>
</head>
<body>
	<div id="inhalt">
		<div id="kopf">
			<h3 align="left">
				<img src="/BigDataFront/img/Logo Big Data Projekt.PNG" border="0"
					height="200" width="800" alt="" style="" />
			</h3>
		</div>
		<form name="Auswahlliste" action="index.jsp" method="post">
			<div id="tableheader">
				<table border="0">
					<tr>
						<td colspan="1" align="right">
							<%
								session.setAttribute("team", request.getParameter("team"));
													ConnectMongoDB t1 = new ConnectMongoDB();
													List<String> l_teams = new ArrayList<String>();
													l_teams = ConnectMongoDB.getTeams();
													long l_tweetsum = ConnectMongoDB.getTweetsSum();
													Map<String,Long> words_list = new HashMap<String,Long>();
													words_list = ConnectMongoDB.WordList_Alltime();
							%> <font face="verdana"> Teamauswahl: </font>
						</td>
						<td colspan="3" width="200"><select name="team"
							onSubmit="<%session.setAttribute("team", request.getParameter("team"));%>"
							onChange="submit()">
								<option value="0">Bitte wählen Sie eine Manschaft aus</option>
								<%
									for (int i = 0; i < l_teams.size(); i++) {
								%>
								<option value="<%=l_teams.get(i)%>"
									<%if ((l_teams.get(i))
						.equals((request.getParameter("team") + ""))) {%>
									selected <%}%>><%=l_teams.get(i)%></option>
								<%
									}
								%>
						</select></td>
					</tr>
					<%
						if (session.getAttribute("team") != null) {
					%>
					<tr>
						<td colspan="1" align="right">
							<%
								//session.setAttribute("match", null);
														List<Match> retVal = new ArrayList<Match>();
														retVal = ConnectMongoDB.getMatches(session.getAttribute("team")
																.toString());
							%> <font face="verdana"> Spielliste:</font>
						</td>
						<td colspan="3" width="200"><select name="match"
							onSubmit="<%session.setAttribute("match", request.getParameter("match"));
				session.setAttribute("team", request.getParameter("team"));%>"
							onChange="submit()">
								<option value="0">Bitte wählen Sie ein Spiel aus</option>
								<%
									for (int i = 0; i < retVal.size(); i++) {
								%>
								<option value="<%=retVal.get(i).getName()%>"
									<%if ((retVal.get(i).getName() + "").equals((request
							.getParameter("match") + ""))) {%>
									selected <%}%>><%=retVal.get(i).getName()%></option>
								<%
									}
								%>
						</select></td>

					</tr>
					<%
						}
					%>

					<tr>

						<td>
							<div class="kachel" align="center">
								<h2>
									<a href="/BigDataFront/Architecture.jsp"
										title="Datachart_xml.jsp" target="_top"> <font
										face="verdana">Architektur</font></a>
								</h2>
								<p>
									<a href="/BigDataFront/Architecture.jsp"
										title="Datachart_xml.jsp" target="_top"><img
										src="/BigDataFront/img/Archtecture.gif" border="0"
										height="145" width="165" alt="" style="" /></a>
								</p>
							</div>
						</td>
						<td>

							<div class="kachel" align="center">
								<h2>
									<a href="/BigDataFront/Datenbankausgabe.jsp"
										title="Datenbankausgabe.jsp" target="_top"> <font
										face="verdana">Wettanbieterdaten </font></a>
								</h2>
								<p>

									<a href="/BigDataFront/Datenbankausgabe.jsp"
										title="Datenbankausgabe.jsp" target="_top"><img
										src="/BigDataFront/img/Icon_overview.png" border="0"
										height="145" width="165" alt="" style="" /></a>
								</p>
							</div>

						</td>
						<td>
							<div class="kachel" align="center">
								<h2>
									<a href="/BigDataFront/ChartExample.jsp"
										title="Datachart_xml.jsp" target="_top" onClick="submit()"><font
										face="verdana">Dashboard </font></a>
								</h2>
								<p>
									<a href="/BigDataFront/ChartExample.jsp"
										title="Datachart_xml.jsp" target="_top" onClick="submit()"><img
										src="/BigDataFront/img/Icon_Chart2.png" border="0"
										height="145" width="165" alt="" style="" onClick="submit()" /></a>
								</p>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<table>
								<tr>
									<td colspan="2"><img
										src="/BigDataFront/img/Logo_BetClic.gif" border="0"
										height="75" width="500" alt="" style="" /></td>
									<td colspan="2"><img
										src="/BigDataFront/img/Logo_Twitter.png" border="0"
										height="125" width="500" alt="" style="" /></td>
								</tr>
								<tr>
									<td colspan="2" align="center"> <font
										face="verdana"> Anzahl Teams: <%=l_teams.size()%></font></td>
									<td colspan="2" align="center"><font
										face="verdana">Anzahl Tweets: <%=l_tweetsum%></font></td>
								</tr>
								<tr>

									<%
										Cloud cloud_A = new Cloud(); // create cloud 
											cloud_A.setMaxWeight(58.0); // max font size
											int counter = 0;
											for (Map.Entry<String, Long> entry : words_list.entrySet()) {
												String key = entry.getKey();
												Long value = entry.getValue();
												Tag tag;
												if (counter < 5) {
													tag = new Tag(key, 48);
												} else if (counter < 10) {
													tag = new Tag(key, ((Math.random() * 10) + 28));
												} else if (counter < 30) {
													tag = new Tag(key, ((Math.random() * 10) + 18));
												} else {
													tag = new Tag(key, ((Math.random() * 10) + 8));
												}
												counter++;
												
												if (cloud_A.size() < 50) {
													cloud_A.addTag(tag);
												}
											}%>
									<th colspan="4" align="center" height="400" width="1000">
										<%
											for (Tag tag2 : cloud_A.tags()) {
									%>
									<a style="font-size: <%=tag2.getWeight()%>px"><font
										color="rgb(<%=((Math.random() * 255) + 1)%>,<%=((Math.random() * 255) + 1)%>,<%=((Math.random() * 255) + 1)%>)">
											<%=tag2.getName()%></font></a>
									<%
										}
									%>
									</th>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>

