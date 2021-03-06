package goodsbase.ui;

import goodsbase.Main;
import goodsbase.model.DataLoadException;
import goodsbase.model.User;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class MainWindow implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("stats")){
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						StatsViewerFrame frame = new StatsViewerFrame();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}

	/**
	 * Launch the application.
	 */
	public static void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmGoodsBase.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public MainWindow() throws SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frmGoodsBase = new JFrame();
		frmGoodsBase.setTitle("Goods Base");
		frmGoodsBase.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		frmGoodsBase.setBounds(100, 100, 800, 600);
		frmGoodsBase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGoodsBase.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
               Main.exit();
            }
        });
		
		JMenuBar menuBar = new JMenuBar();
		frmGoodsBase.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmStats = new JMenuItem("Statistics");
		mntmStats.setActionCommand("stats");
		mntmStats.addActionListener(this);
		mnFile.add(mntmStats);
		
		
		if(Main.getCurrentUser().getRole().equals(User.ADMIN)) {
			
			ActionListener lsn = new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					AuthDialog dlg = new AuthDialog(e.getActionCommand());
					String message;
					dlg.setVisible(true);
					User result = dlg.getResult();
					try {
						if(result != null && User.createUser(result)) {
							message ="User created";
						} else {
							message = "Failed to create user";
						}
					} catch (DataLoadException e1) {
						message = "Failed to create user";
					}
					JOptionPane.showMessageDialog(frmGoodsBase, message);
				}
				
			};
			JMenu mnAccounts = new JMenu("Accounts");
			menuBar.add(mnAccounts);
			
			JMenuItem mntmCreateAdminAccount = new JMenuItem("Create admin account");
			mntmCreateAdminAccount.setActionCommand(User.ADMIN);
			mntmCreateAdminAccount.addActionListener(lsn);
			mnAccounts.add(mntmCreateAdminAccount);
			
			JMenuItem mntmCreateWhmanagerAccount = new JMenuItem("Create wh-manager account");
			mntmCreateWhmanagerAccount.setActionCommand(User.WH_MANAGER);
			mntmCreateWhmanagerAccount.addActionListener(lsn);
			mnAccounts.add(mntmCreateWhmanagerAccount);
			
			JMenuItem mntmCreateSalesmanagerAccount = new JMenuItem("Create sales-manager account");
			mntmCreateSalesmanagerAccount.setActionCommand(User.SAL_MANAGER);
			mntmCreateSalesmanagerAccount.addActionListener(lsn);
			mnAccounts.add(mntmCreateSalesmanagerAccount);
			
		
		}
			
		/*same listener for tree popup*/
		ActionListener actListener = new ProductTableMenuListener(this);
		searchField = new JTextField();
		searchField.setToolTipText("Search");
		menuBar.add(searchField);
		searchField.setColumns(20);
		searchField.addActionListener(actListener);
		searchField.setActionCommand("search");
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.15);
		frmGoodsBase.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		/*product table*/
		productTable = new ProductTable();
		productTable.addMouseListener( new ProductTableMouseAdapter(this));
	
		/*product table popup menu*/
		
		JPopupMenu tablePopupMenu = new JPopupMenu();	
		JMenuItem item = new JMenuItem("View at warehouse (double click)");
		item.setActionCommand("viewProductAtWh");
		item.addActionListener(actListener);
		tablePopupMenu.add(item);
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) || 
				Main.getCurrentUser().getRole().equals(User.WH_MANAGER)) {			
			item = new JMenuItem("Add Product");
			item.setActionCommand("addProduct");
			item.addActionListener(actListener);
			tablePopupMenu.add(item);
			item = new JMenuItem("Edit Product");
			item.setActionCommand("editProduct");
			item.addActionListener(actListener);
			tablePopupMenu.add(item);
			item = new JMenuItem("Remove Product");
			item.setActionCommand("removeProduct");
			item.addActionListener(actListener);
			tablePopupMenu.add(item);			
		}
		productTable.setComponentPopupMenu(tablePopupMenu);
		
		catTree = new CategoryTree();		
		catTree.addMouseListener(new CategoryTreeMouseAdapter(this));
	
		JScrollPane catScrollPane = new JScrollPane(catTree);
		splitPane.setLeftComponent(catScrollPane);
		
		JScrollPane prodScrollPane = new JScrollPane(productTable);
		splitPane.setRightComponent(prodScrollPane);
		
		JPanel statusBar = new JPanel();
		frmGoodsBase.getContentPane().add(statusBar, BorderLayout.SOUTH);
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		
		JLabel lblLoggedInAs = new JLabel("Logged in as: ");
		statusBar.add(lblLoggedInAs);
		
		JLabel lblLogin = new JLabel(Main.getCurrentUser().getUsername());
		statusBar.add(lblLogin);
	}
	

	public JFrame getFrmGoodsBase() {
		return frmGoodsBase;
	}

	public ProductTable getProductTable() {
		return productTable;
	}

	public CategoryTree getCatTree() {
		return catTree;
	}


	/**
	 * @return the searchField
	 */
	public JTextField getSearchField() {
		return searchField;
	}


	private JFrame frmGoodsBase;
	private ProductTable productTable;
	private CategoryTree catTree;
	private JTextField searchField;

}
