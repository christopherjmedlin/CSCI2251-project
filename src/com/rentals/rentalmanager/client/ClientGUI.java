package com.rentals.rentalmanager.client;

<<<<<<< Updated upstream
import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.RequestType;
import com.rentals.rentalmanager.common.SingleHouse;
=======
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.rentals.rentalmanager.common.*;
>>>>>>> Stashed changes
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
import java.util.Arrays;
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
    private JButton editTenantButton;

    //class variables
    public String id;
    ArrayList<String> idList = new ArrayList<>();
    private int clicks = 0;
    private LocalDate localDate = now();
<<<<<<< Updated upstream
=======
    DefaultListModel dlmProperty = new DefaultListModel();
    DefaultListModel dlmTenant = new DefaultListModel();

>>>>>>> Stashed changes

    DefaultListModel dlm = new DefaultListModel();

    //constructor
    public ClientGUI() throws IOException {

        add(guiPanel);
        addTenantButton.setVisible(false);
<<<<<<< Updated upstream

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
=======
        editTenantButton.setVisible(false);
        this.host = host;

        //Action listeners
        addPropertyButton.addActionListener(e -> {
            System.out.println(idList.toString());

            try {
                addProperty();
            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
>>>>>>> Stashed changes
            }
        });

        //apply properties to property list
        propertyList.setModel(dlmProperty);

<<<<<<< Updated upstream
        propertyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
=======
        propertyList.addListSelectionListener(e -> {
            try {
                setDescription(id);
                setTenantDescription();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
                try {
                    removeFromList();
                } catch (IOException ioException) {
=======
        // populate with initial search
        this.search();

        addTenantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addTenant();
                } catch (IOException | ClassNotFoundException ioException) {
>>>>>>> Stashed changes
                    ioException.printStackTrace();
                }
            }
        });
<<<<<<< Updated upstream
=======

        editTenantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tenantList.getSelectedValue() != null) {
                    testTenant();
                }

            }
        });
>>>>>>> Stashed changes
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    //prompts user for property ID and adds to property list
<<<<<<< Updated upstream
    private void addProperty() throws IOException {
        ClientControls cc = new ClientControls();
=======
    private void addProperty() throws IOException, ClassNotFoundException {
        ClientControls cc = new ClientControls(this, this.host);
>>>>>>> Stashed changes

        String id = (String)JOptionPane.showInputDialog(
               guiPanel, "Enter property ID", "Client",
               JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (cc.idAlreadyExists(id) == true) {
            JOptionPane.showMessageDialog(guiPanel,"Property with ID already exists.");

        } else {
            //add property to dB
            cc.addNew(id);

            //add property to GUI list
<<<<<<< Updated upstream
            dlm.addElement(id);
=======
            dlmProperty.addElement(newId);
>>>>>>> Stashed changes

            //add ID of property to array of strings
            idList.add(id);
        }
    }

<<<<<<< Updated upstream
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
=======
    // sets fields and text panes to selected property's values
    private void setDescription(String id) throws IOException, ClassNotFoundException {
        // retrieve property from server
        RentalProperty property = null;
        id = (String) this.propertyList.getSelectedValue();

        try {
            ClientControls cc = new ClientControls(this, this.host);
            property = cc.getProperty(id);
            if (property == null) return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
>>>>>>> Stashed changes
        }

    }

    //sets fields to editable, need to send results to dB
<<<<<<< Updated upstream
    // TODO update values with text entered in field
    public void editProperty() throws IOException, ClassNotFoundException {
        ClientControls cc = new ClientControls();
        id = propertyList.getSelectedValue().toString();
=======
    public void updateProperty() throws IOException, ClassNotFoundException {
        try {
            this.selectedProperty.setBalance(Double.valueOf(balanceField.getText()));
            this.selectedProperty.setPrice(Double.valueOf(rentField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(guiPanel, "Invalid number for rent and/or balance.");
        }
>>>>>>> Stashed changes

        rentField.setEditable(true);
        balanceField.setEditable(true);
        moveInDateField.setEditable(true);
        descriptionPane.setEditable(true);

        cc.updateProperty(id);

<<<<<<< Updated upstream

=======
    private void setEditable(boolean editable) {
        editPropertyButton.setText(editable ? "Save Information" : "Edit Property");
        rentField.setEditable(editable);
        balanceField.setEditable(editable);
        moveInDateField.setEditable(editable);
        descriptionPane.setEditable(editable);
        addTenantButton.setVisible(editable);
        editTenantButton.setVisible(editable);
    }

    private void search() {
        int paymentStatus = 0;
        switch (this.comboBox2.getSelectedIndex()) {
            case 0:
                paymentStatus = 1;
            case 1:
                paymentStatus = 2;
            case 2:
                paymentStatus = 0;
        }

        PropertySearch searchParameters = new PropertySearch(
                this.searchField.getText(),
                paymentStatus,
                comboBox2.getSelectedIndex() == 1
        );

        List<String> ids = new ArrayList<>(0);
        try {
            ids = new ClientControls(this, this.host).search(searchParameters);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dlmProperty.clear();
        dlmProperty.addAll(ids);
>>>>>>> Stashed changes
    }

    private void removeFromList() throws IOException {
        ClientControls cc = new ClientControls();
        id = propertyList.getSelectedValue().toString();

        int selectedIndex = propertyList.getSelectedIndex();

<<<<<<< Updated upstream
        if( selectedIndex !=1) {
            dlm.remove(selectedIndex);
=======
        if (selectedIndex != 1) {
            dlmProperty.remove(selectedIndex);
>>>>>>> Stashed changes
        }
        cc.deleteProperty(id);
<<<<<<< Updated upstream

    }
=======
    }

    public void addTenant() throws IOException, ClassNotFoundException {
        ClientControls cc = new ClientControls(this, this.host);
        id = propertyList.getSelectedValue().toString();
        String name = "";

        //dialog box with two text fields
        JTextField firstNameField = new JTextField(8);
        JTextField lastNameField = new JTextField(8);

        JPanel tenantPrompt = new JPanel();
        tenantPrompt.add(new JLabel("First name:"));
        tenantPrompt.add(firstNameField);
        tenantPrompt.add(Box.createHorizontalStrut(15));
        tenantPrompt.add(new JLabel("Last name:"));
        tenantPrompt.add(lastNameField);

        int result = JOptionPane.showConfirmDialog(null, tenantPrompt, "Enter Tenant's First and Last Name",
                JOptionPane.OK_CANCEL_OPTION);

        if (!((firstNameField.getText()) == null) & !((lastNameField.getText()) == null)) {
            if (result == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                name = firstName + " " + lastName;
                cc.addNewTenant(id, name);
            } else {
                JOptionPane.showMessageDialog(tenantPrompt, "Both fields must filled before adding a tenant.");
            }
        }
    }

    // TODO Update tenant list when new tenant is added & edited
    public void setTenantDescription() throws IOException, ClassNotFoundException {
       ClientControls cc = new ClientControls(this, this.host);

       String[] tenants = this.selectedProperty.getTenantNames();
       boolean hasTenants = cc.hasTenant(this.selectedProperty);

       //important to repaint jList, duplicates tenants without this
       dlmTenant.removeAllElements();

       if(hasTenants) {
           for (int i = 0; i < tenants.length; i++) {
               dlmTenant.addElement(tenants[i]);
               tenantList.setModel(dlmTenant);
           }
       }
    }

    public void testTenant() {
        Tenant tenant;
        tenant = this.selectedProperty.getTenant(tenantList.getSelectedValue().toString());
        EditTenant et = new EditTenant(tenant);
    }

>>>>>>> Stashed changes
}

