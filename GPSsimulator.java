package it.asirchia.utils.gps.simulator;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import it.asirchia.utils.gps.simulator.model.Location;
import it.asirchia.utils.gps.simulator.model.Route;
import it.asirchia.utils.gps.simulator.utils.GeoHelper;

public class GPSsimulator {

	public static final double SIMULATOR_MOVEMENT_SPEED = 0.0001	; // ~0.05m - 0.1m per step 0.0015km 1,5m/s => 0.0001 10m/s
	public static final double ARRIVAL_RADIUS_IN_KM = 0.33 / 1000;  // 0.05m => 0.33
	
	private boolean repeat = false;
	private boolean finished = false; 			

	public JSONArray route = new JSONArray();
	public Location currentLocation;    
    public int waypointCounter = 0;	
    public Route simulatedRoute;
    
    public void setRepeat(boolean repeat){
    	this.repeat = repeat;
    }
    
    public boolean isFinished(){
    	return this.finished;
    }
    
    public Location getCurrentLocation() {
		return currentLocation;
	}

    public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

    public int getWaypointCounter() {
		return waypointCounter;
	}

    public void setWaypointCounter(int waypointCounter) {
		this.waypointCounter = waypointCounter;
	}

    public Route getSimulatedRoute() {
		return simulatedRoute;
	}

    public void setSimulatedRoute(Route simulatedRoute) {
		this.simulatedRoute = simulatedRoute;
		this.currentLocation = simulatedRoute.waypoints.get(0);
		waypointCounter++;
	}
    
	public void move(String muuid) throws IOException, JSONException{
		
		
    			
		Location nextWaypoint = simulatedRoute.waypoints.get(waypointCounter);
    	if (GeoHelper.calcGeoDistanceInKm(currentLocation, nextWaypoint) < ARRIVAL_RADIUS_IN_KM) {
    		waypointCounter++;
    		if (waypointCounter > simulatedRoute.waypoints.size()-1) {
    			if(repeat){
    				currentLocation = simulatedRoute.waypoints.get(0);
    				waypointCounter = 1;
    			}
    			else {
    				System.out.println(route);
    				finished = true;
    				
    			}
    		}
//    		nextWaypoint = simulatedRoute.waypoints.get(waypointCounter);
    	}
    	JSONObject coordinates = new JSONObject();
    	//System.out.println("Moving to " + nextWaypoint.name + ". Distance = " + GeoHelper.calcGeoDistanceInKm(currentLocation, nextWaypoint) * 1000 + "m");
    	double angle = GeoHelper.calcAngleBetweenGeoLocationsInRadians(currentLocation, nextWaypoint);
    	double newLat = currentLocation.latitude + Math.sin(angle) * SIMULATOR_MOVEMENT_SPEED;
    	double newLon = currentLocation.longitude + Math.cos(angle) * SIMULATOR_MOVEMENT_SPEED;
    	currentLocation = new Location("currentLocation", newLat, newLon);  
    	
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	
    	coordinates.put("service","palermo" );
    	coordinates.put("servicepath", "/autobus_palermo");
    	coordinates.put("resource_id", "109");
    	coordinates.put("muuid", muuid);
    	coordinates.put("timeslot", timestamp);
    	coordinates.put("lat", currentLocation.latitude);
    	coordinates.put("lon", currentLocation.longitude);
    	route.put(coordinates);
    	
    	
    	String postUrl = "http://tesi-edge-01.northeurope.cloudapp.azure.com:8082/topics/jsontest";
    	String coord = coordinates.toString();
    	String example =  "{\"records\": [{\"value\": \""+new String(Base64.getEncoder().encode(coord.getBytes()))+"\"}]}";
    	Client client = Client.create();
    	WebResource webResource = client.resource(postUrl);
    	ClientResponse response = webResource.type("application/json").post(ClientResponse.class,example);
    	 if(response.getStatus()!=200){
             throw new RuntimeException("HTTP Error: "+ response.getStatus());
         }
    	
    	
    	  	    
	}
}