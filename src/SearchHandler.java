import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
public class SearchHandler {
	private int subject_num = 0;
	private String sid =null;
	ResultSet rs = null;
	PreparedStatement psmt = null;
	String result=null;
	public SearchHandler(String logSID, String SearchJson , Connection con) {
		System.out.println("-------SearchHandler Constructor Start-------");
		sid=logSID;
		JSONObject Jsonobj = new JSONObject();
		JSONObject jobj = null;
		JSONArray jarry=new JSONArray();
		ArrayList<String> jsonlist = new ArrayList<String>();
		try {
			//psmt = con.prepareStatement("select * from take,classlog where classlog.class_id=take.class_id and take.student_id='201131046' and classlog.available ='true'");
			psmt = con.prepareStatement("select class.class_id, classlog.class_no, class.name from take,classlog, class  where classlog.class_id=take.class_id and take.student_id=? and classlog.available ='true' and class.class_id=classlog.class_id");
			psmt.setString(1, sid);
			rs = psmt.executeQuery();
			if(rs.next()){
				do {
					jobj = new JSONObject();
					int i = 1;
					String classid = rs.getString(i++);
					String classno = rs.getString(i++);
					String classname= rs.getString(i++);
					//System.out.println(classid + " " + classno + " " + classname + " ");
					jobj.put("CLASS_NAME",classname);
					jobj.put("CLASS_NO",classno);
					jobj.put("CLASS_ID",classid);
					jarry.add(jobj);
					subject_num++;
				}while (rs.next());
				Jsonobj.put("SUBJECT",jarry);
				Jsonobj.put("SUBJECT_NUM",subject_num);
				jarry= new JSONArray();
				jarry.add(Jsonobj);
				Jsonobj=new JSONObject();
				Jsonobj.put("SEARCH_RESULT", jarry);
				result =Jsonobj.toJSONString();
				//System.out.println(result);
			}
			else {
				System.out.println("you don't have available subject ");
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("-------SearchHandler Constructor End-------");
	}
	public String getSearchResultJson(){
		return result;
	}
}
