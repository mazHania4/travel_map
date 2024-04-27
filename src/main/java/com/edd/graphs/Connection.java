package com.edd.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Builder @Setter @ToString
public class Connection {
    private boolean hasWay;
    private Node to;
    private int timeByCar, timeByFoot, gas, fatigue, distance, traffic, start_traffic, end_traffic;
}
