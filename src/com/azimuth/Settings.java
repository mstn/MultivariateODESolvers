package com.azimuth;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class contains the global settings for this packages
 * @author marco
 *
 */
public class Settings {
	
	private final static String DEFAULT_PROPERTIES_FILE = "./settings.properties";
	private final static String GNUPLOT_COMMAND_PROPERTY = "gnuplot.command";
	
	
	private static Settings instance;
	private Properties properties;
	
	private Logger logger = Logger.getLogger(Settings.class.getSimpleName());
	
	
	private Settings(){
		this(DEFAULT_PROPERTIES_FILE);
	}
	
	private Settings(String filename){
		// load properties from files
		properties = new Properties();
		try {
			properties.load(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			logger.severe("Settings file not found.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Settings getInstance(){
		if (instance == null){
			instance = new Settings();
		}
		return instance;
	}
	
	public static void load(String filename){
		instance = new Settings(filename);
	}

	
	public String getGnuplotCommandName(){
		return properties.getProperty(GNUPLOT_COMMAND_PROPERTY);
	}
	
}
