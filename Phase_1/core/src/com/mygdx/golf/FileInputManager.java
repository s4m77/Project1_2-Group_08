package com.mygdx.golf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class FileInputManager {

    String file;
    /**
     * array storing the input.txt file values
     * stores, in order: [0]:x0, [1]:y0, [2]:xt, [3]:yt, [4]:r, [5]:muk, [6]:mus
     * [7]:heightProfile, [8]:sandPitX, [9]:sandPitY, [10]:muks, [11]:muss
     */
    String[] input;
    public FileInputManager(String file){
        this.file = file;

        try {readFile(file);} 
        catch (IOException e) {e.printStackTrace();}

    }

    public void readFile(String file) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(file));
        for (int i = 0; i < input.length; i++) {
            String s = read.readLine();
            
            input[i] = ;
        }
        read.close();
    }

    public double getInitialX()      {return Double.parseDouble(input[0]);}
    public double getInitialY()      {return Double.parseDouble(input[1]);}
    public double getXt()            {return Double.parseDouble(input[2]);}
    public double getYt()            {return Double.parseDouble(input[3]);}
    public double getRadius()        {return Double.parseDouble(input[4]);}
    public double grassKinetic()     {return Double.parseDouble(input[5]);}
    public double grassStatic()      {return Double.parseDouble(input[6]);}
    public String getHeightProfile() {return input[7];}
    // public double[] sandPitX(){

    // }
    // public double sandPitY(){

    // }
    public double sandKinetic()      {return Double.parseDouble(input[10]);}
    public double sandStatic()       {return Double.parseDouble(input[11]);}

}
