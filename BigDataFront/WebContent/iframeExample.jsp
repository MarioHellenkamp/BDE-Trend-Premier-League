<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
#my-div
{
    width    : 580px;
    height   : 88px;
    overflow : hidden;
    position : relative;
}
 
#my-iframe
{
    position : absolute;
    top      : -210px;
    left     : -130px;
    width    : 1280px;
    height   : 1200px;;
}
</style>
</head>
<body>
<!-- <iframe onload="this.contentWindow.document.documentElement.scrollTop=200;this.contentWindow.document.documentElement.scrollLeft=130" src="/BigDataFront/googleProxy.jsp?game=<%= request.getParameter("game")%>" width=580 height=95 scrolling=no></iframe> -->
<div>ansklanalsansn<br>ndnoiasdnoidnsaodni<br>aodnaoidnodsan<br>asjioansoa<br></div>
<div id="my-div">
<iframe id="my-iframe" src="/BigDataFront/googleProxy.jsp?game=<%= request.getParameter("game")%>" scrolling=no></iframe>
</div>
</body>
</html>