package com.edd.controller;

import com.edd.graphs.Connection;
import com.edd.graphs.Route;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.model.MutableGraph;
import static guru.nidi.graphviz.model.Factory.mutGraph;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


public class GraphvizCtr {

    private int pagCount;

    public void treeGraph(int graphId, Tree tree) throws IOException {
        MutableGraph g = guru.nidi.graphviz.model.Factory.mutGraph("b_tree").setDirected(true);
        if (tree.getRoot() != null) {
            Queue<Pag> cola = new ArrayDeque<>();
            cola.add(tree.getRoot());
            while (!cola.isEmpty()) {
                Pag pag = cola.poll();
                MutableNode n = guru.nidi.graphviz.model.Factory.mutNode(String.valueOf(pagCount)).add(Label.of(pag.values.toString()));
                n.add(Shape.RECTANGLE);
                g.add(n);
                for (int i = 0; i < pag.children.size(); i++) {
                    pagCount++;
                    MutableNode child = guru.nidi.graphviz.model.Factory.mutNode(String.valueOf(pagCount)).add(Label.of(pag.children.get(i).values.toString()));
                    child.add(Shape.RECTANGLE);
                    g.add(child);
                    cola.add(pag.children.get(i));
                    g.add(guru.nidi.graphviz.model.Factory.graph().link(n.addLink(child)));
                }
            }
        }
        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("src/main/resources/generated/map_"+graphId+".png"));
    }

    public void mapGraph(List<com.edd.graphs.Node> nodes, Route route, String actualNodeId, int graphId, int lblCase, int actualTime) throws IOException{
        MutableGraph g = mutGraph("map_"+graphId).setDirected(true);
        for (com.edd.graphs.Node n : nodes) {
            MutableNode node = Factory.mutNode(n.getId());
            if (route.getSteps().contains(n)) {
                node.add(Color.DARKGOLDENROD1);
                if (n.getId().equals(actualNodeId)) node.add(Shape.M_CIRCLE);
                if (route.getSteps().indexOf(n)==0) node.add(Shape.HOUSE);
                if (route.getSteps().indexOf(n)==route.getSteps().size()-1) node.add(Shape.DOUBLE_CIRCLE);
            } else node.add(Shape.CIRCLE);
            g.add(node);
            for (com.edd.graphs.Connection c : n.getConnections()) {
                if (c.isHasWay()) {
                    node.addLink(Factory.mutNode(c.getTo().getId()));
                    String lbl = getLbl(lblCase, c, route.isByCar(), actualTime);
                    node.links().getLast().add(Label.of(lbl));
                    int i = route.getSteps().indexOf(n)+1 < route.getSteps().size() ? route.getSteps().indexOf(n) + 1 : route.getSteps().indexOf(n);
                    if (route.getSteps().contains(n) && route.getSteps().get(i).equals(c.getTo()))
                        node.links().getLast().add(Color.DARKGOLDENROD1);
                    if (!route.isByCar() && route.getSteps().contains(n) && route.getSteps().contains(c.getTo()))
                        node.links().getLast().add(Color.DARKGOLDENROD1);
                }
            }
        }
        Graphviz.fromGraph(g).height(650).render(Format.PNG).toFile(new File("src/main/resources/generated/map_"+graphId+".png"));
    }

    private static String getLbl(int lblCase, Connection c, boolean isByCar, int actualTIme) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String lbl = "-";
        switch (lblCase){
            case 1 -> lbl = String.valueOf(c.getDistance());
            case 2 -> lbl = String.valueOf(c.getFatigue());
            case 3 -> lbl = String.valueOf(c.getGas());
            case 4 -> {
                double num = (double) c.getDistance() / c.getFatigue();
                lbl = String.valueOf(df.format(num));
            }
            case 5 -> {
                double num = (double) c.getDistance() / c.getGas();
                lbl = String.valueOf(df.format(num));
            }
            case 6 -> {
                double num = c.getSpeed(isByCar);
                int time = isByCar ? c.getTimeByCar(actualTIme) : c.getTimeByFoot();
                lbl = time + "|" + df.format(num);
            }
        }
        return lbl;
    }

}
