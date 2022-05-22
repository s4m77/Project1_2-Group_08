package com.mygdx.golf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import com.badlogic.gdx.math.Vector2;


public class FileInputManager {

    protected static String filePathInput = "inputs/input_5.txt";
    protected static String filePathVelocity = "inputs/velocity.txt";
    
    public static int shots;
    private BufferedReader read;
    private URL pathInput;
    private URL pathVelocity;

    /**
     * array storing the input.txt file values and velocity.txt values
     * stores, in order: [0]:x0, [1]:y0, [2]:xt, [3]:yt, [4]:r, [5]:muk, [6]:mus
     * [7]:heightProfile, [8]:sandPitX, [9]:sandPitY, [10]:muks, [11]:muss
     * [12]:v0x, [13]:v0y
     */
    protected String[] input;
    Vector2 initialPos;
    Vector2 targetPos;
    float radius;
    float grassKinetic;
    float grassStatic;
    String heightProfile;
    float sandKinetic;
    float sandStatic;
    double[] sandPitCoords;
    double[] lakeCoords;

    /**
     * Constructor for a FileInputManager obj
     */
    public FileInputManager(){
        shots = 0;
        this.pathInput = FileInputManager.class.getResource(filePathInput);
        this.pathVelocity = FileInputManager.class.getResource(filePathVelocity);
        
        try {readFile();} 
        catch (IOException e) {e.printStackTrace();}
    }
    /**
     * method responsible for reading and storing the input values
     * @throws IOException if file not found 
     */
    public void readFile() throws IOException {
        input = new String[16]; 
        read = new BufferedReader(new FileReader(new File(pathInput.getFile())));

        for (int i = 0; i < 14; i++) {
            
            String s = read.readLine();
            // System.out.println(s);
            char[] c = s.toCharArray();
            for (int j = 0; j < c.length; j++) {
                
                Character chr = c[j];
                if(chr.equals('=')){
                    StringBuilder build = new StringBuilder(s);
                    s = build.substring(j+1);
                    input[i] = s;
                    break;
                }
            }
        }

        read = new BufferedReader(new FileReader(new File(pathVelocity.getFile())));
        for (int i = 14; i < input.length; i++) {
            String s1 = read.readLine();
            char[] c1 = s1.toCharArray();
            for (int j = 0; j < c1.length-1; j++) {
                
                Character chr = c1[j];
                if(chr.equals('=')){
                    StringBuilder build = new StringBuilder(s1);
                    s1 = build.substring(j+1);
                    input[i] = s1;
                    break;
                }
            }
        }
        read.close();
        initialPos = new Vector2(Float.parseFloat(input[0]), Float.parseFloat(input[1]));
        targetPos = new Vector2(Float.parseFloat(input[2]), Float.parseFloat(input[3]));
        radius = Float.parseFloat(input[4]);
        grassKinetic = Float.parseFloat(input[5]);
        grassStatic = Float.parseFloat(input[6]);
        heightProfile = input[7];
        sandKinetic =  Float.parseFloat(input[10]);
        sandStatic =  Float.parseFloat(input[11]);
        sandPitCoords = calcSandPitCoords();
        lakeCoords = calcLakeCoords();

    }
    //ACCESSORS
    public String[] getInputArray() {return this.input;}
    public Vector2 getInitialPos() {
        return initialPos;
    }
    public Vector2 getTargetPos() {
        return targetPos;
    }
    public float getRadius()        {return radius;}
    public float grassKinetic()     {return grassKinetic;}
    public float grassStatic()      {return grassStatic;}
    public String getHeightProfile() {return heightProfile;}
    public float sandKinetic()      {return sandKinetic;}
    public float sandStatic()       {return sandStatic;}
    public double[] getSandPitCoords() {
        return sandPitCoords;
    }
    public double[] getLakeCoords() {
        return lakeCoords;
    }
    /**
     * this method translates an input of, e.g., -1<x<1
     * @return arr[0]: beginning; arr[1] end
     */
    private double[] calcSandPitCoords(){            
        double[] result = new double[4];
        char[] s = input[8].toCharArray();
        for (int i = 0; i < s.length; i++) {
            Character c = s[i];
            if (c.equals('x')){
                StringBuilder build = new StringBuilder(input[8]);
                String sub1 = build.substring(0, i-1);
                String sub2 = build.substring(i+2);
                result[0] = Double.parseDouble(sub1);
                result[1] = Double.parseDouble(sub2);
                
            }
        }

        s = input[9].toCharArray();
        for (int i = 0; i < s.length; i++) {
            Character c = s[i];
            if (c.equals('y')){
                StringBuilder build = new StringBuilder(input[9]);
                String sub1 = build.substring(0, i-1);
                String sub2 = build.substring(i+2);
                result[2] = Double.parseDouble(sub1);
                result[3] = Double.parseDouble(sub2);
               
            }
        }
        return result;
    }

    private double[] calcLakeCoords(){            
        double[] result = new double[4];
        char[] s = input[12].toCharArray();
        for (int i = 0; i < s.length; i++) {
            Character c = s[i];
            if (c.equals('x')){
                StringBuilder build = new StringBuilder(input[12]);
                String sub1 = build.substring(0, i-1);
                String sub2 = build.substring(i+2);
                result[0] = Double.parseDouble(sub1);
                result[1] = Double.parseDouble(sub2);
                
            }
        }

        s = input[13].toCharArray();
        for (int i = 0; i < s.length; i++) {
            Character c = s[i];
            if (c.equals('y')){
                StringBuilder build = new StringBuilder(input[13]);
                String sub1 = build.substring(0, i-1);
                String sub2 = build.substring(i+2);
                result[2] = Double.parseDouble(sub1);
                result[3] = Double.parseDouble(sub2);
               
            }
        }
        return result;
    }
    
    
    public double getV0x()           {return Double.parseDouble(input[14]);}
    public double getV0y()           {return Double.parseDouble(input[15]);}
    public Vector2 getInitialVelocity() {
        return new Vector2(Float.parseFloat(input[14]), Float.parseFloat(input[15]));

    }
    /**
     * this method is used when wanting to perform a new shot:
     * it will read again the velocity.txt file and update the
     * initial velocity
     * @param initialX new initial x position
     * @param initialY new initial y position
     * when calling the method, use as parameters current x and current y
     * @throws IOException
     */
    public void newShot(Double initialX, Double initialY) throws IOException{
        //count shots
        shots++;
        //update current x and y in input array
        this.input[1] = initialX.toString(); this.input[2] = initialY.toString();
        //update the new velocity in input array
        read = new BufferedReader(new FileReader(new File(pathVelocity.getFile())));
        for (int i = 12; i < input.length; i++) {
            String s = read.readLine();
            String s1;
            char[] c = s.toCharArray();
            for (int j = 0; j < c.length; j++) {
                
                Character chr = c[j];
                if(chr.equals('=')){
                    StringBuilder build = new StringBuilder(s);
                    s1 = build.substring(j+1);
                    input[i] = s1;
                    break;
                }
            }
        }
        read.close();
    }
    public static void main(String[] args) {
        FileInputManager f = new FileInputManager();
       
        System.out.println(Arrays.toString(f.calcLakeCoords()) );
    }
    
}
