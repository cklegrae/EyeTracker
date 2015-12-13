/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eyetracker;

import gazepointdataprocessor.TrainingDataGenerator;
import java.util.LinkedList;
import weka.core.Instance;
import weka.core.SparseInstance;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Thien Dinh & Cody
 */
public class ServerCommunicator {

    // How many attribute does it have?
    private LinkedList<String> dataRecord;
    private String unifiedData;

    private String hostname;
    private int port;
    private Socket socketClient;
    private DataOutputStream outToServer;
    private BufferedReader stdIn;

    /**
     *
     */
    public ServerCommunicator(String hostname, int port) {
        dataRecord = new LinkedList<>();
        unifiedData = "";
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException {
        System.out.println("Attempting to connect to " + hostname + ":" + port);
        socketClient = new Socket(hostname, port);
        OutputStream os = socketClient.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        String sendMessage = "<SET ID=\"ENABLE_SEND_DATA\" STATE=\"1\" />\r\n";
        bw.write(sendMessage);
        String sendMessages = "<SET ID=\"CALIBRATE_START\" STATE=\"1\" />\r\n";
        bw.write(sendMessages);
        sendMessages = "<SET ID=\"CALIBRATE_SHOW\" STATE=\"1\" />\r\n";
        bw.write(sendMessages);
        sendMessages = "<SET ID=\"ENABLE_SEND_POG_FIX\" STATE=\"1\" />\r\n";
        bw.write(sendMessages);
        bw.flush();

        System.out.println("Message sent to the server : " + sendMessage);
        System.out.println("Connection Established");
        
        
        stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
    }

    public void readResponse() throws IOException {
        String userInput;
        //System.out.println("Response from server:");
        if ((userInput = stdIn.readLine()) != null) {
            //System.out.println(userInput);
            int xIndex = userInput.indexOf("FPOGX=");
            int yIndex = userInput.indexOf("FPOGY=");
            int dIndex = userInput.indexOf("FPOGD=");
            if (xIndex > -1) {
                String x = userInput.substring(xIndex + 7, xIndex + 14);
                String y = userInput.substring(yIndex + 7, yIndex + 14);
                String d = userInput.substring(dIndex + 7, dIndex + 14);
                double xCoor = Double.parseDouble(x);
                double yCoor = Double.parseDouble(y);
                double dTime = Double.parseDouble(d);
                if (xCoor >= 0 && xCoor <= 1 && yCoor >= 0 && yCoor <= 1) {
                    // Values are valid.
                    String formatedData = xCoor + ", " + yCoor + ", " + dTime;
                    addEditedData(formatedData);
                }
            }
        }
    }

    public void addEditedData(String formatedData) {
        dataRecord.add(formatedData);
    }
    
    public boolean isUnifiedDataEmpty(){
        return this.unifiedData.equals("");
    }

    public void unifyDataRecord() {
        unifiedData = TrainingDataGenerator.extractData(dataRecord);
        dataRecord.clear();
    }

    public Instance getInput() {
        // For all the attribute, initialize them.
        int totalAttribute = MLPProcessor.inst.firstInstance().numAttributes();
        Instance instance = new SparseInstance(totalAttribute);
        instance.setDataset(MLPProcessor.inst);
        String[] attributes = unifiedData.split(",");
        //String[] attributes = examData.split(",");
        for (int i = 0; i < totalAttribute - 1; i++) {
            instance.setValue(i, Double.valueOf(attributes[i]));
        }
        return instance;
    }

}
