package goodsbase.ui;

import goodsbase.model.DataLoadException;
import goodsbase.model.Loaders;
import goodsbase.model.Product;
import goodsbase.model.Supply;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.xml.xpath.XPathExpressionException;

public class SuppliesFrame extends JFrame {


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SuppliesFrame frame = new SuppliesFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public SuppliesFrame(Product prod) {
		this();
		this.setTitle(prod.getCategory() +" - " + prod.getName()
				+ " TM: " + prod.getTradeMark() + " BY: " + prod.getManufacturer());
		loadTableModel(prod);
	}
	
/*	public SuppliesFrame(Supply supply) {
		this();
		this.setTitle(supply.getProduct().getCategory() +" - " + supply.getProduct().getName()
				+ " TM: " + supply.getProduct().getTradeMark() + " BY: " + supply.getProduct().getManufacturer());
	}*/
	
	private void loadTableModel(Product p) {	
		try {
			Object[][] data = Loaders.getSuppliesOn(p);		
			DefaultTableModel model = new DefaultTableModel(){
				private static final long serialVersionUID = 1L;
				@Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }	
			};
			model.setDataVector(data, columns);		
			table.setModel(model);
		} catch (XPathExpressionException | DataLoadException e) {
			JOptionPane.showMessageDialog(this, "Failed to load items", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Create the frame.
	 */
	private SuppliesFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(SuppliesFrame.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
	}

	private static final String[] columns = {"Date", "Type", "Price", "Units", "Quantity", "Total"};

	/**
	 * 
	 */
	private static final long serialVersionUID = -1847456316723790695L;
	private JPanel contentPane;
	private JTable table;
}
