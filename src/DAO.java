/*
 * ip : DriverManager.getConnection("jdbc:mysql://52.79.139.169:3306/att","root", "beacon");
 * mysql 서버
 * 
 * */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DAO {
	public String db_ip =null;
	public String port = null;
	public String db_id = null;
	public String db_pw = null;
	public String db_name=null;
	Connection con = null;
	ResultSet rs = null;
	java.sql.Statement st = null;
	PreparedStatement psmt = null; //쿼리부, ?에 값을 주기 위해 PrepareStatement사용
	
	/*생성자 */
	/*public  DAO(String IP, String port, String id, String pw) throws SQLException{
		 this.db_ip=IP;
		 this.port = port;
		 this.db_id = id;
		 this.db_pw =pw;
		
		 con = DriverManager.getConnection("jdbc:mysql:"+db_ip+":"+port,db_id, db_pw);
		 
	}*/
	/* 생성자2  // 디폴트 DB서버 */
	public  DAO() {
		 init();
	}
	private void init() { //드라이버 로딩 메서트
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("드라이버 로딩 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		}
	}
	
	public Connection getMyConnection(){
		// this.db_ip= "jdbc:mysql://52.79.139.169:3306/att";
		 this.db_ip= "52.79.139.169";
		 this.port="3306";
		 this.db_id = "root";
		 this.db_pw ="beacon";
		 this.db_name="att";
		 String ConnectionString = "jdbc:mysql://"+db_ip+":"+port+"/"+db_name;
		 try {
			// con = DriverManager.getConnection("jdbc:mysql://52.79.139.169:3306/att","root", "beacon");
			con = DriverManager.getConnection(ConnectionString,db_id, db_pw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	public Connection getConnection(String IP, String port, String id, String pw){
		 this.db_ip=IP;
		 this.port = port;
		 this.db_id = id;
		 this.db_pw =pw;
		
		 try {
			con = DriverManager.getConnection("jdbc:mysql:"+db_ip+":"+port+"/att",db_id, db_pw);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	
	/*SELECT용*/ //좀더 수정해야함.
	public ResultSet getQuery(String query) throws SQLException{
		psmt = con.prepareStatement(query);
		rs = psmt.executeQuery();
		return rs;
	}
	
	/*INSERT, DELETE, UPDATE쿼리용 true = 수행한 행의 수가 존재 , false =수행한 행의 수가 0일때 */
	public boolean setQuery(String query) throws SQLException{
		psmt = con.prepareStatement(query);
		int num = psmt.executeUpdate();
		if(num==0)return false; 
		return true; 
	}
	
}
