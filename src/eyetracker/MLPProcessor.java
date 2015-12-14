/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eyetracker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author Thien Dinh & Cody Klegraefe
 */
public class MLPProcessor {
    
        private MultilayerPerceptron mlp;
        public static Instances inst;

        public MLPProcessor() {
            try {
                FileReader fr = new FileReader("trainingData.arff");
                Instances training = new Instances(fr);
                training.setClassIndex(training.numAttributes() - 1);
                mlp = new MultilayerPerceptron();
                mlp.setOptions(Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 5"));
                mlp.buildClassifier(training);
                
                FileReader tr = new FileReader("trainingData.arff");
                Instances testdata = new Instances(tr);
                inst = testdata;
                testdata.setClassIndex(testdata.numAttributes() - 1);
                Evaluation eval = new Evaluation(training);
                eval.evaluateModel(mlp, testdata);
                System.out.println(eval.toSummaryString("\nResults\n*******\n", false));
                tr.close();
                fr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        
        public MultilayerPerceptron getMLP(){
            return mlp;
        }
    }
