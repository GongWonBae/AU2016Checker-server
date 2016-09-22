import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//ddd
public class BeaconHandler {
	public BeaconHandler(String BeaconJson) {
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj;
			try {
				System.out.println("-------Beacon Constructor Start-------");
				jsonObj = (JSONObject) jsonParser.parse(BeaconJson);
				JSONArray memberArray = null;

				memberArray = (JSONArray) jsonObj.get("BEACON");
				for (int i = 0; i < memberArray.size(); i++) {
					JSONObject tempObj = (JSONObject) memberArray.get(i);
					String id = tempObj.get("ID").toString();
					String phone = tempObj.get("PHONE").toString();
				
					System.out.println("  ID : " + id);
					System.out.println("PHONE : " + phone );
					JSONArray infoArray = (JSONArray) tempObj.get("BEACON_INFO"); 
					
					System.out.println(""+infoArray.size());
					
					for (int j = 0; j < infoArray.size(); j++) {
				              JSONObject infoObj = (JSONObject) infoArray.get(j);
				              System.out.println(""+(j+1)+"번째 UUID : "+infoObj.get("UUID"));
				              System.out.println(""+(j+1)+"번째 MAJOR : "+infoObj.get("MAJOR"));
				              System.out.println(""+(j+1)+"번째 MINOR : "+infoObj.get("MINOR"));
				              System.out.println(""+(j+1)+"번째 DIS : "+infoObj.get("DISTANCE"));
				              System.out.println("----------------------------");    
					}
					String beacon_info = tempObj.get("BEACON_INFO").toString();
					
				}
				System.out.println("-------Beacon Constructor End-------");
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}
	
}
