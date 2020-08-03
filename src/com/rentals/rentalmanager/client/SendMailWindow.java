package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.BillingStatement;
import com.rentals.rentalmanager.common.RentalProperty;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendMailWindow extends JFrame {
    private List<JCheckBox> checkBoxList;
    private String host;
    private ClientGUI parent;
    private RentalProperty property;

    public SendMailWindow(RentalProperty property, String host, ClientGUI parent) {
        setTitle("Send Billing Statement");
        setSize(350,250);

        this.checkBoxList = new ArrayList<JCheckBox>();
        this.host = host;
        this.parent = parent;
        this.property = property;

        Container container = getContentPane();
        container.add(new JLabel("Select emails:"));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        for (String name : property.getTenantNames()) {
            // create check boxes for every tenant with email
            String email = property.getTenant(name).getEmail();
            if (email != null) {
                JCheckBox check = new JCheckBox(email);
                checkBoxList.add(check);
                this.add(check);
            }
        }
        JButton send = new JButton("Send");
        container.add(send);
        send.addActionListener(e -> send());

        this.setVisible(true);
    }

    public void send() {
        List<String> addresses = new ArrayList<>();

        // get the email of each checkbox that is selected
        for (JCheckBox check : checkBoxList) {
            if (check.isSelected()) {
                addresses.add(check.getText());
            }
        }

        BillingStatement statement = new BillingStatement(this.property);
        try {
            ClientControls cc = new ClientControls(parent, host);
            if (!cc.sendMail(addresses, statement.getStatement()))
                JOptionPane.showMessageDialog(this, cc.getErrorMessage());
            else
                JOptionPane.showMessageDialog(this, "Email(s) successfuly sent");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error sending mail request to server");
        }

        super.dispose();
    }
}
