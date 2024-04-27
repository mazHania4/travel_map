package com.edd;

import com.edd.controller.GraphsCtr;
import com.edd.view.WindowPnl;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        JFrame window = new JFrame();
        window.setBounds(300,300,1000,800);
        window.setResizable(false);
        window.setTitle("WEB XML");
        window.add(new WindowPnl());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        String routesData = """
                Nodo1|Nodo2|5|8|20|3|12
                Nodo2|Nodo1|17|6|9|14|23
                Nodo2|Nodo3|11|4|18|7|22
                Nodo4|Nodo3|13|19|15|1|10
                Nodo4|Nodo6|16|2|24|21|25
                Nodo6|Nodo4|26|28|29|27|30
                Nodo5|Nodo6|35|32|34|31|33
                Nodo7|Nodo6|36|37|38|40|39
                Nodo1|Nodo7|43|41|49|44|47
                Nodo7|Nodo1|50|45|48|42|46
                Nodo3|Nodo7|51|59|52|54|53
                Nodo3|Nodo5|60|55|58|57|56
                Nodo5|Nodo3|63|61|65|62|6
                Nodo4|Nodo5|68|67|66|70|69
                Nodo5|Nodo4|72|71|73|74|75
                Nodo7|Nodo5|76|79|78|80|77
                """;
        String trafficData = "Nodo2|Nodo1|17|19|15";
        GraphsCtr graphsCtr = new GraphsCtr(routesData, trafficData);
        var list = graphsCtr.findAllRoutes("Nodo1", "Nodo7", true);
        System.out.println(list);
        System.out.println("Bye!");
    }
}