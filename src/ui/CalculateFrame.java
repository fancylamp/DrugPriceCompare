package ui;

import com.google.gson.*;
import model.Drug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CalculateFrame extends JDialog implements ActionListener {

    JTextField drugName;
    JTextField drugQty;
    ArrayList<Drug> MasterList;
    JTextArea USAPrice;
    JTextArea ourPrice;
    private double usaShow;
    private double ourShow;

    public CalculateFrame(ArrayList<Drug> MasterList) {
        this.MasterList = MasterList;
        setSize(1000, 800);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);



        JPanel panel = new JPanel(null);
        panel.setBackground(Color.YELLOW);

        // Components
        JTextArea enterTitle = new JTextArea("Calculate price for a drug (Please ensure the drug has been entered)");
        enterTitle.setEditable(false);
        enterTitle.setFont(new Font("Calibri", Font.ITALIC, 20));
        enterTitle.setBackground(Color.YELLOW);

        JTextArea drugNameTitle = new JTextArea("Name of the drug:");
        drugNameTitle.setEditable(false);
        drugNameTitle.setFont(new Font("Calibri", Font.PLAIN, 20));
        drugNameTitle.setBackground(Color.YELLOW);

        drugName = new JTextField();
        drugName.setFont(new Font("Calibri", Font.PLAIN, 20));

        JTextArea drugQtyTitle = new JTextArea("Quantity (number of units):");
        drugQtyTitle.setEditable(false);
        drugQtyTitle.setFont(new Font("Calibri", Font.PLAIN, 20));
        drugQtyTitle.setBackground(Color.YELLOW);

        drugQty = new JTextField();
        drugQty.setFont(new Font("Calibri", Font.PLAIN, 20));

        USAPrice = new JTextArea("USA Price: $" + usaShow + " USD");
        USAPrice.setEditable(false);
        USAPrice.setFont(new Font("Calibri", Font.PLAIN, 20));
        USAPrice.setBackground(Color.YELLOW);

        ourPrice = new JTextArea("Our Price: $" + ourShow + " USD");
        ourPrice.setEditable(false);
        ourPrice.setFont(new Font("Calibri", Font.PLAIN, 20));
        ourPrice.setBackground(Color.YELLOW);

        JButton createButton = new JButton("Calculate!");
        createButton.addActionListener(this);


        // Setting bounds
        enterTitle.setBounds(100,100,800,50);
        drugNameTitle.setBounds(100,200,300,50);
        drugName.setBounds(100,300,300,50);
        drugQtyTitle.setBounds(100,400,300,50);
        drugQty.setBounds(100,500,200,50);
        createButton.setBounds(100,600,200,50);
        USAPrice.setBounds(500,200,300,50);
        ourPrice.setBounds(500,300,300,50);


        panel.add(enterTitle);
        panel.add(drugNameTitle);
        panel.add(drugName);
        panel.add(drugQtyTitle);
        panel.add(drugQty);
        panel.add(createButton);
        panel.add(USAPrice);
        panel.add(ourPrice);

        setContentPane(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String queryName = drugName.getText();
        double CDNPrice = 0;
        double USPrice = 0;
        for (Drug d: MasterList){
            if (d.getName().equals(queryName)){
                CDNPrice = d.getPrice() * 0.75;
            }
        }
        String replaced = queryName.replaceAll(" ","+");
        System.out.println(replaced);
        String endpoint = "https://data.medicaid.gov/resource/rt4v-78r4.json";
        endpoint = endpoint +  "?ndc_description=" + replaced;
        System.out.println(endpoint);
        try {
            URL url = new URL(endpoint);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            JsonArray rootArr = root.getAsJsonArray();
            JsonObject rootObj = rootArr.get(0).getAsJsonObject();
            USPrice = rootObj.get("nadac_per_unit").getAsDouble();
        } catch (Exception exc){
            System.out.println("Something went wrong.");
        }

        usaShow = USPrice * Integer.parseInt(drugQty.getText());
        System.out.println(usaShow);
        ourShow = ((USPrice - CDNPrice)*0.3+CDNPrice) * Integer.parseInt(drugQty.getText()) + 7;
        System.out.println(ourShow);
        USAPrice.setText("USA Price: $" + usaShow + " USD");
        ourPrice.setText("Our Price: $" + ourShow + " USD");
    }
}