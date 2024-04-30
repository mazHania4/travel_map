package com.edd.controller;

import com.edd.graphs.Connection;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import guru.nidi.graphviz.model.MutableGraph;
import static guru.nidi.graphviz.model.Factory.mutGraph;

import java.io.*;
import java.util.List;


public class GraphvizCtr {
    public void mapGraph(List<com.edd.graphs.Node> nodes, List<com.edd.graphs.Node> route, String actualNodeId, int graphId, int lblCase) throws IOException{
        MutableGraph g = mutGraph("map_"+graphId).setDirected(true);
        for (com.edd.graphs.Node n : nodes) {
            MutableNode node = Factory.mutNode(n.getId());
            if (route.contains(n)) {
                node.add(Color.DARKGOLDENROD1);
                if (n.getId().equals(actualNodeId)) node.add(Shape.M_CIRCLE);
                if (route.indexOf(n)==0) node.add(Shape.HOUSE);
                if (route.indexOf(n)==route.size()-1) node.add(Shape.DOUBLE_CIRCLE);
            } else node.add(Shape.CIRCLE);
            g.add(node);
            for (com.edd.graphs.Connection c : n.getConnections()) {
                if (c.isHasWay()) {
                    node.addLink(Factory.mutNode(c.getTo().getId()));
                    String lbl = getLbl(lblCase, c);
                    node.links().getLast().add(Label.of(lbl));
                    int i = route.indexOf(n)+1 < route.size() ? route.indexOf(n) + 1 : route.indexOf(n);
                    if (route.contains(n) && route.get(i).equals(c.getTo()))
                        node.links().getLast().add(Color.DARKGOLDENROD1);
                }
            }
        }
        Graphviz.fromGraph(g).height(650).render(Format.PNG).toFile(new File("src/main/resources/generated/map_"+graphId+".png"));
    }

    private static String getLbl(int lblCase, Connection c) {
        String lbl = "-";
        switch (lblCase){
            case 1 -> lbl = String.valueOf(c.getDistance());
            case 2 -> lbl = String.valueOf(c.getFatigue());
            case 3 -> lbl = String.valueOf(c.getGas());
            case 4 -> lbl = c.getFatigue() + "|" + c.getDistance();
            case 5 -> lbl = c.getGas() + "|" + c.getDistance();
            case 6 -> lbl = c.getSpeed(true) + "|"+c.getSpeed(false);
        }
        return lbl;
    }

    /*
     grafoGrapher.graph(FilesUtil.RESOURCES_PATH, "globalGraph" + globalGraphNum, grafo);
filesUtil.deleteFile(FilesUtil.RESOURCES_PATH + "globalGraph" + globalGraphNum + ".
 initGraphImage(label);

private void initGraphImage(JLabel label) {
        label.setIcon(null);
        label.revalidate();
        label.repaint();
        String path = FilesUtil.RESOURCES_PATH + "globalGraph" + globalGraphNum + ".png";
        ImageIcon imageIcon = new ImageIcon(path);
        label.setIcon(imageIcon);
        label.revalidate();
        label.repaint();
        filesUtil.deleteFile(FilesUtil.RESOURCES_PATH + "globalGraph" + (globalGraphNum-1) + ".dot");
        globalGraphNum++;
    }

/*el truco esta en agregarle un numero al nombre del archivo xd */


}
