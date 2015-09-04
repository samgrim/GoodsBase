package goodsbase.ui;

import goodsbase.model.DataLoadException;
import goodsbase.model.Loaders;
import goodsbase.model.Product;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;

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
		
		ActionListener lstnr = new WhItemListener();
		JButton btnRefreshButton = new JButton("Refresh table");
		btnRefreshButton.setActionCommand("refresh");
		btnRefreshButton.addActionListener(lstnr);
		toolBar.add(btnRefreshButton);
		
		JButton btnAddGoods = new JButton("Add goods");
		btnAddGoods.setActionCommand("addWhItems");
		btnAddGoods.addActionListener(lstnr);
		toolBar.add(btnAddGoods);
		
		JButton btnWriteOff = new JButton("Write goods off");
		btnWriteOff.setActionCommand("removeWhItems");
		btnWriteOff.addActionListener(lstnr);
		toolBar.add(btnWriteOff);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);
		loadTableModel(table);
		scrollPane.setViewportView(table);
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
	
	private JFrame frame;
	private JTable table;
	private Product prod;

	private static final String[] tableCols = {"Quantity", "Units", "Price", "Total"};
	
	private class WhItemListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
				case "refresh":
					loadTableModel(table);
					break;
				case "addWhItems":
					break;
				case "removeWhItems":
					break;
			}
		}
	}
}
