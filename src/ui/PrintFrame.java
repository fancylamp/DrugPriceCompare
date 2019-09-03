package ui;

import model.Drug;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class PrintFrame extends JDialog{

    ArrayList<Drug> MasterList;

    public PrintFrame(ArrayList<Drug> MasterList) {
        this.MasterList = MasterList;
        setSize(1000, 1000);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        JPanel panel = new JPanel(null);
        panel.setBackground(Color.LIGHT_GRAY);

        // Components
        JTextArea listTitle = new JTextArea("A list of entered drugs. Please note the dosage.");
        listTitle.setFont(new Font("Calibri", Font.ITALIC, 35));
        listTitle.setEditable(false);
        listTitle.setBackground(Color.LIGHT_GRAY);
        ArrayList<String> nameList = new ArrayList<>();

        for (Drug d: MasterList){ ;
            nameList.add(d.getName());
        }

        System.out.println(nameList.size());


        JList drugList = new JList(nameList.toArray());
        drugList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        drugList.setLayoutOrientation(JList.VERTICAL);
        drugList.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(drugList);
        listScroller.setPreferredSize(new Dimension(250, 80));

        listScroller.setBackground(Color.LIGHT_GRAY);

        // Setting bounds
        listScroller.setBounds(100,200,800,500);
        listTitle.setBounds(100,100,800,50);

        // Adding components
        panel.add(listTitle);
        panel.add(listScroller);

        setContentPane(panel);

        setVisible(true);
    }


}