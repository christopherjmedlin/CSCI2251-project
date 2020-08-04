package com.rentals.rentalmanager.server.db;

import com.rentals.rentalmanager.common.RentalProperty;
import com.rentals.rentalmanager.common.Tenant;

import java.sql.*;
import java.util.logging.Logger;

import static java.sql.DriverManager.*;

/**
 * Used for retrieving, updating, and creating RentalProperties in the database
 */
public class TenantQueries {
    private static final Logger LOGGER = Logger.getLogger(TenantQueries.class.getName());

    private Connection db;

    private PreparedStatement updateTenant;
    private PreparedStatement newTenant;
    private PreparedStatement tenantsByProperty;
    private PreparedStatement deleteTenant;
    private PreparedStatement deleteTenantsByProperty;

    public TenantQueries(String username, String password) throws SQLException {
        this(getConnection(DatabaseUtilities.URL, username, password));
    }

    //TODO add information logging to the methods below

    /**
     * This constructor exists because this is often used from the PropertyQueries class, where a connection already is
     * formed
     */
    public TenantQueries(Connection db) {
        this.db = db;
        try {
            // creates a new tenant
            // the string array is the generated keys, which can be retrieved after running the prepared statement
            this.newTenant = db.prepareStatement(
                    "INSERT INTO tenants (property, name) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            // gets every tenant associated with a property
            this.tenantsByProperty = db.prepareStatement(
                    "SELECT * FROM tenants WHERE property=?"
            );

            // updates a specific tenant
            this.updateTenant = db.prepareStatement(
                    "UPDATE tenants " +
                     "SET name=?, email=?, phone=? WHERE id=?"
            );

            this.deleteTenant = this.db.prepareStatement(
                    "DELETE FROM tenants " +
                            "WHERE id=?"
            );

            this.deleteTenantsByProperty = this.db.prepareStatement(
                    "DELETE FROM tenants " +
                     "WHERE property=?"
            );
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }
    }

    public int newTenant(String property, String name) {
        try {
            this.newTenant.setString(1, property);
            this.newTenant.setString(2, name);
            this.newTenant.execute();
            // get the newly generated id
            ResultSet id = this.newTenant.getGeneratedKeys();
            if (id.next())
                return id.getInt(1);
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        return 0;
    }

    /**
     * Using the addTenant method, this method will retrieve every tenant associated with the given property from the
     * database and add them to it.
     */
    public void getTenantsForProperty(RentalProperty property) {
        try {
            this.tenantsByProperty.setString(1, property.getId());
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        try(ResultSet results = this.tenantsByProperty.executeQuery()) {
            while(results.next()) {
                String fullName = results.getString("name");
                String[] split = fullName.split(" ");
                property.addTenant(new Tenant(
                        results.getInt("id"),
                        split[0], split[1],
                        results.getString("email"),
                        results.getString("phone")
                ));
            }
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }
    }

    /**
     * Updates the row in the database corresponding to the given Tenant object.
     */
    public int updateTenant(Tenant t) {
        try {
            this.updateTenant.setString(1, t.getFullName());
            this.updateTenant.setString(2, t.getEmail());
            this.updateTenant.setString(3, t.getPhone());
            this.updateTenant.setInt(4, t.getId());

            return this.updateTenant.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        return 0;
    }

    public int deleteTenant(int id) {
        LOGGER.info("Deleting tenant from database with id " + id + ".");
        try {
            deleteTenant.setInt(1, id);
            return deleteTenant.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        return 0;
    }

    public int deleteTenantsByProperty(String propertyId) {
        LOGGER.info("Deleting tenants in database with property id " + propertyId + ".");
        try {
            deleteTenantsByProperty.setString(1, propertyId);
            return deleteTenantsByProperty.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.toString());
        }

        return 0;
    }
}
