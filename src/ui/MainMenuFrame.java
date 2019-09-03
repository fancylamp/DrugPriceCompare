package ui;

import model.Drug;
import model.PriceCalc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenuFrame extends JFrame implements ActionListener {

    private JButton enterDrugButton;
    private JButton calculateButton;
    private JButton printButton;

    public ArrayList<Drug> MasterList;

    public MainMenuFrame(String title, PriceCalc pc){
        super(title);
        this.MasterList = pc.MasterList;
        setSize(1000,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        JPanel panel = new JPanel(null);
        panel.setBackground(Color.white);

        // Components
        JTextArea welcomeMessage = new JTextArea("Item price calculator for USA prescription patients");
        welcomeMessage.setFont(new Font("Calibri", Font.BOLD, 25));
        welcomeMessage.setEditable(false);
        JTextArea subMessage = new JTextArea("Please ensure that the drug has been entered to the list with KnF prices before calculating!!");
        subMessage.setEditable(false);
        subMessage.setFont(new Font("Calibri",Font.PLAIN,15));

        enterDrugButton = new JButton("Enter a new drug");
        enterDrugButton.addActionListener(this);
        calculateButton = new JButton("Calculate price");
        calculateButton.addActionListener(this);
        printButton = new JButton("View a list of entered drugs");
        printButton.addActionListener(this);

        // Setting bounds
        welcomeMessage.setBounds(250,100,600,75);
        subMessage.setBounds(225,200,600,50);
        enterDrugButton.setBounds(100,300,200,50);
        calculateButton.setBounds(400,300,200,50);
        printButton.setBounds(700,300,200,50);


        // Adding components
        panel.add(welcomeMessage);
        panel.add(subMessage);
        panel.add(enterDrugButton);
        panel.add(calculateButton);
        panel.add(printButton);

        setContentPane(panel);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() ==  enterDrugButton){
            new EnterDrugFrame(MasterList);
        }
        else if (e.getSource() == calculateButton){
            new CalculateFrame(MasterList);
        }
        else if (e.getSource() == printButton){
            new PrintFrame(MasterList);
        }
    }





}