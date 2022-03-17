package com.mygdx.golf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import com.badlogic.gdx.math.Vector2;


public class FileInputManager {

    protected static String filePathInput = "inputs/input_1.txt";
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
        input = new String[14]; 
        read = new BufferedReader(new FileReader(new File(pathInput.getFile())));

        for (int i = 0; i < 12; i++) {
            
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
        for (int i = 12; i < input.length; i++) {
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
    }
    //ACCESSORS
    public String[] getInputArray() {return this.input;}
    public Vector2 getInitialPos() {
        return new Vector2(Float.parseFloat(input[0]), Float.parseFloat(input[1]));
    }
    public Vector2 getTargetPos() {
        return new Vector2(Float.parseFloat(input[2]), Float.parseFloat(input[3]));
    }
    public float getRadius()        {return Float.parseFloat(input[4]);}
    public float grassKinetic()     {return Float.parseFloat(input[5]);}
    public float grassStatic()      {return Float.parseFloat(input[6]);}
    public String getHeightProfile() {return input[7];}
    public float sandKinetic()      {return Float.parseFloat(input[10]);}
    public float sandStatic()       {return Float.parseFloat(input[11]);}
    
    /**
     * this method translates an input of, e.g., -1<x<1
     * @return arr[0]: beginning; arr[1] end
     */
    public double[] sandPitX(){            
        double[] result = new double[2];
        char[] s = input[8].toCharArray();
        for (int i = 0; i < s.length; i++) {
            Character c = s[i];
            if (c.equals('x')){
                StringBuilder build = new StringBuilder(input[8]);
                String sub1 = build.substring(0, i-1);
                String sub2 = build.substring(i+2);
                result[0] = Double.parseDouble(sub1);
                result[1] = Double.parseDouble(sub2);
                return result;
            }
        }
        return result;
    }
    /**
     * this method translates an input of, e.g., -1<y<1
     * @return arr[0]: beginning; arr[1] end
     */
    public double[] sandPitY(){
        double[] result = new double[2];
        char[] s = input[9].toCharArray();
        for (int i = 0; i < s.length; i++) {
            Character c = s[i];
            if (c.equals('y')){
                StringBuilder build = new StringBuilder(input[9]);
                String sub1 = build.substring(0, i-1);
                String sub2 = build.substring(i+2);
                result[0] = Double.parseDouble(sub1);
                result[1] = Double.parseDouble(sub2);
                return result;
            }
        }
        return result;
    }
    public double getV0x()           {return Double.parseDouble(input[12]);}
    public double getV0y()           {return Double.parseDouble(input[13]);}
    public Vector2 getInitialVelocity() {
        return new Vector2(Float.parseFloat(input[12]), Float.parseFloat(input[13]));

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
        for (String string : f.getInputArray()) {
            System.out.println(string);
        }
        System.out.println(f.getV0x());
    }
    
}
