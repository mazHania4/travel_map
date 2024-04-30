package com.edd.graphs;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @ToString
public class Route {
    private final List<Node> steps;
    private final boolean isByCar;
    private int timeByCar, timeByFoot, gas, fatigue, distance;
    private final int speed;

    public Route(List<Connection> connections, boolean isByCar, int actualTime) {
        this.steps = new ArrayList<>();
        this.isByCar = isByCar;
        for (Connection c: connections) {
            steps.add(c.getTo());
            distance += c.getDistance();
            gas += c.getGas();
            if( (actualTime >= c.getStart_traffic()) && (actualTime <= c.getEnd_traffic()) ) {
                timeByCar = (timeByCar + c.getTimeByCar()) * (c.getTraffic() / 100);
            } else { timeByCar += c.getTimeByCar(); System.out.print("++"); }
            fatigue += c.getFatigue();
            timeByFoot += c.getTimeByFoot();
        }
        if (timeByFoot == 0) timeByFoot = 1;
        if (timeByCar == 0) timeByCar = 1;
        speed = isByCar ? distance/timeByCar : distance/timeByFoot;
    }
}
