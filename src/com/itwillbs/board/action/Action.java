package com.itwillbs.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	// 상수
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	// 1. 요청 및 응답을 매개변수로 받아 페이지 이동 기능 구현을 강제하는 메서드
	//    호출 완료 시 ActionForward(path, isRedirect)
	public ActionForward execute(HttpServletRequest request, 
			HttpServletResponse response) throws Exception;
}
