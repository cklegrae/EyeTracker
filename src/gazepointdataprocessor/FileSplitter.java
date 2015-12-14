/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gazepointdataprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is used to split the 2-minute data file into 2 one-minute data files.
 * @author Thien Dinh & Cody Klegraefe
 */
public class FileSplitter {

    public static final int startingIndex = 31;

    public static File splitIntoHalf(File directory) throws FileNotFoundException {
        File files[] = directory.listFiles();
        File storageDir = new File("splitedData");
        storageDir.mkdir();
        // For each file in the directory, split it.
        for (int i = 0; i < files.length; i++) {
            Scanner reader = new Scanner(files[i]);
            // Skip the titles.
            String title = reader.nextLine();
            ArrayList<String> lines = new ArrayList<>();
            // Count the number of lines. (-)(n) = n
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();
            String[] name = files[i].getName().split("\\.");
            File outFileA = new File(storageDir.getName() + "/" + name[0] + "a." + name[1]);
            File outFileB = new File(storageDir.getName() + "/" + name[0] + "b." + name[1]);

            int firstHalf = lines.size() / 2;

            int j = 0;
            PrintWriter writer = new PrintWriter(outFileA);
            writer.println(title);
            for (; j < firstHalf; j++) {
                writer.println(lines.get(j));
            }
            writer.close();

            writer = new PrintWriter(outFileB);
            writer.println(title);
            for (; j < lines.size(); j++) {
                writer.println(lines.get(j));
            }
            writer.close();
        }
        return storageDir;
    }

}
