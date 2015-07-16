package goodsbase.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Toolkit;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JMenuBar;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import java.awt.Dimension;

public class MainWindow {

	private JFrame frmGoodsBase;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
		mntmAbout.setHorizontalTextPosition(SwingConstants.LEFT);
		mntmAbout.setMaximumSize(new Dimension(50, 50));
		mntmAbout.setSize(new Dimension(77, 2));
		mntmAbout.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar.add(mntmAbout);
		
		JSplitPane splitPane = new JSplitPane();
		frmGoodsBase.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JTree catTree = new JTree();
		splitPane.setLeftComponent(catTree);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);
		
		JPanel statusBar = new JPanel();
		frmGoodsBase.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		JToolBar toolBar = new JToolBar();
		frmGoodsBase.getContentPane().add(toolBar, BorderLayout.NORTH);
		frmGoodsBase.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frmGoodsBase.getContentPane()}));
	}

}