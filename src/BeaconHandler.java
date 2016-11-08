import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mysql.jdbc.Connection;

public class BeaconHandler {
	private String sid = null;
	private String class_code = null;
	private String class_no = null;
	private String beacon_cnt = null;
	private String week = null;
	private String classroom = null;
	private String ctime = null;
	private String attend_flag = null;
	private int beacon_flag = 2; // 비콘 거리안에 있으면 0 멀면 1, 찾을수 없으면 2
	private Connection con = null;
	int LATE_TIME_SECOND = 900; // 900초 15분
	ResultSet rs = null;
	PreparedStatement psmt = null;
	String result = null;
	beaconinfo foundbeaon_info[] = null;
	String checker = null;

	public BeaconHandler(String BeaconJson, Connection con, String sid) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj;
		this.con = con;
		this.sid = sid;
		// cc
		try {
			System.out.println("-------Beacon Constructor Start-------");
			jsonObj = (JSONObject) jsonParser.parse(BeaconJson);
			JSONArray memberArray = null;

			memberArray = (JSONArray) jsonObj.get("BEACON");
			for (int i = 0; i < memberArray.size(); i++) {
				JSONObject tempObj = (JSONObject) memberArray.get(i);
				this.beacon_cnt = tempObj.get("BEACON_CNT").toString();
				this.class_code = tempObj.get("CLASS_CODE").toString();
				this.class_no = tempObj.get("CLASS_NO").toString();
				this.week = tempObj.get("WEEK").toString();
				this.classroom = tempObj.get("CLASSROOM").toString();
				this.ctime = tempObj.get("CTIME").toString();

				System.out.println(" SID : " + sid);
				System.out.println("CLASS_CODE : " + class_code);
				System.out.println("CLASS_NO : " + class_no);
				System.out.println("BEACON_CNT : " + beacon_cnt);
				System.out.println("CTIME : " + ctime);
				JSONArray infoArray = (JSONArray) tempObj.get("BEACON_INFO");

				System.out.println("감지된 비콘수 : " + infoArray.size());
				foundbeaon_info = new beaconinfo[infoArray.size()];
				for (int k = 0; k < infoArray.size(); k++) {
					foundbeaon_info[k] = new beaconinfo();
				}
				for (int j = 0; j < infoArray.size(); j++) {

					JSONObject infoObj = (JSONObject) infoArray.get(j);
					foundbeaon_info[j].uuid = infoObj.get("UUID").toString();
					foundbeaon_info[j].major = Integer.parseInt(infoObj.get("MAJOR").toString());
					foundbeaon_info[j].minor = Integer.parseInt(infoObj.get("MINOR").toString());
					foundbeaon_info[j].distance = Double.parseDouble(infoObj.get("DISTANCE").toString());

					System.out.println("" + (j + 1) + "번째 UUID : " + foundbeaon_info[j].uuid);
					System.out.println("" + (j + 1) + "번째 MAJOR : " + foundbeaon_info[j].major);
					System.out.println("" + (j + 1) + "번째 MINOR : " + foundbeaon_info[j].minor);
					System.out.println("" + (j + 1) + "번째 DIS : " + foundbeaon_info[j].distance);
					System.out.println("----------------------------");

				}
				// String beacon_info = tempObj.get("BEACON_INFO").toString();
				// System.out.println(beacon_info);
			}
			System.out.println("-------Beacon Constructor End-------");
		} catch (ParseException e) {

			e.printStackTrace();
		}

	}

	public void setBeaconflag(String sid, Connection con) {
		ResultSet rs = null;
		PreparedStatement psmt = null;
		if (foundbeaon_info != null) {
			try {
				psmt = con.prepareStatement(
						"select beacon_list.uuid , beacon_list.major , beacon_list.minor , beacon_list.distance from beacon_list, classroom where beacon_list.class_id = classroom.classroom_id and classroom.classroom_id = ? ");
				psmt.setString(1, classroom);
				rs = psmt.executeQuery();
				int k = 0;
				while (rs.next()) {
					int i = 1;
					String tempuuid = rs.getString(i++);
					int tempmajor = rs.getInt(i++);
					int tempminor = rs.getInt(i++);
					double tempdistance = rs.getDouble(i++);
					System.out.println(tempuuid + " " + tempmajor + " " + tempminor + " " + tempdistance);
					for (int j = 0; j < foundbeaon_info.length; j++) {
						if (tempuuid.equals(foundbeaon_info[j].uuid) && tempmajor == foundbeaon_info[j].major
								&& tempminor == foundbeaon_info[j].minor) {
							if (foundbeaon_info[j].distance <= tempdistance) {
								checker = "OK";
								beacon_flag = 0;
								System.out.println("찾음");
								break;
							} else {
								checker = "too far distance";
								beacon_flag = 1;
								break;
							}
						} else {
							checker = "can not find beacon of your class";
							beacon_flag = 2;
						}

					}
					k++;
					System.out.println(k);
				}
			} catch (SQLException sqex) {
				System.out.println("SQLException: " + sqex.getMessage());
				System.out.println("SQLState: " + sqex.getSQLState());
			}
		}

	}

	public void setAttendflag(String sid, Connection con) {
		int tempDay = 0;
		
		int tempTime = 0;
		try {
			psmt = con.prepareStatement("select to_days(now()), time_to_sec(now())");
			rs = psmt.executeQuery();
			while (rs.next()) {
				int i = 1;
				tempDay = rs.getInt(i++);
				tempTime = rs.getInt(i++);
			}
			if (tempDay == getDbDay()) {
				if (tempTime <= getDbTime() + getDb_stime()) {
					attend_flag = "00"; // 출석
				} else if (tempTime <= getDbTime() + getDb_stime() + LATE_TIME_SECOND) {
					attend_flag = "01"; // 지각
				} else {
					attend_flag = "11"; // 결석
				}
			} else {
				attend_flag = "11";
			}

		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
	}

	public int getDbDay() {
		String DAY = null;
		String TIME = null;
		try {
			psmt = con.prepareStatement("select to_days(now_time), time_to_sec(now_time) from classlog"
					+ " where class_id = ? and class_no = ? and ctime = ? and week = ? ");
			psmt.setString(1, class_code);
			psmt.setString(2, class_no);
			psmt.setString(3, ctime);
			psmt.setString(4, week);
			rs = psmt.executeQuery();
			while (rs.next()) {
				int i = 1;
				DAY = rs.getString(i++);
				TIME = rs.getString(i++);
				break;
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return Integer.parseInt(DAY);
	}
	

	public int getDbTime() {
		String DAY = null;
		String TIME = null;
	
		try {
			psmt = con.prepareStatement("select to_days(now_time), time_to_sec(now_time) from classlog"
					+ " where class_id = ? and class_no = ? and ctime = ? and week = ? ");
			psmt.setString(1, class_code);
			psmt.setString(2, class_no);
			psmt.setString(3, ctime);
			psmt.setString(4, week);
			rs = psmt.executeQuery();
			while (rs.next()) {
				int i = 1;
				DAY = rs.getString(i++);
				TIME = rs.getString(i++);
				break;
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return Integer.parseInt(TIME);
	}

	public int getDb_stime() {

		String TIME = null;
		try {
			psmt = con.prepareStatement(
					"select s_time from classlog" + " where class_id = ? and class_no = ? and ctime = ? and week = ? ");
			psmt.setString(1, class_code);
			psmt.setString(2, class_no);
			psmt.setString(3, ctime);
			psmt.setString(4, week);
			rs = psmt.executeQuery();
			while (rs.next()) {
				int i = 1;
				TIME = rs.getString(i++);
				break;
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
		return Integer.parseInt(TIME);
	}

	public int attendUpdataDB(String sid, Connection con) {
		setBeaconflag(sid, con);
		setAttendflag(sid, con);
		ResultSet rs = null;
		PreparedStatement psmt = null;
		System.out.println("TEST : "+Integer.parseInt(ctime));
		int update_num = 0;
		try {
			if (beacon_flag == 0) {
				psmt = con.prepareStatement("select ctime1, ctime2, ctime3 from result where student_id= ? and class_id = ? and class_no = ? and week = ?");
				psmt.setString(1, sid);
				psmt.setString(2, class_code);
				psmt.setString(3, class_no);
				psmt.setString(4, week);
				rs = psmt.executeQuery();
				//psmt = null;
				System.out.println("LIEN test");
				if (rs.next()) { // 이미 저장된 해당 주차 결과값이 있으면
					int i = 1;
					String dbctime1 = rs.getString(i++);
					String dbctime2 = rs.getString(i++);
					String dbctime3 = rs.getString(i++);
					
					if (Integer.parseInt(ctime) == 1 && dbctime1.equals(null)) {
						psmt = con.prepareStatement("update result " + "set ctime1 = ? "
								+ "where student_id= ? and class_id = ? and class_no = ? and week = ?");
						psmt.setString(1, attend_flag);
						psmt.setString(2, sid);
						psmt.setString(3, class_code);
						psmt.setString(4, class_no);
						psmt.setString(5, week);
						update_num = psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 2 && dbctime2.equals(null)) {
						psmt = con.prepareStatement("update result " + "set ctime2 = ? "
								+ "where student_id= ? and class_id = ? and class_no = ? and week = ?");
						psmt.setString(1, attend_flag);
						psmt.setString(2, sid);
						psmt.setString(3, class_code);
						psmt.setString(4, class_no);
						psmt.setString(5, week);
						update_num = psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 3  && dbctime3.equals(null)) {
						psmt = con.prepareStatement("update result " + "set ctime3 = ?  "
								+ "where student_id= ? and class_id = ? and class_no = ? and week = ?");
						psmt.setString(1, attend_flag);
						psmt.setString(2, sid);
						psmt.setString(3, class_code);
						psmt.setString(4, class_no);
						psmt.setString(5, week);
						update_num = psmt.executeUpdate();
					}
				} else {
					if (Integer.parseInt(ctime) == 1) {
						psmt = con.prepareStatement("insert into result "
								+ "(student_id,class_id,class_no, week,ctime1) " + "values (?,?,?,?,?)");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.setString(5, attend_flag);
						update_num = psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 2) {
						psmt = con.prepareStatement("insert into result "
								+ "(student_id,class_id,class_no, week,ctime2) " + "values (?,?,?,?,?)");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.setString(5, attend_flag);
						update_num = psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 3) {
						psmt = con.prepareStatement("insert into result "
								+ "(student_id,class_id,class_no, week,ctime3) " + "values (?,?,?,?,?)");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.setString(5, attend_flag);
						update_num = psmt.executeUpdate();
					}
				}
				/*
				 * 위에서 DB반영 끝내고 아래부분에서 결과값을 만들어 전송한다
				 */

				return update_num;
			} else {
				return update_num;
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
			return update_num;
		}

	}

	public class beaconinfo {
		public String uuid = null;
		public int major = 0;
		public int minor = 0;
		public double distance = 0;

		public beaconinfo() {
			uuid = null;
			major = -1;
			minor = -1;
			distance = -1;
		}

	}

	public String getAttendJson(Connection con) {
		JSONObject Jsonobj = new JSONObject();
		JSONArray jarry = new JSONArray();
		int update_num = 0;
		String tempctime = null;
		System.out.println("첵쿠!");
		

		try {
			// psmt = con.prepareStatement("select * from take,classlog where
			// classlog.class_id=take.class_id and take.student_id='201131046'
			// and classlog.available ='true'");
			if(beacon_flag==0){
		
				switch (Integer.parseInt(ctime)) {
				case 1:
					psmt = con.prepareStatement(
							" select class.class_id, class.class_no, class.name, result.dttm, result.week, result.ctime1 "
									+ "from result, class " + "where class.class_id = result.class_id and "
									+ "class.class_no = result.class_no and " + "result.student_id = ? and "
									+ "result.week = ? and class.class_id = ? and class.class_no = ? ");
					break;
				case 2:
					psmt = con.prepareStatement(
							" select class.class_id, class.class_no, class.name, result.dttm, result.week, result.ctime2 "
									+ "from result, class " + "where class.class_id = result.class_id and "
									+ "class.class_no = result.class_no and " + "result.student_id = ? and "
									+ "result.week = ? and class.class_id = ? and class.class_no = ? ");
					break;
				case 3:
					psmt = con.prepareStatement(
							" select class.class_id, class.class_no, class.name, result.dttm, result.week, result.ctime3 "
									+ "from result, class " + "where class.class_id = result.class_id and "
									+ "class.class_no = result.class_no and " + "result.student_id = ? and "
									+ "result.week = ? and class.class_id = ? and class.class_no = ? ");
					break;
				default:
					break;
				}
				
			
		/*	psmt = con.prepareStatement(
					" select class.class_id, class.class_no, class.name, result.dttm, result.week, result.? "
							+ "from result, class " + "where class.class_id = result.class_id and "
							+ "class.class_no = result.class_no and " + "result.student_id = ? and "
							+ "result.week = ? and class.class_id = ? and class.class_no = ? ");
			psmt.setString(1, tempctime);
			psmt.setString(2, sid);
			psmt.setString(3, week);
			psmt.setString(4, class_code);
			psmt.setString(5, class_no);*/
			psmt.setString(1, sid);
			psmt.setString(2, week);
			psmt.setString(3, class_code);
			psmt.setString(4, class_no);
			
			rs = psmt.executeQuery();
			if (rs.next()) {
				Jsonobj = new JSONObject();
				int i = 1;
				String classid = rs.getString(i++);
				String classno = rs.getString(i++);
				String classname = rs.getString(i++);
				String dttm = rs.getString(i++);
				String week = rs.getString(i++);
				String myctime = rs.getString(i++);
				System.out.println(classid + " " + classno + " " + classname);
				Jsonobj.put("CLASS_NAME", classname);
				Jsonobj.put("CLASS_NO", classno);
				Jsonobj.put("CLASS_ID", classid);
				Jsonobj.put("WEEK", week);
				Jsonobj.put("DATE", dttm);
				Jsonobj.put("BEACON_FLAG", beacon_flag);
				Jsonobj.put("ATTEND_FLAG", myctime);
				Jsonobj.put("CTIME",tempctime);
				jarry = new JSONArray();
				jarry.add(Jsonobj);
				Jsonobj = new JSONObject();
				Jsonobj.put("ATTEND_RESULT", jarry);
			} else {
				Jsonobj = new JSONObject();
				int i = 1;
				Jsonobj.put("CLASS_NAME", null);
				Jsonobj.put("CLASS_NO", class_no);
				Jsonobj.put("CLASS_ID", class_code);
				Jsonobj.put("WEEK", week);
				Jsonobj.put("TIME", null);
				Jsonobj.put("BEACON_FLAG", beacon_flag);
				Jsonobj.put("ATTEND_FLAG", null);
				Jsonobj.put("CTIME",tempctime);
				jarry = new JSONArray();
				jarry.add(Jsonobj);
				Jsonobj = new JSONObject();
				Jsonobj.put("ATTEND_RESULT", jarry);

			}
			}else{
				Jsonobj = new JSONObject();
				int i = 1;
				Jsonobj.put("CLASS_NAME", null);
				Jsonobj.put("CLASS_NO", class_no);
				Jsonobj.put("CLASS_ID", class_code);
				Jsonobj.put("WEEK", week);
				Jsonobj.put("TIME", null);
				Jsonobj.put("BEACON_FLAG", beacon_flag);
				Jsonobj.put("ATTEND_FLAG", null);
				Jsonobj.put("CTIME",tempctime);
				jarry = new JSONArray();
				jarry.add(Jsonobj);
				Jsonobj = new JSONObject();
				Jsonobj.put("ATTEND_RESULT", jarry);
			}
			result = Jsonobj.toJSONString();
			// System.out.println(result);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

}
