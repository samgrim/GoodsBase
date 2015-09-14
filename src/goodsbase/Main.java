package goodsbase;

import goodsbase.model.DataLoadException;
import goodsbase.model.User;
import goodsbase.qserver.QServer;
import goodsbase.ui.AuthDialog;
import goodsbase.ui.MainWindow;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	private static final String loggingProps = "/logging.properties";
	
	private static final Logger log = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws InterruptedException {
		initLogger();
		setLookAndFeel();
		
		try {
			if(!QServer.isAlive())
				QServer.start();
			if(User.getUsersCount() == 0) {
				if(createFirstUser())
					JOptionPane.showMessageDialog(null, "User created");
				else
					JOptionPane.showMessageDialog(null, "Failed to create user");
			} 
			authUser();
			if(currentUser == null)	{
				JOptionPane.showMessageDialog(null, "Invalid credentials, try again.");
				exit();
			}
			MainWindow.launch();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed to start application");
			log.log(Level.SEVERE, "Failed to start application", e);
			exit();
		}
				
	}
	
	public static void exit() {
		try {
			QServer.stop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(1);		
	}

	/**
	 * @return the currentUser
	 */
	public static User getCurrentUser() {
		return currentUser;
	}

	private static void authUser() {
		AuthDialog dialog = new AuthDialog(AuthDialog.LOGIN);
		dialog.setVisible(true);
		currentUser = dialog.getResult();
	}

	private static boolean createFirstUser() throws DataLoadException {		
		AuthDialog dialog = new AuthDialog(User.ADMIN);
		dialog.setVisible(true);
		User result = dialog.getResult();
		if(result== null) 
			return false;
		return User.createUser(result);
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
	
	private static User currentUser;
}
