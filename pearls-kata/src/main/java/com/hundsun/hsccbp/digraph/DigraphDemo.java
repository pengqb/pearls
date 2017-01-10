package com.hundsun.hsccbp.digraph;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DigraphDemo {

	public static void main(String[] args) {
		DigraphDemo dd = new DigraphDemo();
		String initGraphStr = dd.readFileByChars();
		AdjListStorageEngine adjListStore = new AdjListStorageEngine();
		try {
			adjListStore.initStore(initGraphStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RouteCalcer routeCalcer = new RouteCalcer(adjListStore);

		// The distance of the route A-B-C.
		routeCalcer.printDistance("A-B-C");

		// The distance of the route A-D.
		routeCalcer.printDistance("A-D");

		// The distance of the route A-D-C.
		routeCalcer.printDistance("A-D-C");

		// The distance of the route A-E-B-C-D.
		routeCalcer.printDistance("A-E-B-C-D");

		// The distance of the route A-E-D.
		routeCalcer.printDistance("A-E-D");

		// The number of trips starting at C and ending at C with a maximum of 3
		// stops. In the sample data below, there are two such trips: C-D-C (2
		// stops). and C-E-B-C (3 stops).
		routeCalcer.calRoutes("C", "C", val -> val.getStops() <= 3);
		System.out.println(routeCalcer.getRoutes().size());

		// The number of trips starting at A and ending at C with exactly 4
		// stops. In the sample data below, there are three such trips: A to C
		// (via B,C,D); A to C (via D,C,D); and A to C (via D,E,B).
		routeCalcer.calRoutes("A", "C", val -> val.getStops() <= 4);
		System.out.println(routeCalcer.getRoutes().stream()
				.filter(route -> route.getStops() == 4).count());

		// The length of the shortest route (in terms of distance to travel)
		// from A to C.
		routeCalcer.calRoutes("A", "C",
				val -> !StringUtils.circleCheck(val.getPath()));
		System.out.println(routeCalcer.getShortestRoute().getDistance());

		// The length of the shortest route (in terms of distance to travel)
		// from B to B.
		routeCalcer.calRoutes("B", "B",
				val -> !StringUtils.circleCheck(val.getPath()));
		System.out.println(routeCalcer.getShortestRoute().getDistance());

		// The number of different routes from C to C with a distance of less
		// than 30. In the sample data, the trips are: CDC, CEBC, CEBCDC,
		// CDCEBC, CDEBC, CEBCEBC, CEBCEBCEBC.
		routeCalcer.calRoutes("C", "C", val -> val.getDistance() < 30);
		System.out.println(routeCalcer.getRoutes().size());
	}

	public String readFileByChars() {
		InputStream is =  this.getClass().getClassLoader().getResourceAsStream("initGraph.txt");	
		Reader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new InputStreamReader(is);
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r') {
					//System.out.print((char) tempchar);
					sb.append((char) tempchar);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
