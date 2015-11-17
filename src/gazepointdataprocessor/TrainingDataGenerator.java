/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gazepointdataprocessor;

import java.io.File;

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

    /**
     * Constructor taking an array of csv Files.
     *
     * @param csvFiles
     */
    public TrainingDataGenerator(File[] csvFiles) {
        this.csvFiles = csvFiles;
    }

    /**
     * Create a data file that each of its lines is the transformation of
     * each csv file.
     * @return
     */
    public File generateUnifiedDataFile() {
        //Double[][] 
        return null;
    }

}
