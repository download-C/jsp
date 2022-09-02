package com.itwillbs.board.action;

import javax.servlet.http.HttpServlet;

// 페이지 이동을 위한 정보 저장 객체 -> 입장 티켓
public class ActionForward extends HttpServlet{
	private String path; // 이동할 주소
	private boolean isRedirect; // 이동할 방식 
								// true - sendRedirect() 방식으로 이동 
								// false - forward() 방식으로 이동
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
	
}
