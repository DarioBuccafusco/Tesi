package it.asirchia.utils.gps;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import org.json.*;

import it.asirchia.utils.gps.simulator.GPSsimulator;		
import it.asirchia.utils.gps.simulator.model.Location;
import it.asirchia.utils.gps.simulator.model.Route;

public class TestMove {

	@Test
	public void testGeoJson() throws JSONException, IOException {	
				
		String geojson = "{\"coordinates\": [[13.367024660110472,38.110868954356164],[13.36509883403778,38.11003322712951],[13.363317847251892,38.10979685809846],[13.361665606498718,38.10922281583876],[13.35639238357544,38.10818024225713],[13.35511028766632,38.10801140303019],[13.352980613708496,38.10999101843005],[13.351027965545654,38.11140499658424],[13.349815607070923,38.110936487451895],[13.350706100463865,38.10987283394187],[13.352830410003662,38.10994880970624],[13.3541339635849,38.108779618361176],[13.353812098503113,38.10756397719154],[13.349499106407166,38.103642552353165],[13.347787857055662,38.10153612423879],[13.345738649368286,38.097707250700495],[13.34260582923889,38.09582862306645],[13.342782855033875,38.095739967535415],[13.345475792884827,38.097175329574846],[13.348345756530762,38.10199202966138],[13.350775837898254,38.104604988272925],[13.353323936462402,38.10682951751515],[13.356547951698303,38.10815491639795],[13.35985243320465,38.108899915085246],[13.36050420999527,38.107882662857435],[13.363044261932373,38.109060310409205],[13.36410105228424,38.107420462662816],[13.366187810897827,38.108427168923846],[13.36522489786148,38.109864392185386],[13.366152942180634,38.110250601545424],[13.366415798664093,38.110132417477196],[13.367064893245695,38.11045742320468],[13.367024660110472,38.110868954356164]]}";
		JSONObject json = new JSONObject(geojson);
	
		
		Route simulatedRoute = new Route();
			
		JSONArray coordinates = json.getJSONArray("coordinates");
		for (int i = 0; i < coordinates.length(); i++) {
			JSONArray lat_lng = coordinates.getJSONArray(i);
			simulatedRoute.addWayPoint(new Location("Waypoint " + i, lat_lng.getDouble(0), lat_lng.getDouble(1)));
		}
		
		GPSsimulator gpsSimulator = new GPSsimulator();
		gpsSimulator.setSimulatedRoute(simulatedRoute);
		
		
		String muuid = UUID.randomUUID().toString();

		for (int i = 0; i < 20000; i++) { // testing 500 steps of the simulator cambiato in 20000 per testare l'arrivo
			gpsSimulator.move(muuid);	
		}
	}
	
	public void testPath() throws IOException, JSONException {
		
		Route simulatedRoute = new Route();
		simulatedRoute.addWayPoint(new Location("Start", 48.138083, 11.561102));
		simulatedRoute.addWayPoint(new Location("Waypoint 1", 48.137413,11.561020));
		simulatedRoute.addWayPoint(new Location("Waypoint 2", 48.137370, 11.564539));
		simulatedRoute.addWayPoint(new Location("Waypoint 3", 48.137449, 11.565000));
		simulatedRoute.addWayPoint(new Location("End", 48.137578, 11.565311));

		GPSsimulator gpsSimulator = new GPSsimulator();
		gpsSimulator.setSimulatedRoute(simulatedRoute);

		for (int i = 0; i < 500; i++) { // testing 500 steps of the simulator
			//gpsSimulator.move();
		}
	}

}
