package goodsbase.ui;

import goodsbase.model.Product;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class MainWindow {


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
		
		JMenuBar menuBar = new JMenuBar();
		frmGoodsBase.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.setHorizontalAlignment(SwingConstants.TRAILING);
		mntmAbout.setHorizontalTextPosition(SwingConstants.LEFT);
		mntmAbout.setSize(new Dimension(77, 2));
		menuBar.add(mntmAbout);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.15);
		frmGoodsBase.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		/*product table*/
		productTable = new JTable(){
			@Override
			public String getToolTipText(MouseEvent event) {
				java.awt.Point p = event.getPoint();
                int rowIndex = rowAtPoint(p);
                if(rowIndex!=-1) {
                	return ((Product)getValueAt(rowIndex, 0))
                				.getDescription();
                }
                return "";
			}
		};
		productTable.setAutoCreateRowSorter(true);
		productTable.addMouseListener( new ProductTableMouseAdapter(this));
		productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		/*product table popup menu*/
		JPopupMenu tablePopupMenu = new JPopupMenu();		
		JMenuItem item = new JMenuItem("Add Product");
		item.setActionCommand("addProduct");
		tablePopupMenu.add(item);
		item = new JMenuItem("Edit Product");
		item.setActionCommand("editProduct");
		tablePopupMenu.add(item);
		item = new JMenuItem("Remove Product");
		item.setActionCommand("removeProduct");
		tablePopupMenu.add(item);
		item = new JMenuItem("View at warehouse (double click)");
		item.setActionCommand("viewProductAtWh");
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

	public JTable getProductTable() {
		return productTable;
	}

	public CategoryTree getCatTree() {
		return catTree;
	}


	private JFrame frmGoodsBase;
	private JTable productTable;
	private CategoryTree catTree;

}
