/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gazepointdataprocessor;

import static gazepointdataprocessor.GazePointDataProcessor.matrixDimension;
import static gazepointdataprocessor.GazePointDataProcessor.range;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * This class is used to process multiple csv files to a single data file. Each
 * csv file is mapped to one line of data in the data file.
 *
 * @author Thien Dinh & Cody Klegraefe
 */
public class TrainingDataGenerator {

    // An array containing all csv files.
    private File[] csvFiles;
    // Each cvs data is mapped to a line in the unified data file.
    private File unifiedDataFile;    

    /**
     * Constructor taking an array of csv Files.
     *
     * @param csvFiles
     * @param dimension
     */
    public TrainingDataGenerator(File[] csvFiles) {
        this.csvFiles = csvFiles;
        unifiedDataFile = new File("trainingData.csv");
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
        for (int i = 0; i < matrixDimension; i++) {
            for (int j = 0; j < matrixDimension; j++) {
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
     * Extract data from a processed csv file.
     *
     * @param csvFile
     * @return a string represent the data in csv file.
     */
    private String extractData(File csvFile) throws FileNotFoundException {
        Scanner fileReader = new Scanner(csvFile);
        // Create a 5x5 table. Each cell is a small area of the screen.
        // The value of each cell is the total eye fixation within that area.
        double[][] durationDistribution = new double[matrixDimension][matrixDimension];
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
    
    /**
     * Combine all the data recorded and transform them into one summary line.
     * @param dataLines the formate is "x1,x2,x3,..."
     * @return 
     */
    public static String extractData(LinkedList<String> datLines){
        // Create a 5x5 table. Each cell is a small area of the screen.
        // The value of each cell is the total eye fixation within that area.
        double[][] durationDistribution = new double[matrixDimension][matrixDimension];        
        LinkedList<String> dataLines = new LinkedList<>(datLines);
        ListIterator<String> dataIterator = dataLines.listIterator();
        // Read data.
        while (dataIterator.hasNext()) {
            String[] strData = dataIterator.next().split(",");
            Double[] doubleData = new Double[strData.length];
            for (int i = 0; i < strData.length; i++) {
                doubleData[i] = Double.valueOf(strData[i]);
            }
            // Figure out which cell should this data belong to.
            int interval = range / matrixDimension;
            double x = doubleData[0].doubleValue();
            int valueX = (int) Math.ceil(x * range);
            if (valueX != 0) {
                if (valueX % interval == 0) {
                    valueX = valueX / interval - 1;
                } else {
                    valueX = valueX / interval;
                }
            }
            double y = doubleData[1].doubleValue();
            int valueY = (int) Math.ceil(y * range);
            if (valueY != 0) {
                if (valueY % interval == 0) {
                    valueY = valueY / interval - 1;
                } else {
                    valueY = valueY / interval;
                }
            }
            double duration = doubleData[2];
            durationDistribution[valueX][valueY] += duration;
        }

        // After extracting all the data in the csv, return one line summation of data.
        String dataSummation = "";
        for (int i = 0; i < durationDistribution.length; i++) {
            for (int j = 0; j < durationDistribution[0].length; j++) {
                dataSummation += (float) (durationDistribution[i][j]) + ",";
            }
        }
        return dataSummation;
    }

}
