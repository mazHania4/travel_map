package com.edd.graphs;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Route {
    private final List<Node> steps;
    private final boolean isByCar;
    private int timeByCar, timeByFoot, gas, fatigue, distance;
    private final double speed;

    public Route(List<Connection> connections, boolean isByCar, int actualTime) {
        this.steps = new ArrayList<>();
        this.isByCar = isByCar;
        for (Connection c: connections) {
            steps.add(c.getTo());
            distance += c.getDistance();
            gas += c.getGas();
            timeByCar += c.getTimeByCar(actualTime);
            fatigue += c.getFatigue();
            timeByFoot += c.getTimeByFoot();
        }
        if (timeByFoot == 0) timeByFoot = 1;
        if (timeByCar == 0) timeByCar = 1;
        speed = isByCar ? (double) distance /timeByCar : (double) distance /timeByFoot;
    }

    public Route() {
        steps = new ArrayList<>();
        isByCar = false;
        speed = 0;
    }

    public double getValue(int i){
        return switch (i){
            case 2 -> fatigue;
            case 3 -> gas;
            case 4 -> (double) distance / fatigue;
            case 5 -> (double) distance / gas;
            case 6 -> timeByCar;
            default -> distance;
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        steps.forEach(s-> builder.append(s.getId()).append("->"));
        return builder.substring(0, builder.length()-2);
    }
}
