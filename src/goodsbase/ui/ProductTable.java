package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Loaders;
import goodsbase.model.Product;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;

/**Table for displaying products
 * @author Daria
 *
 */
public class ProductTable extends JTable {

	/**Creates a new product table*/ 
	public ProductTable(){
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoCreateRowSorter(true);
	}

	
	@Override
	public String getToolTipText(MouseEvent event) {
		java.awt.Point p = event.getPoint();
        int rowIndex = rowAtPoint(p);
        if(rowIndex!=-1 && productColomnIndex>=0) {
        	return getProductAtRowIndex(rowIndex)
        				.getDescription();
        }
        return "";
	}

	/**Loads products from database
	 * @param cat - if null all products will be loaded, otherwise
	 * only for the specified category
	 * @throws DataLoadException 
	 * @throws XPathExpressionException */
	public void loadItems(Category cat) throws XPathExpressionException, DataLoadException {
		String[] colomns;
		/*cells NOT editable*/
		DefaultTableModel tableModel = new DefaultTableModel(){				
			@Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }		
			private static final long serialVersionUID = 2901615843314533198L;
		};
		
		if(cat!=null) {
			colomns = new String[] {"Name", "Trade Mark", "Manufacturer", "Available at warehouse"};
			productColomnIndex = 0;
			tableModel.setDataVector(Loaders.getProductsAsArray(cat), colomns);
		} else {
			colomns = new String[] {"Category", "Name", "Trade Mark", "Manufacturer", "Available at warehouse"};
			productColomnIndex = 1;
			tableModel.setDataVector(Loaders.getProductsAsArray(), colomns);
		}
		this.setModel(tableModel);
	}
	
	/**@return product of the selected row of table,
	 * can return null value if no product presents in the specified row*/
	public Product getProductAtRowIndex(int rowIndex) {
		if(productColomnIndex < 0) 
			return null;
		else{
			return (Product)getValueAt(rowIndex, productColomnIndex);
		}
	}
	
	public void updateRow(int rowIndex, Product result) {
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		String available;
		Object[] row;
		switch(productColomnIndex) {
		case 0:
			row = new Object[4];
			available = (String) model.getValueAt(rowIndex, 3);
			row[0] = result;
			row[1] = result.getManufacturer();
			row[2] = result.getTradeMark();
			row[3] = available;
			break;
		case 1:
			row = new Object[5];
			available = (String) model.getValueAt(rowIndex, 4);
			row[0] = result.getCategory();
			row[1] = result;
			row[2] = result.getManufacturer();
			row[3] = result.getTradeMark();
			row[4] = available;
			break;
		default:
			return;
		}
		model.removeRow(rowIndex);
		model.addRow(row);
	}
	
	public void removeSelectedRow(){
		int row = this.getSelectedRow();
		if(row <0) return;
		((DefaultTableModel)this.getModel()).removeRow(row);
	}
	/**
	 * 
	 */
	private int productColomnIndex = -1;
	private static final long serialVersionUID = 8465178028674334977L;
	
}
