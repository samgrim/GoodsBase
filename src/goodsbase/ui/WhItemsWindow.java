package goodsbase.ui;

import goodsbase.Main;
import goodsbase.model.DataLoadException;
import goodsbase.model.Loaders;
import goodsbase.model.Product;
import goodsbase.model.Supply;
import goodsbase.model.Unit;
import goodsbase.model.User;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
import javax.swing.SwingConstants;

/**Application window for warehouse items*/
public class WhItemsWindow {

	/**
	 * Create the application.
	 */
	public WhItemsWindow(Product prod) {
		this.prod = prod;
		initialize();
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @return the prod
	 */
	public Product getProd() {
		return prod;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WhItemsWindow.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setTitle(prod.getCategory() +" - " + prod.getName()
				+ " TM: " + prod.getTradeMark() + " BY: " + prod.getManufacturer());
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
	
		JButton btnRefreshButton = new JButton("Refresh table");
		btnRefreshButton.setActionCommand("refresh");
		btnRefreshButton.addActionListener(lstnr);
		toolBar.add(btnRefreshButton);
		
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
				Main.getCurrentUser().getRole().equals(User.WH_MANAGER)) {
		JButton btnAddGoods = new JButton("Add goods");
		btnAddGoods.setActionCommand("addWhItems");
		btnAddGoods.addActionListener(lstnr);
		toolBar.add(btnAddGoods);
		}
		
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
				Main.getCurrentUser().getRole().equals(User.SAL_MANAGER)) {
		JButton btnWriteOff = new JButton("Write goods off");
		btnWriteOff.setActionCommand("removeWhItems");
		btnWriteOff.addActionListener(lstnr);
		toolBar.add(btnWriteOff);
		}
		
		JButton btnViewHistory = new JButton("View history");
		btnViewHistory.setHorizontalAlignment(SwingConstants.RIGHT);
		btnViewHistory.setActionCommand("viewHistOnSupply");
		btnViewHistory.addActionListener(lstnr);
		toolBar.add(btnViewHistory);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				/*select on click*/
				//TODO: doesn't work fine
				Point point = e.getPoint();
			    final int currentRow = table.rowAtPoint(point);
			    table.getSelectionModel().setSelectionInterval(currentRow, currentRow);
			}
		});
		loadTableModel(table);
		scrollPane.setViewportView(table);
		
		makeTableMenu();
	}
	
	private void loadTableModel(JTable table) {	
		try {
			Object[][] data = Loaders.getWhItemsOn(prod);		
			DefaultTableModel model = new DefaultTableModel(){
				private static final long serialVersionUID = 1L;
				@Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
			};
			model.setDataVector(data, tableCols);		
			table.setModel(model);
		} catch (XPathExpressionException | DataLoadException e) {
			JOptionPane.showMessageDialog(frame, "Failed to load items", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void makeTableMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item;
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
				Main.getCurrentUser().getRole().equals(User.WH_MANAGER)) {
			item = new JMenuItem("Add supply");
			item.setActionCommand("addWhItems");
			item.addActionListener(lstnr);
			menu.add(item);
		}
		
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
				Main.getCurrentUser().getRole().equals(User.SAL_MANAGER)) {
			item = new JMenuItem("Write off");
			item.setActionCommand("removeWhItems");
			item.addActionListener(lstnr);
			menu.add(item);
		}
		
		item = new JMenuItem("View supply history");
		item.setActionCommand("viewHistOnSupply");
		item.addActionListener(lstnr);
		menu.add(item);
		table.setComponentPopupMenu(menu);
	}
	
	private Supply getSelectedSupplyToWriteoff(){
		int row  = table.getSelectedRow();
		if (row < 0) return null;
		double quantity = Double.valueOf((String)table.getValueAt(row, 0));
		Unit unit = Unit.valueOf((String)table.getValueAt(row, 1));
		double price = Double.valueOf((String)table.getValueAt(row, 2));
		return new Supply(prod, quantity, unit, price, Supply.Type.WRITEOFF);
	}
	
	private class WhItemListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
				case "refresh":
					loadTableModel(table);
					break;
				case "addWhItems":
					JDialog dialog = new AddSupplyDialog(prod, frame);
					dialog.setVisible(true);
					//refresh
					loadTableModel(table);
					break;
				case "removeWhItems":
					Supply s= getSelectedSupplyToWriteoff();
					if(s!=null){
						JDialog dialog1 = new RemoveSupplyDialog(s);
						dialog1.setVisible(true);
						//refresh
						loadTableModel(table);
					} else {
						JOptionPane.showMessageDialog(frame, "Select a row to write products off");
					}
					break;
				case "viewHistOnSupply":
					JFrame fr = new SuppliesFrame(prod);
					fr.setVisible(true);
				break;
			}
		}
	}
	
	
	private JFrame frame;
	private JTable table;
	private Product prod;
	private ActionListener lstnr = new WhItemListener();

	private static final String[] tableCols = {"Quantity", "Units", "Price", "Total"};
}
