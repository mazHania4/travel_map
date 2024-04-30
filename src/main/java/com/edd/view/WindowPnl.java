package com.edd.view;

import com.edd.controller.GraphsCtr;
import com.edd.controller.GraphvizCtr;
import com.edd.controller.Tree;
import com.edd.graphs.Node;
import com.edd.graphs.Route;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private JLabel imgLbl;
    private JTable worstTable;
    private GraphsCtr graphs;
    private Tree tree;
    private final GraphvizCtr graphviz;
    private int graphCounter, filter, currTime;
    private Node actualNode;

    private String selectedOrigin, selectedDestination, selectedTripType;


    public WindowPnl(){
        graphviz = new GraphvizCtr();
        graphCounter = 0;
        currTime = 0;
        filter = 1;
        actualNode = null;
        graphs = null;
        tree = null;
        selectedOrigin = "";

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

        JPanel mainPnl = mainPnl();
        add(mainPnl, BorderLayout.CENTER);


    }

    private void updateImg(java.util.List<Node> nodes, List<Node> route, int lblCase){
        try {
            graphviz.mapGraph(nodes, route, selectedOrigin, graphCounter, lblCase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgLbl.setIcon(null);
        imgLbl.revalidate();
        imgLbl.repaint();
        String path = "src/main/resources/generated/map_"+graphCounter+".png";
        ImageIcon imageIcon = new ImageIcon(path);
        imgLbl.setIcon(imageIcon);
        imgLbl.revalidate();
        imgLbl.repaint();
        graphCounter++;

    }

    private void openFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivos");
        fileChooser.setMultiSelectionEnabled(true);
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            if (selectedFiles.length != 2) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione exactamente 2 archivos .txt", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String routesContent = readFile(selectedFiles[0]);
                String trafficContent = readFile(selectedFiles[1]);
                graphs = new GraphsCtr(routesContent, trafficContent);
                updateImg(graphs.getNodes().values().stream().toList(), new ArrayList<>(), 1);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer los archivos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    private void treeGraph(){
        if (tree != null){

        }
    }
    private void newTrip(){
        if (graphs != null){
            JFrame newTripFrame = new JFrame("Selección de Viaje");
            newTripFrame.setLocationRelativeTo(null);
            JPanel panel = new JPanel();
            String[] nodes = graphs.getNodes().keySet().toArray(new String[0]);
            ArrayList<String> tripType = new ArrayList<>();
            tripType.add("Caminando");
            tripType.add("Vehículo");
            JComboBox<String> originCB = new JComboBox<>(nodes);
            JComboBox<String> destinationCB = new JComboBox<>(nodes);
            JComboBox<String> tripTypeCB = new JComboBox<>(tripType.toArray(new String[0]));
            JButton continueBtn = new JButton("Continuar");
            continueBtn.addActionListener(e -> {
                    selectedOrigin = (String) originCB.getSelectedItem();
                    selectedDestination = (String) destinationCB.getSelectedItem();
                    selectedTripType = (String) tripTypeCB.getSelectedItem();
                    if (selectedDestination.equals(selectedOrigin)){
                        JOptionPane.showMessageDialog(this, "El origen y el destino no pueden ser el mismo", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        originTF.setText(selectedOrigin);
                        destTF.setText(selectedDestination);
                        typeTF.setText(selectedTripType);
                        actualNode = graphs.getNodes().get(selectedOrigin);
                        nextNodeCB.setEnabled(true);
                        updateTrip();
                        newTripFrame.dispose();
                    }
                }
            );

            panel.add(new JLabel("Origen:"));
            panel.add(originCB);
            panel.add(new JLabel("Destino:"));
            panel.add(destinationCB);
            panel.add(new JLabel("Tipo de Viaje:"));
            panel.add(tripTypeCB);
            panel.add(continueBtn);
            newTripFrame.getContentPane().add(panel);
            newTripFrame.pack();
            newTripFrame.setVisible(true);
        }
    }

    private void updateTrip(){
        if (!selectedOrigin.isEmpty() && !selectedOrigin.equals(selectedDestination)) {
            List<Route> routes = graphs.findAllRoutes(selectedOrigin, selectedDestination, selectedTripType.equals("Vehículo"), currTime);
            //System.out.println("generadas:\n" + routes);
            tree = new Tree(filter);
            routes.forEach(r -> tree.insert(r));
            List<Route> routesInOrder = tree.inOrder();
            //System.out.println("ordenadas:\n" + routesInOrder);
            updateImg(graphs.getNodes().values().stream().toList(), routesInOrder.get(0).getSteps(), filter);
            nextNodeCB.removeAllItems();
            Set<String> nextIds = new HashSet<>();
            routesInOrder.forEach(r-> nextIds.add(r.getSteps().get(1).getId()));
            nextIds.forEach(n -> nextNodeCB.addItem(n));
            // update worst table
        }
    }


    private void moveForward() { //to the next node
        if (graphs != null){
            selectedOrigin = (String) nextNodeCB.getSelectedItem();
            actualNode = graphs.getNodes().get(selectedOrigin);
            updateTrip();
        }
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
        JButton newTripBtn = new JButton("Nuevo viaje");
        newTripBtn.setForeground(mainTextColor);
        newTripBtn.setBackground(buttonColor);
        newTripBtn.addActionListener(e ->  newTrip());
        JButton treeBtn = new JButton("Ver árbol");
        treeBtn.setForeground(mainTextColor);
        treeBtn.setBackground(buttonColor);
        treeBtn.addActionListener(e ->  treeGraph());
        JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl3.setOpaque(false);
        pnl3.add(treeBtn);
        pnl3.add(Box.createRigidArea(new Dimension(5, 10)));
        pnl3.add(newTripBtn);
        pnl.add(pnl3);
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
        JRadioButton gasRBtn = new JRadioButton("Distancia");
        gasRBtn.addActionListener(e -> { filter = 1; updateTrip(); });
        gasRBtn.setOpaque(false);
        gasRBtn.setForeground(textColor);
        JRadioButton fatigueRBtn = new JRadioButton("Desgaste físico");
        fatigueRBtn.addActionListener(e -> { filter = 2; updateTrip(); });
        fatigueRBtn.setOpaque(false);
        fatigueRBtn.setForeground(textColor);
        JRadioButton distanceRBtn = new JRadioButton("Gasolina");
        distanceRBtn.addActionListener(e -> { filter = 3; updateTrip(); });
        distanceRBtn.setOpaque(false);
        distanceRBtn.setForeground(textColor);
        JRadioButton gas_distanceRBtn = new JRadioButton("Desgaste físico y distancia");
        gas_distanceRBtn.addActionListener(e -> { filter = 4; updateTrip(); });
        gas_distanceRBtn.setOpaque(false);
        gas_distanceRBtn.setForeground(textColor);
        JRadioButton fatigue_distanceRBtn = new JRadioButton("Gasolina y distancia");
        fatigue_distanceRBtn.addActionListener(e -> { filter = 5; updateTrip(); });
        fatigue_distanceRBtn.setOpaque(false);
        fatigue_distanceRBtn.setForeground(textColor);
        JRadioButton speedRBtn = new JRadioButton("Rapidez");
        speedRBtn.addActionListener(e -> { filter = 6; updateTrip(); });
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

    private JPanel mainPnl(){
        JPanel pnl = new JPanel();
        pnl.setOpaque(false);
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setBorder(new EmptyBorder(15,15,20,15));
        JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl2.setOpaque(false);
        JLabel timeLbl = new JLabel("Hora actual:");
        timeLbl.setForeground(mainTextColor);
        pnl2.add(timeLbl);
        JTextField timeTF = new JTextField(4);
        timeTF.setText(String.valueOf(currTime));
        timeTF.setEditable(false);
        timeTF.setBackground(tfsColor);
        timeTF.setBorder(new EmptyBorder(3,4,3,4));
        timeTF.setForeground(highlightTextColor);
        pnl2.add(timeTF);
        JButton changeTimeBtn = new JButton("Cambiar hora");
        changeTimeBtn.setForeground(mainTextColor);
        changeTimeBtn.setBackground(buttonColor);
        changeTimeBtn.addActionListener(e ->  {
            currTime = Integer.parseInt(JOptionPane.showInputDialog("Escriba la nueva hora"));
            timeTF.setText(String.valueOf(currTime));
        });
        pnl2.add(changeTimeBtn);
        JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnl3.setOpaque(false);
        imgLbl = new JLabel();
        pnl3.add(imgLbl);
        pnl.add(pnl2);
        pnl.add(Box.createRigidArea(new Dimension(0, 10)));
        pnl.add(pnl3);
        return pnl;
    }
}
