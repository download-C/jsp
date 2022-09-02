package com.itwillbs.board.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class BoardDAO {
	// 게시판 관련 모든 메서드 생성
	
	// 공통 변수 
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";

	
	public BoardDAO() {
		System.out.println("DAO : DB 연결을 위한 모든 정보 준비 완료!");
	}
	
	// 1-1. 수동으로 DB 연결하기 ------------------------------------------------
	private Connection getConnect() throws Exception { 
		System.out.println(" DAO : getConnect() 호출");
//		
//		String DRIVER = "com.mysql.cj.jdbc.Driver";
//		String DBURL = "jdbc:mysql://localhost:3306/jspdb";
//		String DBID = "root";
//		String DBPW = "1234";
//		
//		// 1. 드라이버 로드 
//		Class.forName(DRIVER);
//		System.out.println("드라이버 로드 성공!");
//		// 2. 디비 연결
//		con = DriverManager.getConnection(DBURL, DBID, DBPW);
//		System.out.println("DB(jspdb) 연결 성공!");
//		System.out.println("con: "+con);
//	
//	return con;
	
// 1-2. CP(Connection Pool)을 이용해 연결하기 --------------------
		// 프로젝트 정보 초기화
		try {
		Context initCTX = new InitialContext();
		
		// 초기화된 프로젝트 중 데이터 관련 정보 불러오기
		DataSource ds = (DataSource)initCTX.lookup("java:comp/env/jdbc/model2");
		con = ds.getConnection();
		System.out.println(" DAO : DB 연결 완료 ");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	
	
	
// 2. DB 연결하는 역순으로 연결 끊기 --------------------------------------
	public void closeDB() {
		System.out.println(" DAO : closeDB() 호출");
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
			System.out.println("DAO : 자원 해제 완료!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" DAO : DB 연결 해제");
	}
	
	
	
	
	
// 3. DTO에 set()으로 들어오는 정보를 get()으로 받아서 sql실행문으로 넣기--------------------------------------
	public void boardWrite(BoardDTO dto) {
		System.out.println(" DAO : boardWrite() 호출");
		int bno = 0;
		try {
		// 3-0. DB 연결하기	
			getConnect();
			
		// 3-1. 중간 게시물이 삭제될 것을 고려하여 itwill_board 테이블에서 
		//    bno 컬럼의 가장 큰 수(게시판에 작성된 가장 마지막 글번호) 구해서 변수에 넣기
			sql = "select max(bno) from itwill_board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
		// 3-2. select max(bno)를 실행했을 때 나오는 숫자에 1을 더함.
			if(rs.next()) { 
				// sql 워크벤치 행 커서 모양 
				// 1. 삼각형 = true
				// 2. 원 = false
				// 3. 커서 없음 = false
				//getInt() = 컬럼의 마지막값 반환, 만약 null일 경우 0 반환
				bno = rs.getInt(1)+1; 
				//  = rs.getInt("max(bno)")+1;
				System.out.println(" DAO : 현재 생성되는 글번호는 "+bno+"입니다.");
			} 
			
		// 3-3. insert 구문 생성
			sql = "insert into itwill_board(bno,name,pass,subject,content,"
					+ "readcount,re_ref,re_lev,re_seq,date,ip,file) "
					+ "values(?,?,?,?,?,?,?,?,?,now(),?,?)";
			pstmt = con.prepareStatement(sql);
		
		// 3-4. ? 처리하기
			pstmt.setInt(1, bno);
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getPass());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			
			pstmt.setInt(6, 0); // 조회수 0
			pstmt.setInt(7, bno); // 답글 그룹번호 = 글 번호(설정 아직 안 함)
			pstmt.setInt(8, 0);  // 답글 레벨 0
			pstmt.setInt(9, 0);  // 답글 순서 0
//		    pstmt.setDate(10, dto.getDate()) 는 위에서 now()로 넣어줬기 때문에 사라짐
			pstmt.setString(10, dto.getIp());
			pstmt.setString(11, dto.getFile());
			
			
		// 3-5. sql 구문 실행
			pstmt.executeUpdate();
			System.out.println(" DAO : 글 정보 저장 완료! ");
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
		} finally {	
			
		// 3-6. insert 구문 완료 후 DB 연결 해제
			closeDB();
			
		}
	}
	
// 4. 글 목록 조회(all) -> 배열 크기를 알 수 없기 때문에 List로 저장--------------------------
	public List<BoardDTO> getBoardList() {
		System.out.println(" DAO : getBoardList() 호출");
		List<BoardDTO> boardList = new ArrayList<>(); // ArrayList가 List로 업캐스팅돼서 범용적 사용 가능함.
			try {
		// 4-1. 드라이버 로드 및 DB 연결
				con = getConnect();
				
		// 4-2. sql(select) 작성 & pstmt 객체 생성
				sql = "select * from itwill_board";
				pstmt = con.prepareStatement(sql);
				
		// 4-3. sql 실행 
				rs = pstmt.executeQuery();
			
		// 4-4. 데이터 처리
				while(rs.next()) {
					// 4-4-1. 데이터 있을 때 DB에 저장된 정보를 DTO에 저장
					BoardDTO dto = new BoardDTO();
					dto.setBno(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setPass(rs.getString(3));
					dto.setSubject(rs.getString(4));
					dto.setContent(rs.getString(5));
					dto.setReadcount(rs.getInt(6));
					dto.setRe_ref(rs.getInt(7));
					dto.setRe_lev(rs.getInt(8));
					dto.setRe_seq(rs.getInt(9));
					dto.setDate(rs.getDate(10));
					dto.setIp(rs.getString(11));
					dto.setFile(rs.getString(12));
					
					// 4-4-2. DTO 정보를 List에 저장
					boardList.add(dto);
				}
				System.out.println(" C : 게시판 정보 저장 완료");
				// 게시판 정보 출력하나 데이터가 많이 쌓일 경우 오래 걸리기 때문에 확인용으로만 사용
//				System.out.println(" C : "+boardList); 
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
			

		return boardList;
	}
	
	
	// 게시판에 글이 있을 때 페이지에 출력되는 글목록 만환하기
	public List<BoardDTO> getBoardList(int startRow, int pageSize) {
		System.out.println(" DAO : getBoardList(int startRow, int pageSize) 호출");
		List<BoardDTO> boardList = new ArrayList<>(); // ArrayList가 List로 업캐스팅돼서 범용적 사용 가능함.
			try {
		// 4-1. 드라이버 로드 및 DB 연결
				con = getConnect();
				
		// 4-2. sql(select) 작성 & pstmt 객체 생성
//				sql = "select * from itwill_board";
				sql = "select * from itwill_board "
						+ "order by re_ref desc, re_seq asc "
						+ "limit ?, ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, startRow-1);  // 인덱스가 0부터 시작하기 때문에 1을 빼줌
				pstmt.setInt(2, pageSize);
				
		// 4-3. sql 실행 
				rs = pstmt.executeQuery();
			
		// 4-4. 데이터 처리
				while(rs.next()) {
					// 4-4-1. 데이터 있을 때 DB에 저장된 정보를 DTO에 저장
					BoardDTO dto = new BoardDTO();
					dto.setBno(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setPass(rs.getString(3));
					dto.setSubject(rs.getString(4));
					dto.setContent(rs.getString(5));
					dto.setReadcount(rs.getInt(6));
					dto.setRe_ref(rs.getInt(7));
					dto.setRe_lev(rs.getInt(8));
					dto.setRe_seq(rs.getInt(9));
					dto.setDate(rs.getDate(10));
					dto.setIp(rs.getString(11));
					dto.setFile(rs.getString(12));
					
					// 4-4-2. DTO 정보를 List에 저장
					boardList.add(dto);
				}
				System.out.println(" C : 게시판 정보 저장 완료");
				// 게시판 정보 출력하나 데이터가 많이 쌓일 경우 오래 걸리기 때문에 확인용으로만 사용
//				System.out.println(" C : "+boardList); 
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
			

		return boardList;
	}
	
// 5. 글 개수 조회(all) 메서드 getBoardCount() ---------------------------
	
	public int getBoardCount() {
		System.out.println(" DAO : getBoardCount() 호출");
		int cnt = 0; // 조회한 글의 개수
		
		try {
		// 5-1. CP를 이용한 DB 연결
			con = getConnect();
			
		// 5-2. sql 작성 & pstmt 
			sql = "select count(*) from itwill_board";
			
		// 5-3. sql 실행 후 정보 ResultSet에 담기
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
		// 5-4. 조회 결과가 있을 때 cnt 반환
			if(rs.next()) {
				return cnt = rs.getInt(1); // == rs.getInt("count(*))
			}
			
			System.out.println(" DA0 : 글 개수 : 총 "+cnt+"개");
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeDB();
			
		}
		
		
		return 0;
	}
	
	
// 6. 글 조회수 1 증가 - updateReadcount(bno)
				
//	public void updateReadcount(int bno) {
//		System.out.println(" Controller : updatReadcount() 호출");
//		
//		try {
//			con = getConnect();
//			sql = "update itwill_boards set readcount=readcount+1 where bno=?";
//			pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, bno);
//			
//			System.out.println(" DAO : 게시글 조회수 1 증가 완료");
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			closeDB();
//		}
//		
//	}
	
	// 글 조회수 1증가 - updateReadcount(bno)
		public void updateReadcount(int bno){
			System.out.println(" C : updateReadcount(int bno) 호출 ");
			
			try {
				//1.2. 디비연결
				con = getConnect();
				//3. sql 작성(update) & pstmt 객체
				sql = "update itwill_board set readcount=readcount+1 where bno = ?";
				pstmt = con.prepareStatement(sql);
				
				//???
				pstmt.setInt(1, bno);
				//4. sql 실행
				pstmt.executeUpdate();
				
				System.out.println(" DAO : 게시판글 조회수 1증가 완료!");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}
		}
	
// 7. 특정 글의 정보 조회 getBoard() {
	public BoardDTO getBoard(int bno) {
		System.out.println(" DAO : getBoard() 호출");
		BoardDTO dto=null;
		
		try {
			con = getConnect();
			sql = "select * from itwill_board where bno=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {		
				dto = new BoardDTO();
				dto.setBno(rs.getInt("bno"));
				dto.setSubject(rs.getString("subject"));
				dto.setPass(rs.getString("pass"));
				dto.setContent(rs.getString("content"));
				dto.setName(rs.getString("name"));
				dto.setFile(rs.getString("file"));
				dto.setDate(rs.getDate("date"));
				dto.setRe_lev(rs.getInt("re_lev"));
				dto.setRe_ref(rs.getInt("re_ref"));
				dto.setRe_seq(rs.getInt("re_seq"));
				dto.setReadcount(rs.getInt("readcount"));					
			}
			System.out.println(" DAO : "+bno+"번 게시글 저장 완료");
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return dto;
	}
	
			
	
	
}
