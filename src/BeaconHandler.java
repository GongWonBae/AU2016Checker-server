import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.Connection;
import com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl;

public class BeaconHandler {
	private String sid =null;
	private String phone =null;
	private String nowtime =null;
	private String class_code =null;
	private String class_no =null;
	private String beacon_cnt =null;
	ResultSet rs = null;
	PreparedStatement psmt = null;
	String result=null;
	beaconinfo foundbeaon_info []=null;
	String checker = null;
	String merge=null;
	public BeaconHandler(String BeaconJson) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj;
		//cc
		try {
			System.out.println("-------Beacon Constructor Start-------");
			jsonObj = (JSONObject) jsonParser.parse(BeaconJson);
			JSONArray memberArray = null;

			memberArray = (JSONArray) jsonObj.get("BEACON");
			for (int i = 0; i < memberArray.size(); i++) {
				JSONObject tempObj = (JSONObject) memberArray.get(i);
				this.sid = tempObj.get("SID").toString();
				this.phone = tempObj.get("PHONE").toString();
				this.beacon_cnt=tempObj.get("BEACON_CNT").toString();
				this.class_code=tempObj.get("CLASS_CODE").toString();
				this.class_no=tempObj.get("CLASS_NO").toString();
				this.nowtime=tempObj.get("NOWTIME").toString();
				
				
				System.out.println(" SID : " + sid);
				System.out.println("PHONE : " + phone );
				System.out.println("CLASS_CODE : " + class_code );
				System.out.println("CLASS_NO : " + class_no );
				System.out.println("BEACON_CNT : " + beacon_cnt );
				System.out.println("NOWTIME : " + nowtime );
			
				JSONArray infoArray = (JSONArray) tempObj.get("BEACON_INFO"); 
				
				System.out.println("감지된 비콘수 : "+infoArray.size());
				 foundbeaon_info= new beaconinfo[infoArray.size()];
			    for(int k=0; k<infoArray.size(); k++){
			    	foundbeaon_info[k]=new beaconinfo();
			    }
				for (int j = 0; j < infoArray.size(); j++) {
					
			              JSONObject infoObj = (JSONObject) infoArray.get(j);
			              foundbeaon_info[j].uuid=infoObj.get("UUID").toString();
			              foundbeaon_info[j].major=Integer.parseInt(infoObj.get("MAJOR").toString());
			              foundbeaon_info[j].minor=Integer.parseInt(infoObj.get("MINOR").toString());
			              foundbeaon_info[j].distance=Double.parseDouble(infoObj.get("DISTANCE").toString());
			              
			              System.out.println(""+(j+1)+"번째 UUID : "+foundbeaon_info[j].uuid);
			              System.out.println(""+(j+1)+"번째 MAJOR : "+foundbeaon_info[j].major);
			              System.out.println(""+(j+1)+"번째 MINOR : "+foundbeaon_info[j].minor);
			              System.out.println(""+(j+1)+"번째 DIS : "+foundbeaon_info[j].distance);
			              System.out.println("----------------------------");
			              
			             
				}
				//String beacon_info = tempObj.get("BEACON_INFO").toString();
				//System.out.println(beacon_info);
			}
			System.out.println("-------Beacon Constructor End-------");
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
	}
	
	public void judgement(String sid, Connection con){
		ResultSet rs = null;
		PreparedStatement psmt = null;
		if(foundbeaon_info!=null){
			try {
				psmt = con.prepareStatement("select beacon_list.uuid , beacon_list.major , beacon_list.minor , beacon_list.distance from beacon_list, classroom where beacon_list.class_id = classroom.classroom_id");
				rs = psmt.executeQuery();
				while (rs.next()) {
					int i = 1;
					String tempuuid = rs.getString(i++);
					int tempmajor = rs.getInt(i++);
					int tempminor = rs.getInt(i++);
					double tempdistance = rs.getDouble(i++);
					System.out.println(tempuuid+" "+ tempmajor + " " + tempminor+ " "+ tempdistance);
					for(int j=0; j<foundbeaon_info.length;j++){
						if(tempuuid.equals(foundbeaon_info[j].uuid) && tempmajor==foundbeaon_info[j].major && tempminor==foundbeaon_info[j].minor){
							if(foundbeaon_info[j].distance<=tempdistance){
								checker="OK";
								break;
							}
							else {
								checker = "too far distance";
							}
						}
						else{
							checker ="can not find beacon of your class";
						}
							
					}
				}
			} catch (SQLException sqex) {
				System.out.println("SQLException: " + sqex.getMessage());
				System.out.println("SQLState: " + sqex.getSQLState());
			}
		}
		
	}
	public class beaconinfo {
		public String uuid=null;
		public int major=0;
		public int minor=0;
		public double distance=0;
		public beaconinfo() {
			uuid=null;
			major=-1;
			minor=-1;
			distance=-1;
		}
		
	}
}
