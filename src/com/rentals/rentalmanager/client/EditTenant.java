package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.Tenant;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EditTenant extends  JFrame {

    //gui
    Container container;
    private JTextField firstName, lastName, email, phone;
    private JLabel first, last, emailaddress, phoneNumber;
    private JButton save;
    private JButton deleteTenant;

    public EditTenant(Tenant tenant) {
        setTitle("Edit Tenant Information");
        setSize(350,250);

        container = getContentPane();
        container.setLayout(null);

        first = new JLabel("First name:");
        first.setBounds(80,15,100,20);
        container.add(first);

        last = new JLabel("Last name:");
        last.setBounds(80,60,100,20);
        container.add(last);

        emailaddress = new JLabel("Email:");
        emailaddress.setBounds(110,105,100,20);
        container.add(emailaddress);

        phoneNumber = new JLabel("Phone number:");
        phoneNumber.setBounds(60,150,100,20);
        container.add(phoneNumber);

        firstName = new JTextField(8);
        firstName.setText(tenant.getFirstName());
        firstName.setBounds(150,15,100,20);
        container.add(firstName);

        lastName = new JTextField(8);
        lastName.setText(tenant.getLastName());
        lastName.setBounds(150,60,100,20);
        container.add(lastName);

        email = new JTextField(8);
        email.setText(tenant.getEmail());
        email.setBounds(150,105,100,20);
        container.add(email);

        phone = new JTextField(8);
        phone.setText(tenant.getPhone());
        phone.setBounds(150,150,100,20);
        container.add(phone);

        save = new JButton();
        save.setText("Save");
        save.setBounds(15,185,150,20);
        container.add(save);

        deleteTenant = new JButton();
        deleteTenant.setText("Delete Tenant");
        deleteTenant.setBounds(170,185,150,20);
        container.add(deleteTenant);

        save.addActionListener(e -> {
            setTenantInfo(tenant);
            super.dispose();
        });

        setVisible(true);

    }

    public void setTenantInfo(Tenant tenant) {
        tenant.setFirstName(firstName.getText());
        tenant.setLastName(lastName.getText());
        tenant.setEmail(email.getText());
        tenant.setPhone(phone.getText());


    }








    // public static void main(String[] args) throws IOException {
  //      EditTenant editTenant = new EditTenant();
   // }



}
