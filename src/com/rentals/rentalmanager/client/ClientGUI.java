package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.common.SingleHouse;
import com.rentals.rentalmanager.server.ProcessRequest;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static java.time.LocalDate.now;

public class ClientGUI extends JFrame {

    // components
    public JPanel guiPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextField searchField;
    private JTextField rentField;
    public JTextField balanceField;
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
    private JButton deletePropertyButton;

    //class variables
    public String id;
    ArrayList<String> idList = new ArrayList<>();
    private int clicks = 0;
    private LocalDate localDate = now();

    DefaultListModel dlm = new DefaultListModel();

    //constructor
    public ClientGUI() throws IOException {
        this.setEditable(false);

        add(guiPanel);
        addTenantButton.setVisible(false);

        //Action listeners
        addPropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(idList.toString());

                try {
                    addProperty();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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
                    addTenantButton.setVisible(false);
                } else {
                    editPropertyButton.setText("Save Information");
                    try {
                        editProperty();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                    addTenantButton.setVisible(true);
                }
            }
        });

        deletePropertyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    removeFromList();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    //prompts user for property ID and adds to property list
    private void addProperty() throws IOException {
        ClientControls cc = new ClientControls(this);

        String newId = (String)JOptionPane.showInputDialog(
               guiPanel, "Enter property ID", "Client",
               JOptionPane.PLAIN_MESSAGE, null, null, null);

        // attempt to send NEW request to server
        if (cc.addNew(newId)) {
            //add property to GUI list
            dlm.addElement(newId);

            //add ID of property to array of strings
            idList.add(newId);
            propertyList.setSelectedValue(newId, true);
            setDescription(newId);
        } else {
            JOptionPane.showMessageDialog(guiPanel, cc.getErrorMessage());
        }
    }

    // sets fields and text panes to selected property's values
    // TODO add unique values correlating to properties variables
    private void setDescription(String id) {
        // retrieve property from server
        RentalProperty property = null;
        id = (String) this.propertyList.getSelectedValue();

        try {
            ClientControls cc = new ClientControls(this);
            property = cc.getProperty(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        rentField.setText(Double.toString(property.getPrice()));
        balanceField.setText(Double.toString(property.getBalance()));
        moveInDateField.setText(property.getMoveInDate().toString());
        descriptionPane.setText(property.getDescription());
    }

    //sets fields to editable, need to send results to dB
    // TODO update values with text entered in field
    public void editProperty() throws IOException, ClassNotFoundException {
        ClientControls cc = new ClientControls(this);
        id = propertyList.getSelectedValue().toString();
        this.setEditable(true);
        cc.updateProperty(id);
    }

    private void setEditable(boolean editable) {
        rentField.setEditable(editable);
        balanceField.setEditable(editable);
        moveInDateField.setEditable(editable);
        descriptionPane.setEditable(editable);
    }

    private void removeFromList() throws IOException {
        ClientControls cc = new ClientControls(this);
        id = propertyList.getSelectedValue().toString();

        int selectedIndex = propertyList.getSelectedIndex();

        if( selectedIndex !=1) {
            dlm.remove(selectedIndex);
        }

        cc.deleteProperty(id);

    }
}
