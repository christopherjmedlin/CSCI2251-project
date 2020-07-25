package com.rentals.rentalmanager.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ClientGUI extends JFrame {

    // components
    private JPanel guiPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField searchField;
    private JTextField roomsField;
    private JTextField garageField;
    private JTextField frontyardField;
    public JList propertyList;
    private JButton addPropertyButton;
    private JButton saveInformationButton;
    private JLabel searchLabel;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JLabel tenInfoLabel;
    private JLabel frontyardLabel;
    private JLabel garageLabel;
    private JLabel roomsLabel;
    private JList list1;
    private JTextPane descriptionPane;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton generateStatementButton;

    //constructor
    public ClientGUI() {
        add(guiPanel);
        this.pack();

    }

    //methods

    public JPanel getGuiPanel() {
        return guiPanel;
    }


}
