package com.edd.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder @ToString
public class Connection {
    @Getter @Setter private boolean hasWay;
    @Getter @Setter private Node to;
    @Setter private int timeByCar;
    @Getter @Setter private int timeByFoot, gas, fatigue, distance, traffic, start_traffic, end_traffic;

    public double getSpeed(boolean isByCar){
        if (isByCar) return (double) distance /timeByCar;
        else return (double) distance /timeByFoot;
    }

    public int getTimeByCar(int actualTime) {
        if( (actualTime >= start_traffic) && (actualTime <= end_traffic) ) {
            return timeByCar * getTraffic() / 100;
        } else return timeByCar;
    }
}
