/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eyetracker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import weka.core.Instance;

/**
 *
 * @author Thien Dinh & Cody Klegraefe
 */
public class RealTimeDetector implements Runnable {

    // 2 seconds.
    private static final int timeInterval = 1 * 1000;
    private MLPProcessor mlp;
    private ServerCommunicator serverCom;
    private boolean ready;

    public RealTimeDetector() {
        mlp = new MLPProcessor();
        ready = false;
        serverCom = new ServerCommunicator("localhost", 4242);
    }

    public static void main(String[] args) throws Exception {
        RealTimeDetector r = new RealTimeDetector();
        Thread thread = null;
        String command = "";
        Scanner reader = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("What do you want to do? Command: ");
            command = reader.nextLine();
            switch (command) {
                case "quit":
                    if (thread != null) {
                        thread.interrupt();
                    }
                    exit = true;

                    break;
                case "stop":
                    thread.interrupt();
                    r.notReady();
                    break;
                case "start":
                    thread = new Thread(r);
                    thread.start();
                    break;
                case "recognize":
                    r.ready();
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }
    }

    public void ready() {
        ready = true;
    }

    public void notReady() {
        ready = false;
    }

    public boolean isReady() {
        return ready;
    }

    @Override
    public void run() {
        try {

            DecimalFormat df = new DecimalFormat("#.000");
            this.serverCom.connect();
            while (!ready);
            long timer = System.currentTimeMillis();
            // Set up Frame.
            JFrame parent = new JFrame();
            parent.setVisible(false);
            JLabel label = new JLabel();
            label.setFont(new Font("Serif", Font.PLAIN, 50));
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            parent.getContentPane().setBackground(Color.WHITE);
            parent.setSize(350, 250);
            parent.add(label);
            parent.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - parent.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height - parent.getHeight());
            parent.setAlwaysOnTop(true);
            while (!Thread.interrupted()) {
                this.serverCom.readResponse();
                long timeUpdate = System.currentTimeMillis();
                long interval = (long) Math.floor((timeUpdate - timer) / 1000.0) * 1000;
                if (interval - timeInterval == 0) {
                    this.serverCom.unifyDataRecord();
                    if (this.serverCom.isUnifiedDataEmpty()) {
                        continue;
                    }
                    System.out.println("Recognize activity...");
                    //String heli = "0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.32849,0.83752,0.46002,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.32846,0.44351,0.59137,1.11719,0.96914,0.0,0.526,0.95288,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.57507,0.36151,0.72296,0.24615,0.68982,0.37631,0.36151,0.72318,0.62244,0.39413,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.29599,0.75579,0.32849,0.82144,1.39575,0.88711,0.24649,0.26303,0.7067,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.31229,0.24738,0.0,0.22998,0.27948,0.29559,1.11676,0.85434,0.78809,2.8089,1.41313,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.22986,1.42904,1.80738,1.11694,1.41305,0.93573,0.5752,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.2627,0.0,0.96896,0.21347,0.19727,1.19956,0.2626,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.26257,0.0,0.26288,0.85421,0.0,0.0,0.22989,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.60766,1.11716,1.10117,1.26486,0.24652,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.2966,1.33224,2.31586,1.26489,0.24609,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.32864,0.26163,0.87021,1.19977,0.47629,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.82101,0.35861,0.31198,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0";
                    //String pong = "0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.19714,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.41095,0.78839,0.50726,0.0,0.27911,0.26263,0.34509,0.75561,0.0,0.0,1.15002,0.90344,0.54236,0.0,0.0,0.0,0.0,0.0,0.0,0.32855,0.0,1.60968,1.19922,0.78882,0.26288,0.0,0.82135,0.2301,0.0,0.26263,0.29565,0.77167,0.44336,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.75616,0.44372,0.0,0.2464,0.29584,0.21381,0.0,0.0,0.24652,0.2301,0.24609,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.44379,0.55835,0.41034,0.0,0.0,0.0,0.0,0.0,0.0,0.21375,0.24603,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.2135,0.1972,0.0,0.75604,0.21375,0.21356,0.0,0.39398,0.0,0.44409,0.2464,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.32874,0.4275,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.27966,0.19733,0.0,0.19714,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.83765,0.21564,0.19696,0.0,1.08398,0.0,0.42749,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.22974,0.67365,0.0,0.0,0.3941,0.21375,0.0,0.1972,0.0,0.21338,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.41077,0.11493,0.0,0.0,0.75605,0.46051,0.29565,0.21368,0.22992,0.29565,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.70624,0.22974,0.19708,0.26251,0.23035,0.21362,0.0,0.2301,0.21375,0.0,0.24597,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.49286,0.19763,0.62426,0.31177,0.23016,0.2301,0.1972,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.19727,0.19702,0.0,0.0,0.44373,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.21362,0.0,0.0,0.0,0.0,0.21387,0.0,0.23004,0.21295,0.21362,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.19702,0.0,0.49316,0.0,0.0,0.0,0.0,0.19708,0.65734,0.0,0.34503,0.21216,0.23004,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.36139,0.83777,0.31213,1.26513,0.0,0.24634,0.46002,0.75561,1.65856,1.98724,1.01873,0.57489,0.37799,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.36133,0.0,0.0,0.59107,0.0,0.32819,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0";
                    //String tetris = "0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.23035,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.04932,0.93628,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.7063,0.36145,0.0,0.0,0.0,0.31201,0.0,0.27905,0.57483,0.0,0.0,0.0,0.0,0.0,0.82129,0.0,0.0,0.0,0.0,1.24842,3.41735,1.57727,0.0,0.0,0.0,0.42737,2.18481,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.23218,0.64075,0.65674,1.75818,3.56529,0.82117,0.0,0.0,0.0,0.2627,0.0,0.26282,0.0,0.0,0.0,0.0,0.0,0.0,0.37756,0.34509,0.0,0.21375,1.41272,0.73986,0.69018,0.0,0.0,0.0,0.0,0.0,0.62451,1.41222,0.0,0.0,0.0,0.0,0.57483,0.09863,0.50928,0.87036,0.80481,1.70752,0.41064,0.78869,0.50915,0.83777,0.0,0.96948,0.0,0.0,0.95227,0.0,0.31238,0.0,0.0,0.0,0.0,0.0,1.46216,0.39429,1.08374,0.37756,1.87318,0.32874,0.0,0.0,0.0,0.0,0.50891,0.32849,0.0,0.14795,0.39429,0.0,0.0,0.0,0.0,0.42676,0.0,0.42712,1.0841,0.72302,0.77209,0.06567,0.0,0.34497,0.0,0.24646,0.69007,0.80505,0.0,0.54224,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.82141,0.0,0.37769,0.0,0.52588,0.88696,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.2135,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.1969,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.21326,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0";
                    Instance activity = serverCom.getInput();
                    //activity = MLPProcessor.inst.instance(350);
                    //System.out.println("Number of instances: " + activity.classAttribute());
                    //for(int i = 0; i < activity.numAttributes(); i++){
                    //System.out.print(activity.attribute(i) + " ~ ");
                    //}
                    String[] activities = {
                        "Heli", "Pong", "Tetris"
                    };
                    try {
                        double[] v = mlp.getMLP().distributionForInstance(
                                activity);

                        int mostAccuracyIndex = 0;
                        double mostAccuracy = v[mostAccuracyIndex];
                        for (int i = 1; i < v.length; i++) {
                            if (v[i] > mostAccuracy) {
                                mostAccuracyIndex = i;
                                mostAccuracy = v[i];
                            }
                        }
                        System.out.println("Recognized activity: "
                                + activities[mostAccuracyIndex]);
                        String[] txtActivities = new String[3];
                        txtActivities[0] = activities[0] + " " + df.format(v[0]);
                        txtActivities[1] = activities[1] + " " + df.format(v[1]);
                        txtActivities[2] = activities[2] + " " + df.format(v[2]);
                        txtActivities[mostAccuracyIndex] = "<b>" + txtActivities[mostAccuracyIndex] + "</b>";
                        label.setText("<html>" + txtActivities[0] + "<br>"
                                + txtActivities[1] + "<br>" + txtActivities[2] + "</html>");
                        parent.setVisible(true);
                    } catch (Exception ex) {
                        System.out.println("Error occurs: " + ex.getMessage());
                    }
                    timer = timeUpdate;
                } else {
                    //System.out.println("Not equal(" + timeInterval + "):" + interval);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up." + e.getMessage());
        }
    }

}
