/*
 * ip : DriverManager.getConnection("jdbc:mysql://52.79.139.169:3306/att","root", "beacon");
 * mysql ����
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
	PreparedStatement psmt = null; //������, ?�� ���� �ֱ� ���� PrepareStatement���
	
	/*������ */
	/*public  DAO(String IP, String port, String id, String pw) throws SQLException{
		 this.db_ip=IP;
		 this.port = port;
		 this.db_id = id;
		 this.db_pw =pw;
		
		 con = DriverManager.getConnection("jdbc:mysql:"+db_ip+":"+port,db_id, db_pw);
		 
	}*/
	/* ������2  // ����Ʈ DB���� */
	public  DAO() {
		 init();
	}
	private void init() { //����̹� �ε� �޼�Ʈ
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("����̹� �ε� ����");
		} catch (ClassNotFoundException e) {
			System.out.println("����̹� �ε� ����");
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
	
	
	/*SELECT��*/ //���� �����ؾ���.
	public ResultSet getQuery(String query) throws SQLException{
		psmt = con.prepareStatement(query);
		rs = psmt.executeQuery();
		return rs;
	}
	
	/*INSERT, DELETE, UPDATE������ true = ������ ���� ���� ���� , false =������ ���� ���� 0�϶� */
	public boolean setQuery(String query) throws SQLException{
		psmt = con.prepareStatement(query);
		int num = psmt.executeUpdate();
		if(num==0)return false; 
		return true; 
	}
	
}
