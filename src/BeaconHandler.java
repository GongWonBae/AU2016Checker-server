import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private String attendflag = null;
	private int beacon_flag = -1; // 비콘 거리안에 있으면 0 멀면 1, 찾을수 없으면 2
	private Connection con= null;
	ResultSet rs = null;
	PreparedStatement psmt = null;
	String result = null;
	beaconinfo foundbeaon_info[] = null;
	String checker = null;

	public BeaconHandler(String BeaconJson, Connection con) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj;
		this.con=con;
		// cc
		try {
			System.out.println("-------Beacon Constructor Start-------");
			jsonObj = (JSONObject) jsonParser.parse(BeaconJson);
			JSONArray memberArray = null;

			memberArray = (JSONArray) jsonObj.get("BEACON");
			for (int i = 0; i < memberArray.size(); i++) {
				JSONObject tempObj = (JSONObject) memberArray.get(i);
				this.sid = tempObj.get("SID").toString();
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
		try {
			psmt = con.prepareStatement("select to_days(now_time), time_to_sec(now_time) from classlog"
					+ "where class_id = ? and class_no = ? and ctime = ? and week = ? ");
			rs = psmt.executeQuery();
			while (rs.next()) {
				int i = 1;
				String id = rs.getString(i++);
				String pw = rs.getString(i++);
				System.out.println(id + " " + pw);
			}

		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
		}
	}

	public int getTableDay(){
		String DAY=null;
		String TIME=null;
		try {
			psmt = con.prepareStatement("select to_days(now_time), time_to_sec(now_time) from classlog"
					+ "where class_id = ? and class_no = ? and ctime = ? and week = ? ");
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
	public int getTableTime(){
		String DAY=null;
		String TIME=null;
		try {
			psmt = con.prepareStatement("select to_days(now_time), time_to_sec(now_time) from classlog"
					+ "where class_id = ? and class_no = ? and ctime = ? and week = ? ");
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
	
	
	public String getSendmsgAndupdateDb(String sid, Connection con) {
		setBeaconflag(sid, con);
		ResultSet rs = null;
		PreparedStatement psmt = null;
		try {
			if (beacon_flag == 0) {
				psmt = con.prepareStatement(
						"select * from result where student_id= ? and class_id = ? and class_no = ? and week = ?");
				psmt.setString(1, sid);
				psmt.setString(2, class_code);
				psmt.setString(3, class_no);
				psmt.setString(4, week);
				rs = psmt.executeQuery();
				psmt = null;
				if (rs.next()) { // 이미 저장된 해당 주차 결과값이 있으면
					if (Integer.parseInt(ctime) == 1) {
						/*
						 * psmt = con.prepareStatement("insert into result " +
						 * "(student_id,class_id,class_no, week,ctime1) " +
						 * "values (?,?,?,?,'00')");
						 */
						psmt = con.prepareStatement("update result " + "set ctime1 = '00' "
								+ "where student_id= ? and class_id = ? and class_no = ? and week = ?");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 2) {
						psmt = con.prepareStatement("update result " + "set ctime2 = '00' "
								+ "where student_id= ? and class_id = ? and class_no = ? and week = ?");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 3) {
						psmt = con.prepareStatement("update result " + "set ctime3 = '00' "
								+ "where student_id= ? and class_id = ? and class_no = ? and week = ?");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.executeUpdate();
					}
				} else {
					if (Integer.parseInt(ctime) == 1) {
						psmt = con.prepareStatement("insert into result "
								+ "(student_id,class_id,class_no, week,ctime1) " + "values (?,?,?,?,'00')");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 2) {
						psmt = con.prepareStatement("insert into result "
								+ "(student_id,class_id,class_no, week,ctime2) " + "values (?,?,?,?,'00')");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.executeUpdate();
					} else if (Integer.parseInt(ctime) == 3) {
						psmt = con.prepareStatement("insert into result "
								+ "(student_id,class_id,class_no, week,ctime3) " + "values (?,?,?,?,'00')");
						psmt.setString(1, sid);
						psmt.setString(2, class_code);
						psmt.setString(3, class_no);
						psmt.setString(4, week);
						psmt.executeUpdate();
					}
				}
				/*
				 * 위에서 DB반영 끝내고 아래부분에서 결과값을 만들어 전송한다
				 */

				return checker;
			} else {
				return checker;
			}
		} catch (SQLException sqex) {
			System.out.println("SQLException: " + sqex.getMessage());
			System.out.println("SQLState: " + sqex.getSQLState());
			return "Beacon handler getSendAndupdateDb is not working";
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
}
