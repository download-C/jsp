package com.itwillbs.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwillbs.board.db.BoardDAO;
import com.itwillbs.board.db.BoardDTO;
import com.sun.org.apache.xml.internal.serialize.OutputFormat.DTD;

public class BoardWriteAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		System.out.println(" M : BoardWriteAction_execute() 호출");
	// 0. 한글 처리 -------------------------------------------------------
		request.setCharacterEncoding("UTF-8");
	// 1. wirteForm.jsp에서 받은 정보를 담은 WriteBean 저장 ---------------
		                        // ↳(글쓴이, 비밀번호, 제목, 내용)
		
		BoardDTO dto = new BoardDTO();
		dto.setName(request.getParameter("name"));
		dto.setPass(request.getParameter("pass"));
		dto.setSubject(request.getParameter("subject"));
		dto.setContent(request.getParameter("content"));
		
		System.out.println(" M : "+dto);
		
		
	// 2. DB에 정보 저장 --------------------------------------------------
		// 2-1. BoardDAO 객체 생성
		BoardDAO dao = new BoardDAO();
		
		// 2-2. DB에 정보 글 정보 저장하는 BoardDAO - boardWrite() 호출
		dao.boardWrite(dto);
		
	// 3. 글 작성 완료 후 게시판 페이지 또는 작성한 글 페이지 이동
		ActionForward forward = new ActionForward();
		forward.setPath("./BoardList.bo");
		forward.setRedirect(true);
		
		return forward;
	// 4. execute() 메서드를 호출한 곳(BoardFrontController.java)로 돌아감
	}

}
