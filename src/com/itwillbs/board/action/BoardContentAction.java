package com.itwillbs.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itwillbs.board.db.BoardDAO;
import com.itwillbs.board.db.BoardDTO;

public class BoardContentAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		System.out.println(" Model : BoardContentAction_execute() 호출");
		
		// 1. view에서 전달된 정보(bno, pageNum) 저장
		int bno = Integer.parseInt(request.getParameter("bno"));
		String pageNum = request.getParameter("pageNum");
		
		// 2. 조회수를 올리기 위해 BoardDAO_updateReadCount() 메서드 호출
		BoardDAO dao = new BoardDAO();
		
		dao.updateReadcount(bno);
		System.out.println(" Model : 조회수 1 증가");
		
		// 모델에서는 객체 정보를 보여줄 필요가 없기 때문에
		// 받아온 정보를 바로 view로 보내줌
		
		
		BoardDTO dto = dao.getBoard(bno);
		request.setAttribute("dto", dto);
		//request.setAttribute(dto2", dao.getBoard(bno));
		request.setAttribute("pageNum", pageNum); // 출력할 때 사용
		
		// 출력 view 페이지로 이동
		ActionForward forward =  new ActionForward();
		forward.setPath("./board/boardContent.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
