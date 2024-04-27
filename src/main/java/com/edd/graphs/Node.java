package com.edd.graphs;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @Builder
public class Node {
    private String id;
    private List<Connection> connections;

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                '}';
    }
}
