package com.edd.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WindowPnl extends JPanel {
    private static final Color buttonColor = new Color(68, 94, 74);
    private static final Color bgColor = new Color(54, 71, 62);
    private static final Color tfsColor = new Color(76, 89, 79);
    private static final Color menuColor = new Color(83, 95, 117);
    private static final Color mainTextColor = new Color(227, 227, 197);
    private static final Color textColor = new Color(191, 191, 184);
    private static final Color highlightTextColor = new Color(217, 183, 13);
    private static final Color bordersColor = new Color(242, 215, 80);
    private static final Color highlightTextColor2 = new Color(27, 41, 39);
    private static final Font font = new Font(Font.MONOSPACED, Font.ITALIC, 12);

    private JTextField originTF, destTF, typeTF;
    private JComboBox<String> nextNodeCB;
    private JTable worstTable;


    public WindowPnl(){
        setLayout(new BorderLayout());
        setBackground(bgColor);

        //----------- Menu ------------------------------------
        JMenuBar menu = new JMenuBar();
        menu.setBackground(menuColor);
        JMenuItem file = new JMenuItem("Cargar archivo");
        file.setForeground(Color.WHITE);
        file.setBackground(menuColor);
        file.addActionListener(e -> {
            openFile();
        });
        menu.add(file);
        add(menu, BorderLayout.NORTH);

        JPanel sideBar = new JPanel();
        sideBar.setOpaque(false);
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.add(tripDataPnl()); //----------- Current Trip Data ------------------
        sideBar.add(Utils.separatorH(bordersColor));
        sideBar.add(forwardPnl()); //----------- Move to another node ------------------
        sideBar.add(Utils.separatorH(bordersColor));
        sideBar.add(filterPnl()); //----------- Options to filter the routes ------------------
        sideBar.add(Utils.separatorH(bordersColor));
        sideBar.add(worstRoutesPnl()); //----------- Worst Routes by filter ------------------

        add(sideBar, BorderLayout.WEST);

    }

    private void openFile(){

    }

    private void filter(int option) { //
        System.out.println(option);
    }

    private void moveForward() { //to the next node

    }

    private JPanel tripDataPnl(){
        JPanel pnl = new JPanel();
        pnl.setOpaque(false);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15,15,15,15));
        JLabel title = new JLabel("Viaje actual:");
        title.setForeground(mainTextColor);
        pnl.add(title);
        pnl.add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel tripData = new JPanel(new SpringLayout());
        tripData.setOpaque(false);
        JLabel originLbl = new JLabel("Origen: ", JLabel.TRAILING);
        originLbl.setForeground(textColor);
        tripData.add(originLbl);
        originTF = new JTextField();
        originTF.setBackground(tfsColor);
        originTF.setEditable(false);
        originTF.setForeground(highlightTextColor);
        originTF.setBorder(new EmptyBorder(3,4,3,4));
        originTF.setText("hey!");
        tripData.add(originTF);
        JLabel destinationLbl = new JLabel("Destino: ", JLabel.TRAILING);
        destinationLbl.setForeground(textColor);
        tripData.add(destinationLbl);
        destTF = new JTextField();
        destTF.setBackground(tfsColor);
        destTF.setEditable(false);
        destTF.setBorder(new EmptyBorder(3,4,3,4));
        destTF.setForeground(highlightTextColor);
        tripData.add(destTF);
        JLabel typeLbl = new JLabel("Tipo: ", JLabel.TRAILING);
        typeLbl.setForeground(textColor);
        tripData.add(typeLbl);
        typeTF = new JTextField();
        typeTF.setEditable(false);
        typeTF.setBackground(tfsColor);
        typeTF.setBorder(new EmptyBorder(3,4,3,4));
        typeTF.setForeground(highlightTextColor);
        tripData.add(typeTF);
        Utils.makeCompactGrid(tripData, 3, 2, 5, 5, 5, 10);
        pnl.add(tripData);
        return pnl;
    }

    private JPanel forwardPnl(){
        JPanel pnl = new JPanel();
        pnl.setOpaque(false);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15,15,15,15));
        JLabel title = new JLabel("Avanzar al siguiente nodo:");
        title.setForeground(mainTextColor);
        JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnl2.setOpaque(false);
        pnl2.add(title);
        pnl.add(pnl2);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));
        nextNodeCB = new JComboBox<>();
        nextNodeCB.addItem("----");
        nextNodeCB.setEnabled(false);
        pnl.add(nextNodeCB);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));
        JButton forwardBtn = new JButton("Moverse");
        forwardBtn.setForeground(mainTextColor);
        forwardBtn.setBackground(buttonColor);
        forwardBtn.addActionListener(e ->  moveForward());
        JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl3.setOpaque(false);
        pnl3.add(forwardBtn);
        pnl.add(pnl3);
        return pnl;
    }

    private JPanel filterPnl(){
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnl.setBorder(new EmptyBorder(15,15,15,15));
        pnl.setOpaque(false);
        JPanel pnl2 = new JPanel();
        pnl2.setLayout(new BoxLayout(pnl2, BoxLayout.Y_AXIS));
        pnl2.setOpaque(false);
        JLabel title = new JLabel("Ordenar rutas por:");
        title.setForeground(mainTextColor);
        pnl2.add(title);
        pnl2.add(Box.createRigidArea(new Dimension(0, 10)));
        JRadioButton gasRBtn = new JRadioButton("Gasolina");
        gasRBtn.addActionListener(e -> filter(1));
        gasRBtn.setOpaque(false);
        gasRBtn.setForeground(textColor);
        JRadioButton fatigueRBtn = new JRadioButton("Desgaste físico");
        fatigueRBtn.addActionListener(e -> filter(2));
        fatigueRBtn.setOpaque(false);
        fatigueRBtn.setForeground(textColor);
        JRadioButton distanceRBtn = new JRadioButton("Distancia");
        distanceRBtn.addActionListener(e -> filter(3));
        distanceRBtn.setOpaque(false);
        distanceRBtn.setForeground(textColor);
        JRadioButton gas_distanceRBtn = new JRadioButton("Gasolina y distancia");
        gas_distanceRBtn.addActionListener(e -> filter(4));
        gas_distanceRBtn.setOpaque(false);
        gas_distanceRBtn.setForeground(textColor);
        JRadioButton fatigue_distanceRBtn = new JRadioButton("Desgaste físico y distancia");
        fatigue_distanceRBtn.addActionListener(e -> filter(5));
        fatigue_distanceRBtn.setOpaque(false);
        fatigue_distanceRBtn.setForeground(textColor);
        JRadioButton speedRBtn = new JRadioButton("Rapidez");
        speedRBtn.addActionListener(e -> filter(6));
        speedRBtn.setOpaque(false);
        speedRBtn.setForeground(textColor);
        ButtonGroup BtnGroup = new ButtonGroup();
        BtnGroup.add(gasRBtn);
        BtnGroup.add(fatigueRBtn);
        BtnGroup.add(distanceRBtn);
        BtnGroup.add(gas_distanceRBtn);
        BtnGroup.add(fatigue_distanceRBtn);
        BtnGroup.add(speedRBtn);
        pnl2.add(gasRBtn);
        pnl2.add(fatigueRBtn);
        pnl2.add(distanceRBtn);
        pnl2.add(gas_distanceRBtn);
        pnl2.add(fatigue_distanceRBtn);
        pnl2.add(speedRBtn);
        pnl.add(pnl2);
        return pnl;
    }

    private JPanel worstRoutesPnl(){
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15,15,20,15));
        pnl.setOpaque(false);
        JLabel title = new JLabel("Peores rutas:");
        title.setForeground(mainTextColor);
        pnl.add(title);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));
        String[][] data = { { "-", "-" }, { "-", "-" }, { "-", "-" }, { "-", "-" }, { "-", "-" } };
        String[] columnNames = { "Ruta", "Valor" };
        worstTable = new JTable(data, columnNames);
        worstTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        worstTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        worstTable.setPreferredScrollableViewportSize(worstTable.getPreferredSize());
        JScrollPane sp = new JScrollPane(worstTable);

        pnl.add(sp);
        return pnl;
    }
}
