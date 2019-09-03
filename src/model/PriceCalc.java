package model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class PriceCalc {
    public static ArrayList<Drug> MasterList = new ArrayList<>();

    public static void run(){
        // model for core user interaction

        mainMenu();
    }

    public static void load() throws FileNotFoundException {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Drug>>(){}.getType();
        MasterList = gson.fromJson(new FileReader("DrugList.txt"),listType);
    }

    public static void mainMenu(){
        System.out.println("1 to input a canadian drug, 2 to generate a quote, 3 TO TEST LIST");
        Scanner input = new Scanner(System.in);
        String option = input.nextLine();
        if (option.equals("1")){
            try{
                drugInput();
            } catch (Exception e){
                System.out.println("error in saving list");
            }
        } else if (option.equals("2")){
            try {
                generateQuote();
            } catch (Exception e){
                System.out.println("cannot find drug!");
            }
        } else if (option.equals("3")){
            printList();
        }
    }

    public static void drugInput() throws IOException{
        System.out.println("Input name then unit price");
        Drug newDrug = new Drug();
        Scanner input= new Scanner(System.in);
        newDrug.setName(input.nextLine());
        newDrug.setPrice(Double.parseDouble(input.nextLine()));
        MasterList.add(newDrug);
        Gson gson = new Gson();
        String serializedList = gson.toJson(MasterList);
        System.out.println(serializedList);
        FileWriter fw = new FileWriter("DrugList.txt");
        fw.write(serializedList);
        fw.close();
        mainMenu();
    }


    public static void generateQuote() throws IOException{
        System.out.println("Input name and quantity");
        Scanner input = new Scanner(System.in);
        String name = input.nextLine();
        double CANPrice;
        for (Drug d: MasterList){
            if (d.getName().equals(name)){
                CANPrice = d.getPrice();
            }
        }

        String replaced = name.replaceAll(" ","+");
        System.out.println(replaced);
        String endpoint = "https://data.medicaid.gov/resource/rt4v-78r4.json";
        endpoint = endpoint +  "?ndc_description=" + replaced;
        System.out.println(endpoint);

        URL url = new URL(endpoint);
        URLConnection request = url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonArray rootArr = root.getAsJsonArray();
        JsonObject rootObj = rootArr.get(0).getAsJsonObject();
        double USPrice = rootObj.get("nadac_per_unit").getAsDouble();
        System.out.println(USPrice);

    }

    public static void printList(){
        for (Drug d: MasterList){
            System.out.println(d.getName());
            System.out.println(d.getPrice());
        }
    }
}
