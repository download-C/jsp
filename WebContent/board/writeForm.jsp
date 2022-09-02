<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
	label {
		width: 30px;
	}
</style>
<title>Insert title here</title>
</head>
<body>
	<h1>writeForm.jsp</h1>
	
	<h3>글쓰기 페이지입니다.</h3>
	
	<fieldset>
		<!-- 게시판은 기본적으로 첨부파일이 있기 때문에 post 방식으로 구현 -->
		<form action="./BoardWriteAction.bo" method="post">
			글쓴이 : <input type="text" name="name" id="name"> <br>
			비밀번호 : <input type="password" name="pass" id="pass"> <br>
			제목 : <input for="text" name="subject" id="subject"> <br>
			내용 : <textarea rows="10" cols="20" name="content" id="content"></textarea>
			<input type="submit" value="글쓰기">
		</form>
	</fieldset>
</body>
</html>