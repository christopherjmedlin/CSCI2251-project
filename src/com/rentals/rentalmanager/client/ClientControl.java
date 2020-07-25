package com.rentals.rentalmanager.client;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.server.db.PropertyQueries;

import javax.swing.*;
import java.util.List;

public class ClientControl extends JFrame {

 PropertyQueries propertyQueries = new PropertyQueries("testing", "testing");
 ClientGUI gui = new ClientGUI();


 //does not function
 public void InitialQuery() {
    List<RentalProperty> properties = propertyQueries.getAllProperties();
    DefaultListModel dlm = new DefaultListModel();
    gui.propertyList.setModel(dlm);

    for(int i = 0; i < properties.size(); i++) {
        String property = properties.get(i).toString();
        dlm.addElement(property);

    }

 }


}
