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
	private String time =null;
	private String class_no =null;
	private String week=null;
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
					time = tempObj.get("TIME").toString();
					class_no = tempObj.get("CLASS_NO").toString();
					week = tempObj.get("WEEK").toString();
					System.out.println("  ID : " + id);
					System.out.println("ROOM : " + room );
					System.out.println("TIME : " + time );
					System.out.println("CLASSNO : " + class_no);					
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
				psmt = con.prepareStatement("insert into classlog values (?,?, ?,?, 'false', null,?)");
				psmt.setString(1, id);
				psmt.setString(2, room);
				psmt.setString(3, class_no);
				psmt.setString(4, time);
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
