import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
public class OpenHandler {
	private String id =null;
	private String room =null;
	private String s_time =null;
	private String class_no =null;
	private String week=null;
	private String ctime = null;
	
	public OpenHandler(String BeaconJson) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj;
			try {
				System.out.println("-------Open Constructor Start-------");
				jsonObj = (JSONObject) jsonParser.parse(BeaconJson);
				JSONArray memberArray = null;

				memberArray = (JSONArray) jsonObj.get("OPEN");
				for (int i = 0; i < memberArray.size(); i++) {
					JSONObject tempObj = (JSONObject) memberArray.get(i);
					id = tempObj.get("CODE").toString();
					room = tempObj.get("ROOM").toString();
					class_no = tempObj.get("CLASS_NO").toString();
					s_time = tempObj.get("S_TIME").toString();
					week = tempObj.get("WEEK").toString();
					ctime=tempObj.get("CTIME").toString();
					System.out.println("  ID : " + id);
					System.out.println("ROOM : " + room );
					System.out.println("CLASSNO : " + class_no);
					System.out.println("TIME : " + s_time );
					System.out.println("WEEK : "+week);
					System.out.println("CTIME : "+ctime);
				}
				System.out.println("-------Open Constructor End-------");
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
	public void OpenDB(String sid, Connection con){
		System.out.println("-------OpenDB Start-------");
		ResultSet rs = null;
		PreparedStatement psmt = null;
		
			try {
				//psmt = con.prepareStatement("insert into classlog values ('AN1025','b903', '01','31', 'false', null)");
				psmt = con.prepareStatement("insert into classlog "
						+ "(class_id,classroom_id,class_no,s_time,available,week,ctime) "
						+ "values (?,?,?,?,'true',?,?)");
				psmt.setString(1, id);
				psmt.setString(2, room);
				psmt.setString(3, class_no);
				psmt.setString(4, s_time);
				psmt.setInt(5, Integer.parseInt(week));
				psmt.setInt(6, Integer.parseInt(ctime));
				if(psmt.executeUpdate()==0){
					System.out.println("Open DB is not updated..");
				}
				//	rs = psmt.executeQuery();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("-------  OpenDB  End-------");
		// 
		
	}
	
	
}
