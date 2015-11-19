/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gazepointdataprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class is used to process multiple csv files to a single data file. Each
 * csv file is mapped to one line of data in the data file.
 *
 * @author Thien Dinh
 */
public class TrainingDataGenerator {

    // An array containing all csv files.
    private File[] csvFiles;
    // Each cvs data is mapped to a line in the unified data file.
    private File unifiedDataFile;
    // Dimension.
    private int dimension;

    /**
     * Constructor taking an array of csv Files.
     *
     * @param csvFiles
     * @param dimension
     */
    public TrainingDataGenerator(File[] csvFiles, int dimension) {
        this.csvFiles = csvFiles;
        unifiedDataFile = new File("trainingData.csv");
        this.dimension = dimension;
    }

    /**
     * Create a data file that each of its lines is the transformation of each
     * csv file.
     *
     * @return training data csv file.
     * @throws java.io.FileNotFoundException
     */
    public File generateUnifiedDataFile() throws FileNotFoundException {
        // The generated file will be traingData.csv
        PrintWriter fileWriter = new PrintWriter(unifiedDataFile);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                fileWriter.print("[" + i + ":" + j + "],");
            }
        }
        fileWriter.println("Game");
        // Process cvs files one by one.
        for (int i = 0; i < csvFiles.length; i++) {
            fileWriter.println(extractData(csvFiles[i]));
        }
        // Close file after reading.
        fileWriter.close();
        return unifiedDataFile;
    }

    /**
     * Extract data from a csv file.
     *
     * @param csvFile
     * @return a string represent the data in csv file.
     */
    private String extractData(File csvFile) throws FileNotFoundException {
        Scanner fileReader = new Scanner(csvFile);
        // Create a 5x5 table. Each cell is a small area of the screen.
        // The value of each cell is the total eye fixation within that area.
        double[][] durationDistribution = new double[dimension][dimension];
        if (fileReader.hasNextLine()) {
            // Read titles.
            fileReader.nextLine();
        }
        // Read data.
        while (fileReader.hasNextLine()) {
            String[] strData = fileReader.nextLine().split(",");
            Double[] doubleData = new Double[strData.length];
            for (int i = 0; i < strData.length; i++) {
                doubleData[i] = Double.valueOf(strData[i]);
            }
            // Figure out which cell should this data belong to.
            int x = (int) doubleData[0].doubleValue();
            int y = (int) doubleData[1].doubleValue();
            double duration = doubleData[2];
            durationDistribution[x][y] += duration;
        }
        // Close file after reading.
        fileReader.close();

        // After extracting all the data in the csv, return one line summation of data.
        String dataSummation = "";
        for (int i = 0; i < durationDistribution.length; i++) {
            for (int j = 0; j < durationDistribution[0].length; j++) {
                dataSummation += (float) (durationDistribution[i][j]) + ",";
            }
        }
        // Append the activity of this data by its file name.
        // Assuming that the activity is listed as before the character ]
        String[] fileName = csvFile.getName().split("_");
        return dataSummation + fileName[0];
    }

}
