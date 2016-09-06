import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OpenHandler {
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
					String id = tempObj.get("CODE").toString();
					String room = tempObj.get("ROOM").toString();
					String time = tempObj.get("TIME").toString();
					String class_no = tempObj.get("CLASS_NO").toString();
					System.out.println("  ID : " + id);
					System.out.println("ROOM : " + room );
					System.out.println("TIME : " + time );
					System.out.println("CLASSNO2 : " + class_no);					
				}
				System.out.println("-------Open Constructor End-------");
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
	
}
