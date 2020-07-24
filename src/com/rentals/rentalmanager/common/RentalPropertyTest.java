package com.rentals.rentalmanager.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RentalPropertyTest {
    private static String path;
    private static String[][] rows;
    private static RentalProperty[] properties;
    private static LocalDate currentDate;

    public static void main(String[] args) throws FileNotFoundException {


        // collect name of file/dB
        path = args[0];

        //total amount of properties
        int propertyIndex = numberOfProperties();

        //each row of file to row of string array
        rows = fileToArray(propertyIndex);

        //create properties
        properties = newRental(propertyIndex, rows, currentDate);
        printProperties();

        //add a tenant(s) to a property
        addTenant(properties);
        printTenants();

    }


    //iterate through rows of file, return number of properties
    private static int numberOfProperties() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        int propertyIndex = 0;

        while(true) {
            if (scanner.hasNextLine()) {
                propertyIndex += 1;
                scanner.nextLine();
            } else
                break;
        }
        return propertyIndex;
    }

    //loop through file, add rental information to string array
    private static String[][] fileToArray(int propertyIndex) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
        String[][] someRows = new String[propertyIndex][5];

        while(scanner.hasNext() == true) {
            for(int i = 0; i < numberOfProperties(); i++) {
                String[] tempRow = scanner.nextLine().trim().split(" ");
                for(int j = 0; j < tempRow.length; j++) {
                    someRows[i][j] = tempRow[j];
                }
            }

        }
        return someRows;
    }

    //creates an array of properties, iterates through rows & assigns property to type
    private static RentalProperty[] newRental(int propertyIndex, String[][] rows, LocalDate currentDate) {
        RentalProperty[] rentalProperty = new RentalProperty[propertyIndex];

        for(int i = 0; i < propertyIndex; i++) {
                if(rows[i][0].equals("S")) {
                    rentalProperty[i] = new SingleHouse(Double.parseDouble(rows[i][1]), Double.parseDouble(rows[i][2]),
                    rows[i][3], rows[i][4],currentDate);
                } else if(rows[i][0].equals("A")) {
                    rentalProperty[i] = new Apartment(Double.parseDouble(rows[i][1]), Double.parseDouble(rows[i][2]),
                            rows[i][3], rows[i][4],currentDate);
                } else
                    rentalProperty[i] = new VacationRental(Double.parseDouble(rows[i][1]), Double.parseDouble(rows[i][2]),
                            rows[i][3], rows[i][4],currentDate);
        }
        return rentalProperty;
    }

    // iterates through properties, prints appropriate type
    private static void printProperties() {
        for(RentalProperty currentRental : properties) {
            if(currentRental instanceof SingleHouse) {
                System.out.println(currentRental);
            } else if(currentRental instanceof Apartment) {
                System.out.println(currentRental);
            } else
                System.out.println(currentRental);
        }
    }


    // adds tenant(s) to an array and assign to a property
    private static void addTenant(RentalProperty[] properties) throws InputMismatchException {
      Scanner input = new Scanner(System.in);
      int cont = 1;

      while (cont == 1) {

          System.out.print("Property Index: ");
          int propertyNum = input.nextInt();

          System.out.print("\nHow many tenants? ");
          int numTenants = input.nextInt();
          Tenant[] newTenant = new Tenant[numTenants];


          for (int i = 0; i < numTenants; i++) {
              newTenant[i] = new Tenant();
              System.out.println("\nFirst name of tenant: ");
              newTenant[i].setFirstName(input.next());

              System.out.println("Last name of tenant: ");
              newTenant[i].setLastName(input.next());

              System.out.println("Email of tenant: ");
              newTenant[i].setEmail(input.next());

              properties[propertyNum].setTenants(newTenant);
          }

          System.out.println("Add tenants to another property? '1' or '0'");

          cont = input.nextInt();
          if (cont == 0) {
              break;
          }
          else if (cont == 1) {
              continue;
          }
      }
    }

    // prints array of tenants of each property
    private static void printTenants() throws FileNotFoundException {

        for(int i = 0; i < numberOfProperties(); i++) {
            System.out.print("Tenants of property #" + (i + 1) + " ");
            System.out.println(Arrays.deepToString(properties[i].getTenants()));
        }


    }

}


