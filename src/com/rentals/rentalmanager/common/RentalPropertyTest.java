package com.rentals.rentalmanager.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class RentalPropertyTest {
    private static String path;
    private static String[][] rows;
    private static RentalProperty[] properties;
    private static LocalDate currentDate;

    public static void main(String[] args) throws FileNotFoundException {

        path = args[0];

        int propertyIndex = numberOfProperties();
        System.out.println(propertyIndex);

        rows = fileToArray(propertyIndex);

        properties = newRental(propertyIndex, rows, currentDate);
        //System.out.println(Arrays.deepToString(properties));
        printProperties();

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
        System.out.println(Arrays.deepToString(someRows));
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
    public static void printProperties() {
        for(RentalProperty currentRental : properties) {
            if(currentRental instanceof SingleHouse) {
                System.out.println(currentRental);
            } else if(currentRental instanceof Apartment) {
                System.out.println(currentRental);
            } else
                System.out.println(currentRental);
        }
    }
}
