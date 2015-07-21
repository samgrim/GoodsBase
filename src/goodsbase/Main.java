package goodsbase;

import goodsbase.database.DbConnection;
import goodsbase.ui.CategoryTree;
import goodsbase.ui.MainWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	private static final String loggingProps = "/logging.properties";
	
	private static final Logger log = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws Exception, IOException {
		initLogger();
		setLookAndFeel();
		
		try {	
			DbConnection.init();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to start application", e);
			JOptionPane.showMessageDialog(null, "Failed to launch application.\n" 
					+ e.getMessage(), "Launch failed", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		MainWindow.launch();		
	}
	
	private static void initLogger(){
		try {
			LogManager.getLogManager().readConfiguration(
					Main.class.getResourceAsStream(loggingProps));
		} catch (SecurityException | IOException e) {
			log.log(Level.WARNING, "Failed reading resource " + loggingProps , e);
		}		
	}
	
	private static void setLookAndFeel() {		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			log.log(Level.WARNING, "Failed setting look and feel" + loggingProps , e);
		}		
	}
	


}
