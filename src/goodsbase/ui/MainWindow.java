package goodsbase.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JTable;
import javax.swing.JScrollPane;

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
		
		productTable = new JTable();
		productTable.setAutoCreateRowSorter(true);
		
		catTree = new CategoryTree(frmGoodsBase);
		
		catTree.addMouseListener(new CategoryTreeMouseAdapter(catTree, productTable));
		catTree.getPopupMenu()
			.addMenuListenerToAllItems(new CategoryTreeNodeMenuListener(catTree));
		
		JScrollPane catScrollPane = new JScrollPane(catTree);
		splitPane.setLeftComponent(catScrollPane);
		
		JScrollPane prodScrollPane = new JScrollPane(productTable);
		splitPane.setRightComponent(prodScrollPane);
		
	
		
//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		splitPane.setRightComponent(tabbedPane);
		
		JPanel statusBar = new JPanel();
		frmGoodsBase.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
//		JToolBar toolBar = new JToolBar();
//		frmGoodsBase.getContentPane().add(toolBar, BorderLayout.NORTH);
//		frmGoodsBase.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frmGoodsBase.getContentPane()}));
	}
	

	private JFrame frmGoodsBase;
	private JTable productTable;
	private CategoryTree catTree;

}
