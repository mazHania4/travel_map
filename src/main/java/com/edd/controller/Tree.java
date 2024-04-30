package com.edd.controller;

import com.edd.graphs.Node;
import com.edd.graphs.Route;

import java.util.ArrayList;
import java.util.List;

class Pag {
    ArrayList<Route> values;
    ArrayList<Pag> children;
    boolean isLeaf;

    public Pag(boolean isLeaf) {
        this.values = new ArrayList<>();
        this.children = new ArrayList<>();
        this.isLeaf = isLeaf;
    }
}

public class Tree {


    private Pag root;
    private int comCase;
    private final int m;

    public Tree(int comCase) {
        this.root = null;
        this.m = 5;
        this.comCase = comCase;
    }

    public void insert(Route clave) {
        if (root == null) {
            root = new Pag(true);
            root.values.add(clave);
        } else {
            if (root.values.size() == m - 1) {
                Pag newRoot = new Pag(false);
                newRoot.children.add(root);
                divide(newRoot, 0, root);
                root = newRoot;
            }
            insertNotFull(root, clave);
        }
    }

    private int getValue(Route r){
        return switch (comCase){
            case 2 -> r.getFatigue();
            case 3 -> r.getGas();
            case 4 -> r.getDistance() / r.getFatigue();
            case 5 -> r.getDistance() / r.getGas();
            case 6 -> r.getSpeed();
            default -> r.getDistance();
        };
    }

    private void insertNotFull(Pag pag, Route clave) {
        int i = pag.values.size() - 1;
        if (pag.isLeaf) {
            pag.values.add(null);
            while (i >= 0 && getValue(pag.values.get(i)) > getValue(clave)) {
                pag.values.set(i + 1, pag.values.get(i));
                i--;
            }
            pag.values.set(i + 1, clave);
        } else {
            while (i >= 0 && getValue(pag.values.get(i)) > getValue(clave)) {
                i--;
            }
            i++;
            if (pag.children.get(i).values.size() == m - 1) {
                divide(pag, i, pag.children.get(i));
                if (getValue(pag.values.get(i)) < getValue(clave)) {
                    i++;
                }
            }
            insertNotFull(pag.children.get(i), clave);
        }
    }

    private void divide(Pag parent, int index, Pag full) {
        Pag newPag = new Pag(full.isLeaf);
        for (int i = 0; i < m - 1; i++) {
            if ((m/2) < full.values.size()) {
                newPag.values.add(full.values.remove(m / 2));
            }
        }
        if (!full.isLeaf) {
            for (int i = 0; i < m / 2; i++) {
                if ((m/2) < full.values.size()) {
                    newPag.children.add(full.children.remove(m / 2));
                }
            }
        }
        parent.children.add(index + 1, newPag);
        parent.values.add(index, full.values.get(m / 2 - 1));
        if (((m/2) - 1) < full.values.size()) {
            full.values.remove(m / 2 - 1);
        }
    }

    public List<Route> inOrder() {
        List<Route> list = new ArrayList<>();
        traverseInOrder(root, list);
        return list;
    }

    private void traverseInOrder(Pag pag, List<Route> list) {
        if (pag != null) {
            int i;
            for (i = 0; i < pag.values.size(); i++) {
                if (!pag.isLeaf) {
                    traverseInOrder(pag.children.get(i), list);
                }
                list.add(pag.values.get(i));
            }
            if (!pag.isLeaf) {
                traverseInOrder(pag.children.get(i), list);
            }
        }
    }


}
