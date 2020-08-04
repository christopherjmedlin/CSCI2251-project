package com.rentals.rentalmanager.client;
import com.rentals.rentalmanager.common.Tenant;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AddTenant extends JFrame {

    public Container tenantPrompt;
    private JTextField firstNameField, lastNameField;
    private JLabel firstLabel, lastLabel;
    private JButton confirm, cancel;

    private String id;
    private String first = "";
    private String last = "";
    private String phone = "";
    private String email = "";
    ClientGUI gui;

    public AddTenant(String id, ClientGUI gui) throws IOException {
        setTitle("Add Tenant");
        setSize(250, 150);

        this.id = id;
        this.gui = gui;

        tenantPrompt = getContentPane();
        tenantPrompt.setLayout(null);

        firstLabel = new JLabel("First name");
        firstLabel.setBounds(35, 15, 100, 20);
        tenantPrompt.add(firstLabel);

        firstNameField = new JTextField(8);
        firstNameField.setText("");
        firstNameField.setBounds(15, 35, 100, 20);
        tenantPrompt.add(firstNameField);

        lastLabel = new JLabel("Last name");
        lastLabel.setBounds(140, 15, 100, 20);
        tenantPrompt.add(lastLabel);

        lastNameField = new JTextField(8);
        lastNameField.setText("");
        lastNameField.setBounds(120, 35, 100, 20);
        tenantPrompt.add(lastNameField);

        confirm = new JButton();
        confirm.setText("Confirm");
        confirm.setBounds(15, 70, 100, 30);
        tenantPrompt.add(confirm);

        cancel = new JButton();
        cancel.setText("Cancel");
        cancel.setBounds(120, 70, 100, 30);
        tenantPrompt.add(cancel);

        cancel.addActionListener(e -> {
            super.dispose();
        });

        confirm.addActionListener(e -> {
            try {
                confirmTenant();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void confirmTenant() throws IOException {
        ClientControls cc = new ClientControls(gui, null);

        if(firstNameField.getText().equals("") | lastNameField.getText().equals("")) {
            JOptionPane.showMessageDialog(tenantPrompt, "Must enter first and last name values");

        } else if (!((firstNameField.getText().equals("")) && (lastNameField.getText().equals("")))) {
            first = firstNameField.getText();
            last = lastNameField.getText();

            String name = first + " " + last;
            int tenantId = cc.addNewTenant(id, name);
            gui.addTenantToList(tenantId, first, last);
            super.dispose();
        }

    }
}

