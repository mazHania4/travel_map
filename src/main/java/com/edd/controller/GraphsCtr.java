package com.edd.controller;

import com.edd.graphs.*;

import java.util.*;

public class GraphsCtr {
    private final HashMap<String, Node> nodes;

    public List<Route> findAllRoutes(String originId, String destinationId, boolean isByCar) {
        List<Route> allRoutes = new ArrayList<>();
        List<Connection> currConnections = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Connection start = Connection.builder().hasWay(true).to(nodes.get(originId))
                .timeByCar(0).timeByFoot(0).gas(0).fatigue(0).distance(0).traffic(0).start_traffic(-1).end_traffic(-1).build();
        dfs(start, destinationId, currConnections, allRoutes, visited, isByCar);
        return allRoutes;
    }

    private void dfs(Connection connection, String destinationId, List<Connection> currConnections, List<Route> allRoutes, Set<String> visited, boolean isByCar) {
        Node node = connection.getTo();
        visited.add(node.getId());
        currConnections.add(connection);
        if (node.getId().equals(destinationId)) {
            allRoutes.add( new Route(new ArrayList<>(currConnections), isByCar, getCurrTime()) );
        } else {
            for (Connection c : node.getConnections()) {
                Node adjacent = c.getTo();
                if (isByCar) {
                    if (!visited.contains(adjacent.getId()) && c.isHasWay())
                        dfs(c, destinationId, currConnections, allRoutes, visited, true);
                } else {
                    if (!visited.contains(adjacent.getId()))
                        dfs(c, destinationId, currConnections, allRoutes, visited, false);
                }
            }
        }
        currConnections.remove(currConnections.size()-1);
        visited.remove(node.getId());
    }

    private int getCurrTime() {
        return 2;
    }


    public GraphsCtr(String routesData, String trafficData) {
        nodes = new HashMap<>();
        String[] routes = routesData.split("\n");
        String[] traffic = trafficData.split("\n");
        for (String r : routes) {
            String[] d = r.split("\\|");
            if (!nodes.containsKey(d[0])){
                Node node = Node.builder().id(d[0]).connections(new ArrayList<>()).build();
                nodes.put(d[0], node);
            }
            if (!nodes.containsKey(d[1])){
                Node node = Node.builder().id(d[1]).connections(new ArrayList<>()).build();
                nodes.put(d[1], node);
            }
            Node from = nodes.get(d[0]);
            Node to = nodes.get(d[1]);
            boolean hasConnection = false;
            for (Connection c: from.getConnections()) {
                if (c.getTo().getId().equals(to.getId())) {
                    hasConnection = true;
                    c.setTimeByCar(Integer.parseInt(d[2]));
                    c.setGas(Integer.parseInt(d[4]));
                    c.setHasWay(true);
                    break;}
            }
            if (!hasConnection){
                from.getConnections().add(
                        Connection.builder()
                            .hasWay(true)
                            .to(to)
                            .timeByCar(Integer.parseInt(d[2]))
                            .timeByFoot(Integer.parseInt(d[3]))
                            .gas(Integer.parseInt(d[4]))
                            .fatigue(Integer.parseInt(d[5]))
                            .distance(Integer.parseInt(d[6]))
                        .build()
                );
                to.getConnections().add(
                        Connection.builder()
                                .hasWay(false)
                                .to(from)
                                .timeByCar(Integer.parseInt(d[2]))
                                .timeByFoot(Integer.parseInt(d[3]))
                                .gas(Integer.parseInt(d[4]))
                                .fatigue(Integer.parseInt(d[5]))
                                .distance(Integer.parseInt(d[6]))
                                .build()
                );
            }
        }
        for (String t : traffic) {
            String[] d = t.split("\\|");
            Node from = nodes.get(d[0]);
            Node to = nodes.get(d[1]);
            for (Connection c: from.getConnections()) {
                if (c.getTo().getId().equals(to.getId())) {
                    c.setStart_traffic(Integer.parseInt(d[2]));
                    c.setEnd_traffic(Integer.parseInt(d[3]));
                    c.setTraffic(Integer.parseInt(d[4]));
                    break;
                }
            }
        }
    }
}
