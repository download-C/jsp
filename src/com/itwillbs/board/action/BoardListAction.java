package com.itwillbs.board.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.If;

import com.itwillbs.board.db.BoardDAO;
import com.itwillbs.board.db.BoardDTO;



public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
System.out.println(" M : BoardListAction_execute() 호출");
		
	// 0. BoardDAO 객체 생성
		BoardDAO dao = new BoardDAO();
		int cnt = dao.getBoardCount();
		
// 페이징 처리 1 (페이지에 표현할 최신글부터~ 및 글 개수 -----------------------------
		
		// 한 페이지에 보여줄 글의 개수 지정
		int pageSize = 10;
		
		// 현 페이지가 몇 번째 페이진지 계산 후 페이지 정보가 없으면 항상 1페이지로 이동
		String pageNum = request.getParameter("pageNum");
		
			
		if(pageNum == null) {		
		pageNum = "1";
		}
			
		// 한 페이지에 보여줄 글 개수 설정
		String urlPageSize = request.getParameter("pageSize");
		if(urlPageSize == null) {
			urlPageSize = "10";
		}
		pageSize = Integer.parseInt(urlPageSize);
		
		

		// 한 페이지마다 보여줄 맨 위와 맨 아래 글 번호 찾기
		int currentPage = Integer.parseInt(pageNum);				
		// 현 페이지의 시작행 글번호 계산
		int startRow = (currentPage-1)*pageSize+1;
		
		// 현 페이지의 끝행 글번호 계산
		int endRow = currentPage*pageSize;		
	
// 페이징 처리 -------------------------------------------------
	// DB의 글쓰기 정보 불러서 호출
		
	// 2. 객체에 게시판 글 목록 정보 저장
//		List<BoardDTO> boardList = dao.getBoardList();
		List<BoardDTO> boardList = dao.getBoardList(startRow, pageSize);
		System.out.println(" M : 게시판 글 정보(DTO) 저장 완료!");

// 페이징 처리 2 (글 목록 하단에 페이지 이동 버튼 만들기) ---------------------
//		// 전체 페이지 수 계산		
//			int pageCount;
//			pageCount = (cnt%pageSize==0)?(cnt/pageSize):(cnt/pageSize)+1;
//		
//			int pageBlock = 10;
//			int startPage = ((currentPage-1/pageCount)/pageBlock) *pageBlock+1;
//			// 총 페이지 수와 페이지 끝블록을 비교
//			
//			int endPage = startPage+pageBlock-1;
//			if(endPage > pageCount ) {
//				endPage = pageCount;
//			}

		// 전체 페이지 수 계산 
		// ex) 전체 글 50개 -> 한페이지 10개씩 출력, 5개 페이지
		// ex) 전체 글 55개 -> 한페이지 10개씩 출력, 6개 페이지
			
		int pageCount =  cnt/pageSize + (cnt%pageSize == 0?  0:1 ) ;
		
		// 한 화면에 보여줄 페이지수(페이지 블럭)
		int pageBlock = 3;
		
		// 페이지블럭 시작번호     1~10 => 1, 11~20 => 11, 21~30=>21
		int startPage = ((currentPage-1)/pageBlock)*pageBlock+1;
		
		// 페이지블럭 끝번호    1~10 => 10   11~20 => 20 
		int endPage = startPage + pageBlock - 1;
		
		// 총 페이지, 페이지 블럭(끝번호) 비교
		if(endPage > pageCount){
			endPage = pageCount;
		}
// 페이징 처리 2 (페이지 이동 버튼 만들기) ---------------------
			
 


// 3. view 페이지 정보 전달을 위해 request 영역에 저장할 내용 -------------------------------
		// 게시판 글 목록 정보 보내기
		request.setAttribute("boardList", boardList);
		
		// 페이징 처리 정보 보내기 (나중에 관련 DTO를 만들 수 있음) 
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("cnt", cnt);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("pageBlock", pageBlock);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		

		
		System.out.println(" Model : 페이징 처리 정보 저장");
		
	
	// 3. 페이지 이름은 같으나 보여지는 화면 양식 전환(목록형 페이지)
		// 3-1. 페이지 이동용 객체 생성
		ActionForward forward = new ActionForward();
		
		// 3-2. 이동할 페이지 경로 생성
		forward.setPath("./board/boardList.jsp");
		// 3-3. 이동할 방식 선택
		forward.setRedirect(false);
	 
		return forward;
		

		

	}
}
