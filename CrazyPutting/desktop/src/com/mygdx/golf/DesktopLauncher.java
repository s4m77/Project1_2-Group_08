package com.mygdx.golf;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	
	//main method that starts our gameg
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(60);
		config.useVsync(true);
		config.setTitle("Golf");
		config.setResizable(false);
		config.setWindowedMode(960, 640);

		new Lwjgl3Application(new Boot(), config);
	}
}
