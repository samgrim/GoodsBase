package goodsbase;

import goodsbase.database.DbConnection;
import goodsbase.ui.CategoryTree;
import goodsbase.ui.MainWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.LogManager;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) throws Exception, IOException {
		URL url = Main.class.getResource("logging.properties");
	
		FileInputStream fin = new FileInputStream("logging.properties");
		LogManager.getLogManager().readConfiguration(fin);
		
		
		//LogManager.getLogManager().readConfiguration(
		//		Main.class.getResourceAsStream("/logging.properties"));
	
		CategoryTree.main(args);
		
		/*try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
			DbConnection.init();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(null, "Failed to launch application.\n" 
					+ e.getMessage(), "Launch failed", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		MainWindow.launch();*/
		
	}

}
