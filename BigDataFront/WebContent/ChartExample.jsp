<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="de.fhmuenster.bigdata.JSP.*"%>
<%@ page import="de.fhmuenster.bigdata.jongo.*"%>
<%@ page import="org.mcavallo.opencloud.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.net.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Match Daten</title>
<script src="Chart.js"></script>
<style type="text/css">
#my-div {
	width: 550px;
	height: 170px;
	overflow: hidden;
	position: relative;
}

#my-iframe {
	position: absolute;
	top: -170px;
	left: -130px;
	width: 1280px;
	height: 1200px;;
}
</style>
</head>
<body>

	<form name="Auswahlliste" action="ChartExample.jsp" method="post">
		<table border="0">
			<tr>
				<th colspan="2" align="center"> <%
					String l_matchid = new String();
								session = request.getSession(true);
																try{
																	l_matchid = session.getAttribute("match").toString();
																	}catch(Exception e){
																		l_matchid = "keine Auswahl";	
																	}
																	
																	ConnectMongoDB t1 = new ConnectMongoDB();
																	Match retVal = new Match();
																	retVal = ConnectMongoDB.getMatch(l_matchid);
																	List<StatsTeam> l_tweet_A = new ArrayList<StatsTeam>();
																	List<StatsTeam> l_tweet_B = new ArrayList<StatsTeam>();
																	long sum_count_A = 0;
																	long sum_count_A_only = 0;
																	long sum_pos_count_A = 0;
																	long sum_neg_count_A = 0;
																	long sum_count_B = 0;
																	long sum_count_B_only = 0;
																	long sum_pos_count_B = 0;
																	long sum_neg_count_B = 0;
																	Map<String,Long> words_A = new HashMap<String,Long>();
																	Map<String,Long> words_B = new HashMap<String,Long>();
																	Map<String,Long> words_A_filtered = new HashMap<String,Long>();
																	Map<String,Long> words_B_filtered = new HashMap<String,Long>();
																	
																	try{
																	if ((request.getParameter("time") + "").equals("0")){
																		l_tweet_A.addAll(ConnectMongoDB.getTweetsbefore(retVal.getTeamA(), retVal.getName()+""));					
																		l_tweet_B.addAll(ConnectMongoDB.getTweetsbefore(retVal.getTeamB(), retVal.getName()+""));
																	}else if ((request.getParameter("time") + "").equals("1")){
																		l_tweet_A.addAll(ConnectMongoDB.getTweetswhile(retVal.getTeamA(), retVal.getName()+""));					
																		l_tweet_B.addAll(ConnectMongoDB.getTweetswhile(retVal.getTeamB(), retVal.getName()+""));
																	}else if ((request.getParameter("time") + "").equals("2")){
																		l_tweet_A.addAll(ConnectMongoDB.getTweetsafter(retVal.getTeamA(), retVal.getName()+""));					
																		l_tweet_B.addAll(ConnectMongoDB.getTweetsafter(retVal.getTeamB(), retVal.getName()+""));
																	}else{
																		l_tweet_A.addAll(ConnectMongoDB.getTweetsbefore(retVal.getTeamA(), retVal.getName()+""));					
																		l_tweet_B.addAll(ConnectMongoDB.getTweetsbefore(retVal.getTeamB(), retVal.getName()+""));
																	}
																	for(int i=0; i< l_tweet_A.size();i++){
																		sum_count_A = sum_count_A + l_tweet_A.get(i).getTweetsCount();
																		sum_count_A_only = sum_count_A_only + l_tweet_A.get(i).getTweetsCountOnly();
																		sum_pos_count_A = sum_pos_count_A + l_tweet_A.get(i).getCountPosSenti();
																		sum_neg_count_A = sum_neg_count_A + l_tweet_A.get(i).getCountNegSenti();
																		words_A.putAll(l_tweet_A.get(i).getWordsSorted());
																		
																	}
																	words_A_filtered = ConnectMongoDB.filterWordList(words_A);
																	
																	for(int i=0; i< l_tweet_B.size();i++){
																		sum_count_B = sum_count_B + l_tweet_B.get(i).getTweetsCount();
																		sum_count_B_only = sum_count_B_only + l_tweet_B.get(i).getTweetsCountOnly();
																		sum_pos_count_B = sum_pos_count_B + l_tweet_B.get(i).getCountPosSenti();
																		sum_neg_count_B = sum_neg_count_B + l_tweet_B.get(i).getCountNegSenti();
																		words_B.putAll(l_tweet_B.get(i).getWordsSorted());
																		
																	}
																	words_B_filtered = ConnectMongoDB.filterWordList(words_B);
																	}catch(Exception e){
																		
																	}
				%> 

				</th>
			</tr>
			<tr>
				<td colspan="2" align="center"><img
					src="/BigDataFront/img/<%=retVal.getTeamA()%>.PNG" border="0"
					height="150" width="150" alt="" style="" /> <img
					src="/BigDataFront/img/versus.png" border="0" height="75"
					width="75" alt="" style="" /> <img
					src="/BigDataFront/img/<%=retVal.getTeamB()%>.PNG" border="0"
					height="150" width="150" alt="" style="" /></td>
			<tr>
				<td colspan="2" align="center">
					<div id="my-div">
						<iframe id="my-iframe"
							src="/BigDataFront/googleProxy.jsp?game=<%=retVal.getName() %>"
							scrolling=no></iframe>
					</div>
				</td>

			</tr>

			<tr>
				<td colspan="2" align="center"><img
					src="/BigDataFront/img/Logo_BetClic.gif" border="0" height="100"
					width="400" alt="" style="" /></td>

			</tr>
			<tr>
				<td colspan="2" align="center">
					<div id="canvas_holder_AB">
						<canvas id="Barchart_AB" width="600" height="300" /></canvas>

					</div> <script>
				var barData = {
					    labels: ["<%=retVal.getTeamA()%>", "Unentschieden", "<%=retVal.getTeamB()%>" ],
						datasets : [ {
							label : "Team A",
							fillColor : "rgba(227,13,5,0.5)",
							strokeColor : "rgba(220,220,220,0.8)",
							highlightFill : "rgba(220,220,220,0.75)",
							highlightStroke : "rgba(220,220,220,1)",
							data : [
				<%=retVal.getGewinnerA()%>
					,
				<%=retVal.getUnentschieden()%>
					,
				<%=retVal.getGewinnerB()%>
					]
						} ]
					};
				</script>
				</td>
			</tr>
			<tr>
				<td colspan="3" align="center"><img
					src="/BigDataFront/img/Logo_Twitter.png" border="0" height="100"
					width="400" alt="" style="" /></td>

			</tr>

			<tr>

				<td colspan="3" align="center"><select name="time"
					onSubmit="<%session.setAttribute("time", request.getParameter("time"));%>"
					onChange="submit()">

						<option value="0"
							<%if ((request.getParameter("time") + "").equals("0")) {%>
							selected <%}%>>Vorher</option>
						<option value="1"
							<%if ((request.getParameter("time") + "").equals("1")) {%>
							selected <%}%>>Während</option>
						<option value="2"
							<%if ((request.getParameter("time") + "").equals("2")) {%>
							selected <%}%>>Nachher</option>

				</select></td>
			</tr>

			<tr>
				<td align="center">Anzahl Tweets: <%=sum_count_A%>

				</td>
				<td align="center">Anzahl Tweets: <%=sum_count_B%>

				</td>
			</tr>
			<tr>
				<td align="center">Anzahl Tweets (only): <%=sum_count_A_only%>

				</td>
				<td align="center">Anzahl Tweets (only): <%=sum_count_B_only%>

				</td>
			</tr>
			<tr>
				<td align="center">Positive Tweets: <%=sum_pos_count_A%>

				</td>
				<td align="center">Positive Tweets: <%=sum_pos_count_B%>

				</td>
			</tr>
			<tr>
				<td align="center">Negative Tweets: <%=sum_neg_count_A%>

				</td>
				<td align="center">Negative Tweets: <%=sum_neg_count_B%>

				</td>
			</tr>
			<tr>
				<td align="center">
					<div id="canvas-holder_A">
						<canvas id="Chart_A" width="300" height="300" /></canvas>

					</div> <script>
					var pieData_A = [ {
						value :
				<%=sum_neg_count_A%>
					,
						color : "#F7464A",
						highlight : "#FF5A5E",
						label : "Negative"
					}, {
						value :
				<%=sum_pos_count_A%>
					,
						color : "#46BFBD",
						highlight : "#5AD3D1",
						label : "Positive"
					} ];
				</script>
				</td>
				<td align="center">
					<div id="canvas-holder_B">
						<canvas id="Chart_B" width="300" height="300" /></canvas>

					</div> <script>
					var pieData_B = [ {
						value :
				<%=sum_neg_count_B%>
					,
						color : "#F7464A",
						highlight : "#FF5A5E",
						label : "Negative"
					}, {
						value :
				<%=sum_pos_count_B%>
					,
						color : "#46BFBD",
						highlight : "#5AD3D1",
						label : "Positive"
					} ];
					window.onload = function() {
						var ctx_AB = document.getElementById("Barchart_AB")
								.getContext("2d");
						window.myPie_AB = new Chart(ctx_AB).Bar(barData, {
							barShowStroke : false
						});

						var ctx_A = document.getElementById("Chart_A")
								.getContext("2d");
						window.myPie_A = new Chart(ctx_A).Pie(pieData_A);
						var ctx_B = document.getElementById("Chart_B")
								.getContext("2d");
						window.myPie_B = new Chart(ctx_B).Pie(pieData_B);
					};
				</script>
				</td>
			</tr>
			<tr>
				<%
					Cloud cloud_A = new Cloud(); // create cloud 
					cloud_A.setMaxWeight(48.0); // max font size
					int counter = 0;
					for (Map.Entry<String, Long> entry : words_A_filtered.entrySet()) {
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
						if (cloud_A.size() < 50) {
							counter = counter + 1;
							cloud_A.addTag(tag);
						}
					}
					Cloud cloud_B = new Cloud(); // create cloud 
					cloud_B.setMaxWeight(48.0); // max font size
					counter = 0;
					for (Map.Entry<String, Long> entry : words_B_filtered.entrySet()) {
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
						if (cloud_B.size() < 50) {
							counter = counter + 1;
							cloud_B.addTag(tag);
						}
					}
				%>
				<th align="center" height="400" width="400">
					<%
						for (Tag tag2 : cloud_A.tags()) {
					%> <a style="font-size: <%=tag2.getWeight()%>px"><font
						color="rgb(<%=((Math.random() * 255) + 1)%>,<%=((Math.random() * 255) + 1)%>,<%=((Math.random() * 255) + 1)%>)">
							<%=tag2.getName()%></font></a> <%
 	}
 %>
				</th>
				<th align="center" height="400" width="400">
					<%
						for (Tag tag2 : cloud_B.tags()) {
					%> <a style="font-size: <%=tag2.getWeight()%>px"><font
						color="rgb(<%=((Math.random() * 255) + 1)%>,<%=((Math.random() * 255) + 1)%>,<%=((Math.random() * 255) + 1)%>)">
							<%=tag2.getName()%></font></a> <%
 	}
 %>
				</th>
			</tr>
		</table>

	</form>

</body>
</html>