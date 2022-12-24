import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.*;

public class RouteCalculatorTest extends TestCase {

    RouteCalculator routeCalculator;
    StationIndex stationIndex;
    ArrayList<Station> route = new ArrayList<>();

    @Override
    protected void setUp() throws Exception {
        Line line1 = new Line(1, "Первый");
        Line line2 = new Line(2, "Второй");
        Line line3 = new Line(3, "Третий");

        Station station1 = new Station("Фрунзенская", line1);
        Station station2 = new Station("Технологический институт 2", line1);
        Station station3 = new Station("Технологический институт 1", line2);
        Station station4 = new Station("Пушкинская", line2);
        Station station5 = new Station("Звенигородская", line3);
        Station station6 = new Station("Обводный канал", line3);

        line1.addStation(station1);
        line1.addStation(station2);
        line2.addStation(station3);
        line2.addStation(station4);
        line3.addStation(station5);
        line3.addStation(station6);

        route.add(station1);
        route.add(station2);
        route.add(station3);
        route.add(station4);
        route.add(station5);
        route.add(station6);

        stationIndex = new StationIndex();
        stationIndex.addStation(station1);
        stationIndex.addStation(station2);
        stationIndex.addStation(station3);
        stationIndex.addStation(station4);
        stationIndex.addStation(station5);
        stationIndex.addStation(station6);

        List<Station> stations = List.of(station2, station3);
        stationIndex.addConnection(stations);
        List<Station> stations1 = List.of(station3, station2);
        stationIndex.addConnection(stations1);
        List<Station> stations2 = List.of(station4, station5);
        stationIndex.addConnection(stations2);
        List<Station> stations3 = List.of(station5, station4);
        stationIndex.addConnection(stations3);

        routeCalculator = new RouteCalculator(stationIndex);
    }

    public void testGetShortestRoute() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Фрунзенская"),
                stationIndex.getStation("Звенигородская"));
        List<Station> expected = List.of(route.get(0), route.get(1), route.get(2), route.get(3), route.get(4));
        assertEquals(expected, actual);
    }

    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 14.5;
        assertEquals(expected, actual);
    }

    public void testGetRouteOnTheLine() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Технологический институт 1"),
                stationIndex.getStation("Пушкинская"));
        List<Station> expected = List.of(route.get(2), route.get(3));
        assertEquals(expected, actual);
    }

    //
    public void testGetRouteWithOneConnection() {
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Технологический институт 1"),
                stationIndex.getStation("Фрунзенская"));
        List<Station> expected = List.of(route.get(2), route.get(1), route.get(0));
        assertEquals(expected, actual);
    }

    //
    public void testGetRouteWithTwoConnections() {

        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Обводный канал"),
                stationIndex.getStation("Фрунзенская"));
        List<Station> expected = List.of(route.get(5), route.get(4), route.get(3), route.get(2), route.get(1),
                route.get(0));
        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() throws Exception {

    }
}
