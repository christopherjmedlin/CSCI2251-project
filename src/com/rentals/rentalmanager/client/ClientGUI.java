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
                try {

                    setDescription(id);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
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

                    try {

                        setDescription(id);

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }

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
        ClientControls cc = new ClientControls();

        String id = (String)JOptionPane.showInputDialog(
               guiPanel, "Enter property ID", "Client",
               JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (cc.idAlreadyExists(id) == true) {
            JOptionPane.showMessageDialog(guiPanel,"Property with ID already exists.");

        } else {
            //add property to dB
            cc.addNew(id);

            //add property to GUI list
            dlm.addElement(id);

            //add ID of property to array of strings
            idList.add(id);
        }
    }

    //sets fields and text panes to properties values
    // TODO add unique values correlating to properties variables
    private void setDescription(String id) throws IOException, ClassNotFoundException {
        ClientControls cc = new ClientControls();

        // sets title, but breaks deleteProperty for some reason.
        //String selected = propertyList.getSelectedValue().toString();
        //rightPanel.setBorder(BorderFactory.createTitledBorder("Property " + selected + " description"));


        if (cc.idAlreadyExists(id) == true) {

            rentField.setText("");
            rentField.setEditable(false);
            balanceField.setText("");
            balanceField.setEditable(false);
            moveInDateField.setText("");
            moveInDateField.setEditable(false);
            descriptionPane.setText("");
            descriptionPane.setEditable(false);
        } else {

            String data = "" + (propertyList.getSelectedIndex() + 1);

            rentField.setText(data);
            rentField.setEditable(false);
            balanceField.setText(data);
            balanceField.setEditable(false);
            moveInDateField.setText(data);
            moveInDateField.setEditable(false);
            descriptionPane.setText("property #" + data);
            descriptionPane.setEditable(false);
        }

    }

    //sets fields to editable, need to send results to dB
    // TODO update values with text entered in field
    public void editProperty() throws IOException, ClassNotFoundException {
        ClientControls cc = new ClientControls();
        id = propertyList.getSelectedValue().toString();

        rentField.setEditable(true);
        balanceField.setEditable(true);
        moveInDateField.setEditable(true);
        descriptionPane.setEditable(true);

        cc.updateProperty(id);


    }

    private void removeFromList() throws IOException {
        ClientControls cc = new ClientControls();
        id = propertyList.getSelectedValue().toString();

        int selectedIndex = propertyList.getSelectedIndex();

        if( selectedIndex !=1) {
            dlm.remove(selectedIndex);
        }

        cc.deleteProperty(id);

    }
}
