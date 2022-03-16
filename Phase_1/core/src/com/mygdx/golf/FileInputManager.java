package com.mygdx.golf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;




public class FileInputManager {

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
        this.pathInput = FileInputManager.class.getResource("input.txt");
        this.pathVelocity = FileInputManager.class.getResource("velocity.txt");
        
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
        for (int i = 0; i < input.length; i++) {
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
    //ACCESSORS
    public String[] getInputArray() {return this.input;}
    public double getInitialX()      {return Double.parseDouble(input[0]);}
    public double getInitialY()      {return Double.parseDouble(input[1]);}
    public double getTargetX()       {return Double.parseDouble(input[2]);}
    public double getTargetY()       {return Double.parseDouble(input[3]);}
    public double getRadius()        {return Double.parseDouble(input[4]);}
    public double grassKinetic()     {return Double.parseDouble(input[5]);}
    public double grassStatic()      {return Double.parseDouble(input[6]);}
    public String getHeightProfile() {return input[7];}
    public double sandKinetic()      {return Double.parseDouble(input[10]);}
    public double sandStatic()       {return Double.parseDouble(input[11]);}
    
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
    
}
