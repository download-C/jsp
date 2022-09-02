<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>index.jsp</h1>
	<%	
		
		// http://localhost:8088/Model2/BoardList.bo
// 		response.sendRedirect("./Test.bo");
		// 게시판 글쓰기 페이지로 이동
// 		response.sendRedirect("./BoardWrite");

		//  rj 게시판 목록으로 이동
		response.sendRedirect("./BoardList.bo");
		
	%>
</body>
</html>