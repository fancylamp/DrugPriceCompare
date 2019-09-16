package ui;

import com.google.gson.*;
import model.Drug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
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
        Double basePrice = 0.0;
        Double USPrice = 0.0;

        String enterName = drugName.getText();

        newDrug.setName(enterName);
        basePrice = Double.parseDouble(String.format("%.2f",Double.parseDouble(drugPrice.getText()) / Double.parseDouble(drugQty.getText())));
        newDrug.setPrice(basePrice);

        String replaced = enterName.replaceAll(" ","+");
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
        } catch (IOException exc){
            System.out.println("IOException");
        }

        newDrug.setUSPrice(Double.parseDouble(String.format("%.2f",USPrice)));
        newDrug.setOurPrice(Double.parseDouble(String.format("%.2f",0.75*((USPrice - basePrice)*0.3+basePrice))));

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

        try {
            HttpClient client = HttpClient.newHttpClient();
            String content = Files.readString(Paths.get("DrugList.txt"));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.jsonbin.io/b/5d7dbfb1de91160d2872834c"))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(content)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        }catch (Exception exc){
            exc.printStackTrace();
        }
        dispose();

    }
}