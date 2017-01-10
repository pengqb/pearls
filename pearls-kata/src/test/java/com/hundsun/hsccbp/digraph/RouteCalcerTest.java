package com.hundsun.hsccbp.digraph;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RouteCalcerTest {
	private RouteCalcer routeCalcer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String initGraphStr = "AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7";

		AdjListStorageEngine adjListStore = new AdjListStorageEngine();
		try {
			adjListStore.initStore(initGraphStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		routeCalcer = new RouteCalcer(adjListStore);
	}

	@Test
	public void testGetDistance() {
		Integer distance = 0;
		try {
			distance = routeCalcer.getDistance("A-B-C");
			assertEquals(Integer.valueOf(9), distance);
			distance = routeCalcer.getDistance("A-D");
			assertEquals(Integer.valueOf(5), distance);
			distance = routeCalcer.getDistance("A-D-C");
			assertEquals(Integer.valueOf(13), distance);
			distance = routeCalcer.getDistance("A-E-B-C-D");
			assertEquals(Integer.valueOf(22), distance);
			distance = routeCalcer.getDistance("A-E-D");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCalRoutes() {
		routeCalcer.calRoutes("C", "C", val -> val.getStops() <= 3);
		assertEquals(Integer.valueOf(2),
				Integer.valueOf(routeCalcer.getRoutes().size()));
		routeCalcer.getRoutes().forEach(entry -> System.out.println(entry));

		routeCalcer.calRoutes("A", "C", val -> val.getStops() <= 4);
		routeCalcer.getRoutes().stream().filter(route -> route.getStops() == 4)
				.forEach(route -> System.out.println(route));
		assertEquals(
				Long.valueOf(3),
				Long.valueOf(routeCalcer.getRoutes().stream()
						.filter(route -> route.getStops() == 4).count()));

		routeCalcer.calRoutes("A", "C",
				val -> !StringUtils.circleCheck(val.getPath()));
		assertEquals(Integer.valueOf(9),
				Integer.valueOf(routeCalcer.getShortestRoute().getDistance()));
		System.out.println(routeCalcer.getShortestRoute());

		routeCalcer.calRoutes("B", "B",
				val -> !StringUtils.circleCheck(val.getPath()));
		assertEquals(Integer.valueOf(9),
				Integer.valueOf(routeCalcer.getShortestRoute().getDistance()));
		System.out.println(routeCalcer.getShortestRoute());

		routeCalcer.calRoutes("C", "C", val -> val.getDistance() < 30);
		assertEquals(Integer.valueOf(7),
				Integer.valueOf(routeCalcer.getRoutes().size()));
		routeCalcer.getRoutes().forEach(entry -> System.out.println(entry));
	}

}
