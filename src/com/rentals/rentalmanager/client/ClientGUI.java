package com.rentals.rentalmanager.client;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.rentals.rentalmanager.common.PropertySearch;
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
import java.time.format.DateTimeParseException;
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
    private String host;
    public String id;
    ArrayList<String> idList = new ArrayList<>();
    private int clicks = 0;
    private LocalDate localDate = now();
    DefaultListModel dlm = new DefaultListModel();

    private RentalProperty selectedProperty;

    //constructor
    public ClientGUI(String host) throws IOException {
        add(guiPanel);
        addTenantButton.setVisible(false);
        this.host = host;

        //Action listeners
        addPropertyButton.addActionListener(e -> {
            System.out.println(idList.toString());

            try {
                addProperty();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //apply properties to property list
        propertyList.setModel(dlm);

        propertyList.addListSelectionListener(e -> setDescription(id));

        editPropertyButton.addActionListener(e -> {
            //clicks allows function of button to alternate, when clicks is
            // odd property is editable, when even properties are view only.
            clicks++;

            if (clicks % 2 == 0) {
                setEditable(false);
                try {
                    updateProperty();
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(guiPanel, "Error: " + exc.toString());
                }
            } else {
                setEditable(true);
            }
        });

        deletePropertyButton.addActionListener(e -> {
            try {
                removeFromList();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        comboBox1.addActionListener(e -> search());
        comboBox2.addActionListener(e -> search());
        searchField.addActionListener(e -> search());

        // populate with initial search
        this.search();
    }

    public JPanel getGuiPanel() {
        return guiPanel;
    }

    //prompts user for property ID and adds to property list
    private void addProperty() throws IOException {
        ClientControls cc = new ClientControls(this, this.host);

        String newId = (String) JOptionPane.showInputDialog(
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
            ClientControls cc = new ClientControls(this, this.host);
            property = cc.getProperty(id);
            if (property == null) return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.selectedProperty = property;
        rentField.setText(Double.toString(property.getPrice()));
        balanceField.setText(Double.toString(property.getBalance()));
        moveInDateField.setText(property.getMoveInDate().toString());
        descriptionPane.setText(property.getDescription());
    }

    //sets fields to editable, need to send results to dB
    // TODO update values with text entered in field
    public void updateProperty() throws IOException, ClassNotFoundException {
        try {
            this.selectedProperty.setBalance(Double.valueOf(balanceField.getText()));
            this.selectedProperty.setPrice(Double.valueOf(rentField.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(guiPanel, "Invalid number for rent and/or balance.");
        }

        try {
            this.selectedProperty.setMoveIn(LocalDate.parse(this.moveInDateField.getText()));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(guiPanel, "Invalid move in date.");
        }
        this.selectedProperty.setDescription(descriptionPane.getText());

        ClientControls cc = new ClientControls(this, this.host);
        if (!cc.updateProperty(this.selectedProperty))
            JOptionPane.showMessageDialog(guiPanel, "Unexpected server error.");
    }

    private void setEditable(boolean editable) {
        editPropertyButton.setText(editable ? "Save Information" : "Edit Property");
        rentField.setEditable(editable);
        balanceField.setEditable(editable);
        moveInDateField.setEditable(editable);
        descriptionPane.setEditable(editable);
        addTenantButton.setVisible(editable);
    }

    private void search() {
        int paymentStatus = 0;
        switch (this.comboBox2.getSelectedIndex()) {
            case 0:
                paymentStatus = 1;
                break;
            case 1:
                paymentStatus = 2;
                break;
            case 2:
                paymentStatus = 0;
                break;
        }

        PropertySearch searchParameters = new PropertySearch(
                this.searchField.getText(),
                paymentStatus,
                comboBox1.getSelectedIndex() == 1
        );

        List<String> ids = new ArrayList<>(0);
        try {
            ids = new ClientControls(this, this.host).search(searchParameters);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dlm.clear();
        dlm.addAll(ids);
    }

    private void removeFromList() throws IOException {
        ClientControls cc = new ClientControls(this, this.host);
        id = propertyList.getSelectedValue().toString();

        int selectedIndex = propertyList.getSelectedIndex();

        if (selectedIndex != 1) {
            dlm.remove(selectedIndex);
        }

        cc.deleteProperty(id);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        guiPanel = new JPanel();
        guiPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        guiPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        guiPanel.add(splitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        splitPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setLeftComponent(leftPanel);
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        searchLabel = new JLabel();
        searchLabel.setText("Search properties:");
        leftPanel.add(searchLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchField = new JTextField();
        leftPanel.add(searchField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scrollPane = new JScrollPane();
        leftPanel.add(scrollPane, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        propertyList = new JList();
        scrollPane.setViewportView(propertyList);
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("No Tenants");
        defaultComboBoxModel1.addElement("Tenants");
        comboBox1.setModel(defaultComboBoxModel1);
        leftPanel.add(comboBox1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox2 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Due Date Approaching");
        defaultComboBoxModel2.addElement("Past Due");
        defaultComboBoxModel2.addElement("N/A");
        comboBox2.setModel(defaultComboBoxModel2);
        leftPanel.add(comboBox2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deletePropertyButton = new JButton();
        deletePropertyButton.setText("Delete Property");
        leftPanel.add(deletePropertyButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addPropertyButton = new JButton();
        addPropertyButton.setText("Add Property");
        leftPanel.add(addPropertyButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setRightComponent(rightPanel);
        tenInfoLabel = new JLabel();
        Font tenInfoLabelFont = this.$$$getFont$$$(null, -1, 12, tenInfoLabel.getFont());
        if (tenInfoLabelFont != null) tenInfoLabel.setFont(tenInfoLabelFont);
        tenInfoLabel.setText("Tenant Information");
        rightPanel.add(tenInfoLabel, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        frontyardLabel = new JLabel();
        frontyardLabel.setText("Balance:");
        rightPanel.add(frontyardLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        garageLabel = new JLabel();
        garageLabel.setText("Rent:");
        rightPanel.add(garageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editPropertyButton = new JButton();
        editPropertyButton.setText("Edit Property");
        rightPanel.add(editPropertyButton, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tenantList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        tenantList.setModel(defaultListModel1);
        rightPanel.add(tenantList, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        descriptionPane = new JTextPane();
        descriptionPane.setEditable(false);
        descriptionPane.setText("Property description string will be displayed here");
        rightPanel.add(descriptionPane, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        generateStatementButton = new JButton();
        generateStatementButton.setText("Generate Statement");
        rightPanel.add(generateStatementButton, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addTenantButton = new JButton();
        addTenantButton.setText("Add Tenant");
        rightPanel.add(addTenantButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rentField = new JTextField();
        rentField.setEditable(false);
        rightPanel.add(rentField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        balanceField = new JTextField();
        balanceField.setEditable(false);
        balanceField.setText("");
        rightPanel.add(balanceField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        moveInDateField = new JTextField();
        moveInDateField.setEditable(false);
        rightPanel.add(moveInDateField, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Move in date:");
        rightPanel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return guiPanel;
    }

}
