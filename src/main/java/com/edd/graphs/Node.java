package com.edd.graphs;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class Node {
    private String id;
    private List<Connection> connections;

    @Override
    public String toString() {
        StringBuilder con = new StringBuilder();
        return "{" +
                "id='" + id + "'}";
    }
}
