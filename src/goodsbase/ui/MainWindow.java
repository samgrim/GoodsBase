package goodsbase.ui;

import goodsbase.qserver.QServer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
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
                try {
					QServer.stop();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setHorizontalAlignment(SwingConstants.TRAILING);
		mntmAbout.setHorizontalTextPosition(SwingConstants.LEFT);
		mntmAbout.setSize(new Dimension(77, 2));
		menuBar.add(mntmAbout);
		
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
		JMenuItem item = new JMenuItem("Add Product");
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
		item = new JMenuItem("View at warehouse (double click)");
		item.setActionCommand("viewProductAtWh");
		item.addActionListener(actListener);
		tablePopupMenu.add(item);
		productTable.setComponentPopupMenu(tablePopupMenu);
		
		catTree = new CategoryTree();		
		catTree.addMouseListener(new CategoryTreeMouseAdapter(this));
	
		JScrollPane catScrollPane = new JScrollPane(catTree);
		splitPane.setLeftComponent(catScrollPane);
		
		JScrollPane prodScrollPane = new JScrollPane(productTable);
		splitPane.setRightComponent(prodScrollPane);
		
		JPanel statusBar = new JPanel();
		frmGoodsBase.getContentPane().add(statusBar, BorderLayout.SOUTH);
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
