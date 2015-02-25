<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.io.*"%>
    <%@ page import="java.net.*"%>
<%
// Hinter q= die Partie angeben. Wichtig alle Leerzeichen entfernen und "-" ersetzen.
String game = request.getParameter("game");
game = game.replaceAll(" ", "-");
game = game.replaceAll("---", "-");
URL uri= new URL("https://www.google.de/search?q=" + game);
        URLConnection ec = uri.openConnection();
        //ec.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        ec.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; de-DE; rv:31.0) Gecko/20100101 Firefox/31.0");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(
                ec.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        out.println(a.toString());  %>