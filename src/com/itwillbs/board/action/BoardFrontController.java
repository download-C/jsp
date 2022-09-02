package com.itwillbs.board.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;

public class BoardFrontController extends HttpServlet{
	
	// http://localhost:8088/Model2/Test.bo
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GET 방식, POST 방식 호출 - doGet(), doPost() 실행");
		
			
			System.out.println("========1. 가상 주소 계산 시작========");
		// 1. 가상주소(servlet 파일이 있는 프로젝트명) 계산----------------------------------------
				      // jsp 파일의 실제 주소는 WebContent 폴더
			// 1-1. URI(/Model2.itwill.bo ) 불러오기
			String requestURI = request.getRequestURI();
			System.out.println("C : requestURL : " +requestURI);
			
			// 1-2. context path( /Model2 ) 불러오기 
			String ctxPath = request.getContextPath();
			System.out.println("C : ctxPAth : "+ctxPath);
			
			// 1-3. URI를 context path 글자길이만큼 자르기 -> /itwill.bo
			String command = requestURI.substring(ctxPath.length());
			System.out.println("C : command : "+command);
			
			System.out.println("========1. 가상 주소 계산 완료========");
			System.out.println();
			
		// 2. 가상 주소 매핑------------------------------------------------------------------
			// 적절한 URL(BoardWrite, BoardWriteAction, BoardListAll 등)을 입력했을 때 해당 페이지 보여주기
			
			System.out.println("========2. 가상 주소 매핑 시작========");
			// 페이지 이동을 위한 정보를 담을 객체 생성
			Action action = null;
			ActionForward forward = null;
			
			//  2-1. URI 가 BoardWrite일 때 
			if(command.equals("/BoardWrite.bo")) {
				System.out.println("C : /BoardWrite.bo 호출");
				System.out.println("DB 정보가 필요 없어 View 페이지로 이동합니다.");
				
				forward = new ActionForward();
				forward.setPath("./board/writeForm.jsp"); // WebContent/board/writeForm.jsp로 이동
				forward.setRedirect(false); // forward() 방식으로 이동
			} 
			// 2-2. URI가 BoardWriteAction일 때
			else if(command.equals("/BoardWriteAction.bo")) {
				System.out.println(" C : /BoardWriteAction.bo 호출");
				System.out.println(" C : DB 정보 필요, ");
				System.out.println("글쓰기 페이지로 이동");
				
				// BoardWriteAction 객체 생성
				// BoardWriteAction bwAction = new BoardWriteAction();
				action = new BoardWriteAction();
				try {
					forward = action.execute(request, response); // true -> sendRedirect() 방식으로 이동
					// excute() 메서드에서 경로와 이동방법을 모두 설정했기 때문에 if문을 빠져나감.
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 2-3. URI가 BoardList일 때
			else if(command.equals("/BoardList.bo")) {
				System.out.println(" C : /BoardList.bo 호출");
				System.out.println(" C : DB 정보 필요, 페이지 이동 X");
				
				// BoardListAction 객체 생성
				//BoardListAction listAction = new BoardListAction();
				action = new BoardListAction();
				try {
					forward = action.execute(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			else if(command.equals("/BoardContent.bo")){
				System.out.println(" Controller : /BoardContet.bo 호출");
				System.out.println(" Controller : DB정보 사용 후 출력");
				
				//BoardContentAction  객체 생성
				action = new BoardContentAction();
				
				try {
					forward = action.execute(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			System.out.println("========2. 가상 주소 매핑 완료========");
			System.out.println();
			
		// 3. 가상 주소 이동------------------------------------------------------------------
			System.out.println("========3. 가상 주소 이동 시작========");
			// 페이지 이동 정보가 있을 때(DB 연동)
			if(forward != null) {
				// true : sendRedirect() 방식
				if(forward.isRedirect()) {
					System.out.println("C : ture");
					System.out.println(forward.getPath() +" 이동");
					System.out.println("방식 : sendReDirect() 방식 : ");
					response.sendRedirect(forward.getPath());
				// false : forward() 방식 (연동 X)
				}else {
					System.out.println("C : false ");
					System.out.println(forward.getPath() +" 이동");
					System.out.println("방식 : forward() 방식");
//					RequestDispatcher dis = request.getRequestDispatcher(forward.getPath());

					RequestDispatcher dis 
					   = request.getRequestDispatcher(forward.getPath());
					dis.forward(request, response);
				}
			}
			
			System.out.println("========3. 가상 주소 이동 완료========");
			System.out.println();
	}


	//////////////////////////////////////doGet()////////////////////////////////////////////////
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GET 방식 호출 - doGet() 실행");
		doProcess(request, response);
	}

	//////////////////////////////////////doPost()///////////////////////////////////////////////
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("POST 방식 호출 - doPost() 실행");
		doProcess(request, response);
		
	}
	
}
