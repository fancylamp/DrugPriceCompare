package ui;

import javax.swing.*;

import static model.PriceCalc.run;

public class Main {

    public static void main(String[] args) {
        try{
            model.PriceCalc NewInstance = new model.PriceCalc();
            NewInstance.load();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new MainMenuFrame("USA shipping Price calculator", NewInstance);
                }
            });
        }
        catch(Exception e){
            System.out.println("Could not find save file!");
        }
    }
}
