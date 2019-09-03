package ui;

import com.google.gson.Gson;
import model.Drug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.util.ArrayList;

public class EnterDrugFrame extends JDialog implements ActionListener {

    JTextField drugName;
    JTextField drugPrice;
    JTextField drugQty;
    ArrayList<Drug> MasterList;

    public EnterDrugFrame(ArrayList<Drug> MasterList) {
        this.MasterList = MasterList;
        setSize(1000, 1000);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);



        JPanel panel = new JPanel(null);
        panel.setBackground(Color.PINK);

        // Components
        JTextArea enterTitle = new JTextArea("Enter a new drug:");
        enterTitle.setEditable(false);
        enterTitle.setFont(new Font("Calibri", Font.ITALIC, 35));
        enterTitle.setBackground(Color.PINK);

        JTextArea drugNameTitle = new JTextArea("Name of the drug:");
        drugNameTitle.setEditable(false);
        drugNameTitle.setFont(new Font("Calibri", Font.PLAIN, 20));
        drugNameTitle.setBackground(Color.PINK);

        drugName = new JTextField();
        drugName.setFont(new Font("Calibri", Font.PLAIN, 20));

        JTextArea drugPriceTitle = new JTextArea("Price of the drug:");
        drugPriceTitle.setEditable(false);
        drugPriceTitle.setFont(new Font("Calibri", Font.PLAIN, 20));
        drugPriceTitle.setBackground(Color.PINK);

        drugPrice = new JTextField();
        drugPrice.setFont(new Font("Calibri", Font.PLAIN, 20));

        JTextArea drugQtyTitle = new JTextArea("Quantity (number of units):");
        drugQtyTitle.setEditable(false);
        drugQtyTitle.setFont(new Font("Calibri", Font.PLAIN, 20));
        drugQtyTitle.setBackground(Color.PINK);

        drugQty = new JTextField();
        drugQty.setFont(new Font("Calibri", Font.PLAIN, 20));

        JButton createButton = new JButton("Enter drug!");
        createButton.addActionListener(this);


        // Setting bounds
        enterTitle.setBounds(100,100,800,50);
        drugNameTitle.setBounds(100,200,800,50);
        drugName.setBounds(100,250,800,50);
        drugPriceTitle.setBounds(100,350,800,50);
        drugPrice.setBounds(100,400,800,50);
        drugQtyTitle.setBounds(100,500,800,50);
        drugQty.setBounds(100,550,800,50);
        createButton.setBounds(100,700,300,50);


        panel.add(enterTitle);
        panel.add(drugNameTitle);
        panel.add(drugName);
        panel.add(drugPriceTitle);
        panel.add(drugPrice);
        panel.add(drugQtyTitle);
        panel.add(drugQty);
        panel.add(createButton);

        setContentPane(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Drug newDrug = new Drug();
        newDrug.setName(drugName.getText());
        newDrug.setPrice(Double.parseDouble(drugPrice.getText()) / Double.parseDouble(drugQty.getText()));
        MasterList.add(newDrug);
        Gson gson = new Gson();
        String serializedList = gson.toJson(MasterList);
        System.out.println(serializedList);
        try {
            FileWriter fw = new FileWriter("DrugList.txt");
            fw.write(serializedList);
            fw.close();
        } catch (Exception exc){
            System.out.println("File not found!");
        }
        dispose();

    }
}