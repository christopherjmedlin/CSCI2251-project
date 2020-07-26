package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;


import static java.time.LocalDate.now;

public class ClientGUI extends JFrame {

    // components
    public JPanel guiPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField searchField;
    private JTextField rentField;
    private JTextField balanceField;
    public JList propertyList;
    JButton addPropertyButton;
    private JButton editPropertyButton;
    private JLabel searchLabel;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JLabel tenInfoLabel;
    private JLabel frontyardLabel;
    private JLabel garageLabel;
    private JList tenantList;
    private JTextPane descriptionPane;
    private JTextField moveInDateField;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton generateStatementButton;
    private JButton addTenantButton;

    //class variables
    private String id;
    private boolean editable = false;
    private int clicks = 0;
    private LocalDate localDate = now();

    DefaultListModel dlm = new DefaultListModel();
    //PropertyQueries propertyQueries = new PropertyQueries();


    //constructor
    public ClientGUI() {

        add(guiPanel);
        addTenantButton.setVisible(false);
        this.pack();


        //Action listeners

        addPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              addProperty();
            }
        });
        //apply properties to property list
        propertyList.setModel(dlm);

        propertyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setDescription(id);
            }
        });
        editPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //clicks allows function of button to alternate, when clicks is
                // odd property is editable, when even properties are view only.
                // TODO send edited properties to dB
                clicks++;

                if (clicks % 2 == 0) {
                    editPropertyButton.setText("Edit Property");
                    setDescription(id);
                    addTenantButton.setVisible(false);
                } else {
                    editPropertyButton.setText("Save Information");
                    editProperty();
                    addTenantButton.setVisible(true);

                }
            }
        });
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    //methods

    //prompts user for property ID and adds to property list
    private void addProperty() {

        String id = (String)JOptionPane.showInputDialog(
                guiPanel, "Enter property ID", "Client",
                JOptionPane.PLAIN_MESSAGE, null, null, null);

        dlm.addElement(id);
    }

    //sets fields and text panes to properties values
    // TODO add unique values correlating to properties variables
    private void setDescription(String id) {
       // if(propertyList.getSelectedIndex() != -1) {
            String data = "" + (propertyList.getSelectedIndex() + 1);

            String text = rentField.getText();
            rentField.setText(text);
            rentField.setEditable(false);
            balanceField.setText(data);
            balanceField.setEditable(false);
            moveInDateField.setText(data);
            moveInDateField.setEditable(false);
            descriptionPane.setText("property #" + data);
            descriptionPane.setEditable(false);

        }

    //}

    //sets fields to editable, need to send results to dB
    // TODO update values with text entered in field
    public void editProperty() {

        rentField.setEditable(true);
        balanceField.setEditable(true);
        moveInDateField.setEditable(true);
        descriptionPane.setEditable(true);

    }


}
