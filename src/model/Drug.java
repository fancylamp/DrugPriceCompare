package model;

public class Drug {
    private String name;
    private double unitPrice;

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(double price){
        unitPrice = price;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return unitPrice;
    }

}
