package goodsbase.ui;

import goodsbase.model.DataLoadException;
import goodsbase.model.User;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.xpath.XPathExpressionException;

public class AuthDialog extends JDialog implements ActionListener{

	
	private final JPanel contentPanel = new JPanel();
	private JTextField usernameField;
	private JPasswordField passwordField;

	/**
	 * Create the dialog.
	 * @param mode - one of AuthDialog.NEW_ADMIN_USER, AuthDialog.NEW_WH_MAN_USER, 
	 * AuthDialog.NEW_SAL_MAN_USER, AuthDialog.LOGIN
	 */
	public AuthDialog(String mode) {
		this.role = mode;
		setIconImage(Toolkit.getDefaultToolkit().getImage(AuthDialog.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		String title = "GoodsBase  ";
		switch(mode) {
			case LOGIN:
				title +="Log in";
				break;
			case NEW_ADMIN_USER:
				title +="Create admin user";
				break;
			case NEW_SAL_MAN_USER:
				title +="Create sales manager user";
				break;	
			case NEW_WH_MAN_USER:
				title +="Create warehouse manager user";
				break;	
		}
		setTitle(title);
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblUserName = new JLabel("User Name:");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		passwordField = new JPasswordField();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(38)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUserName)
						.addComponent(lblPassword))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(usernameField)
						.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
					.addContainerGap(79, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(35)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserName)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(111, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OK")) {
			String username = usernameField.getText();
			char[] pass = passwordField.getPassword();
			if(role.equals(LOGIN)) {			
				try {
					result = User.getUser(username, pass);
				} catch (NoSuchAlgorithmException | XPathExpressionException
						| DataLoadException e1) {
					log.log(Level.WARNING, "Failed to authenticate user", e1);
					JOptionPane.showMessageDialog(this, "Exception occurred. Try again");
				}
			} else {
				try {
					result = new User(username, pass, role);
				} catch (NoSuchAlgorithmException e1) {
					log.log(Level.WARNING, "Failed to create user", e1);		
				}
			}
		} 
		this.dispose();
	}
	
	/**
	 * @return the result
	 */
	public User getResult() {
		return result;
	}

	public static final String NEW_ADMIN_USER = "admin";
	public static final String NEW_WH_MAN_USER = "whmanager";
	public static final String NEW_SAL_MAN_USER = "salesmanager";
	public static final String LOGIN = "login";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1913558693054614548L;
	private static final Logger log = Logger.getLogger(AuthDialog.class.getName());
	
	private String role;
	private User result;
	
}
