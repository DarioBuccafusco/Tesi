package it.asirchia.utils.gps.simulator.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
	
	public List<Location> waypoints;
	
	public Route() {
		waypoints = new ArrayList<Location>();
	}
	
	public Route(List<Location> waypoints) {
		this.waypoints = waypoints;
	}
	
	public void addWayPoint(Location l){
		this.waypoints.add(l);
	}
}
