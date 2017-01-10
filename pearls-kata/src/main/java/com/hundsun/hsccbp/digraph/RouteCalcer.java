package com.hundsun.hsccbp.digraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * 路径计算器类
 * 
 * @author pengqb
 *
 */
public class RouteCalcer {
	AdjListStorageEngine store;
	Set<Route> tmpRoutes = new HashSet<>();
	Set<Route> routes = new HashSet<>();
	Route shortestRoute = new Route("", Integer.MAX_VALUE, Integer.MAX_VALUE);

	public Route getShortestRoute() {
		return shortestRoute;
	}

	public void setShortestRoute(Route shortestRoute) {
		this.shortestRoute = shortestRoute;
	}

	public Set<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(Set<Route> routes) {
		this.routes = routes;
	}

	public RouteCalcer(AdjListStorageEngine store) {
		this.store = store;
	}

	/**
	 * 打印指定路径距离
	 * 
	 * @param routeStr
	 *            指定路径
	 */
	public void printDistance(String routeStr) {
		try {
			System.out.println(getDistance(routeStr));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 返回指定路径的距离
	 * 
	 * @param routeStr
	 *            指定路径
	 * @return
	 * @throws Exception
	 *             当指定路径不存在时报异常
	 */
	public Integer getDistance(String routeStr) throws Exception {
		if (!StringUtils.routeStrMatchs(routeStr)) {
			throw new Exception(StringUtils.routeStrError);
		}
		String[] strs = routeStr.split("-");
		Map<String, Map<String, Integer>> graphStore = store.getGraphStore();
		int distance = 0;
		for (int i = 0; i < strs.length - 1; i++) {
			Integer edgeLenth = graphStore.getOrDefault(strs[i],
					new HashMap<String, Integer>()).get(strs[i + 1]);
			if (edgeLenth == null) {
				throw new Exception("NO SUCH ROUTE");
			} else {
				distance += edgeLenth;
			}
		}
		return distance;
	}

	/**
	 * 计算满足一定条件(function)下,起点城镇和终端城镇之间的路径
	 * 
	 * @param start
	 *            起点城镇
	 * @param end
	 *            终端城镇
	 * @param function
	 *            满足一定条件
	 */
	public void calRoutes(String start, String end,
			Function<Route, Boolean> function) {
		tmpRoutes.clear();
		routes.clear();
		calRoutes(start, end, null, function);
	}

	/**
	 * 计算基于已有路径route,满足一定条件(function)下,起点城镇和终端城镇之间的路径
	 * 
	 * @param start
	 *            起点城镇
	 * @param end
	 *            终端城镇
	 * @param route
	 *            已有路径,当已有路径 = null时,表示计算所有的路径;已有路径 !=
	 *            null,例如基于路径AB计算A到C的路径,则表示路径必须先经过A和B。
	 * @param function
	 *            满足一定条件
	 */
	private void calRoutes(String start, String end, Route route,
			Function<Route, Boolean> function) {
		Optional<Route> oRoute = Optional.ofNullable(route);
		for (Entry<String, Integer> entry : store.getGraphStore().get(start)
				.entrySet()) {
			Route curRoute = oRoute.orElse(new Route(start, 0, 0)).add(
					entry.getKey(), entry.getValue());
			Boolean recursiveCall = function.apply(curRoute);
			if (recursiveCall) {
				tmpRoutes.add(curRoute);
				if (end.equals(entry.getKey())) {
					routes.add(curRoute);
					if (curRoute.getDistance() < shortestRoute.getDistance()) {
						shortestRoute = curRoute;
					}
				}
				this.calRoutes(entry.getKey(), end, curRoute, function);
			} else {
				return;
			}
		}
	}

	class Route {
		String path;
		Integer stops;
		Integer distance;

		public Route(String path, Integer stops, Integer distance) {
			this.path = path;
			this.stops = stops;
			this.distance = distance;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public Integer getStops() {
			return stops;
		}

		public void setStops(Integer stops) {
			this.stops = stops;
		}

		public Integer getDistance() {
			return distance;
		}

		public void setDistance(Integer distance) {
			this.distance = distance;
		}

		/**
		 * 在当前路径的基础上继续往前走一个节点，返回走完之后新的路径。
		 * @param name 
		 * @param otherDistance
		 * @return
		 */
		public Route add(String name, Integer edgeWeight) {
			Route d = new Route(path + name, stops + 1, distance
					+ edgeWeight);
			return d;
		}

		@Override
		public String toString() {
			return "path:" + path + ",stops:" + stops + ",distance:" + distance;
		}
	}
}
