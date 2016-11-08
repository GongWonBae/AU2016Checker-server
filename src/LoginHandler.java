import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.Connection;

import io.netty.util.CharsetUtil;

public class LoginHandler {
	private String SID=null;
	private String PW=null;
	private String name=null;
	public LoginHandler(String logJson) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj;
		try {
			System.out.println("-------Login Constructor Start-------"); //생성자는 파싱을 끝내고 sid와 pw를 임시 저장한다.
			jsonObj = (JSONObject) jsonParser.parse(logJson);
			JSONArray memberArray = null;
			memberArray = (JSONArray) jsonObj.get("Login");
			for (int i = 0; i < memberArray.size(); i++) {
				JSONObject tempObj = (JSONObject) memberArray.get(i);
				SID = tempObj.get("ID").toString();
				PW = tempObj.get("PW").toString();
				System.out.println(" SID : " + SID);
				System.out.println("  PW : " + PW);
			}
			System.out.println("-------Login Constructor End-------");
		} catch (ParseException e) {

			e.printStackTrace();
		}
	}
	public String getSID(){
		return SID;
	}
	public String getPW(){
		return PW;
	}
	public String getname(){
		return name;
	}
	public boolean CheckLogin(Connection con){
		System.out.println("-------Login Check Start-------"); 
		ResultSet rs = null;
		PreparedStatement psmt = null;
		try {
			psmt = con.prepareStatement("select * from user ");
			rs = psmt.executeQuery();
			while (rs.next()) {
				int i = 1;
				String id = rs.getString(i++);
				String pw = rs.getString(i++);
				this.name = rs.getString(i++);
				String phone = rs.getString(i++);
				String major = rs.getString(i++);
				String type = rs.getString(i++);
			//	System.out.println(id + " " + pw + " " + name + " " + phone + " " + major + " " + type);
				if (id.equals(SID) && pw.equals(PW)) {
					System.out.println(id + " " + pw + " " + name + " " + phone + " " + major + " " + type);
					System.out.println("-------Login Check End [True]-------");
					return true;
				}else{
					name=null;
				}
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		System.out.println("-------Login Check End [False]-------");
		return false;
	}
	public String SetLoginResultJson(boolean Logresult){
		String str =null;
		JSONObject Jsonobj = new JSONObject();
		JSONObject jobj = null;
		JSONArray jarry=new JSONArray();
		if(Logresult==true){	
			jobj = new JSONObject();
			jobj.put("LOGFLAG","TRUE");
			jobj.put("NAME", name);
			jarry= new JSONArray();
			jarry.add(jobj);
			Jsonobj=new JSONObject();
			Jsonobj.put("LOGIN_RESULT", jarry);
			str =Jsonobj.toJSONString();
		
		}else{
			jobj = new JSONObject();
			jobj.put("LOGFLAG","FALSE");
			jobj.put("NAME", name);
			jarry= new JSONArray();
			jarry.add(jobj);
			Jsonobj=new JSONObject();
			Jsonobj.put("LOGIN_RESULT", jarry);
			str =Jsonobj.toJSONString();
		}
		System.out.println("SetLoginResultJson : "+str);
		return str;
	}
	
}
