
package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.xpath.XPathExpressionException;

/**
 * @author Daria
 *
 */
public class ProductTableMenuListener implements ActionListener{

	public ProductTableMenuListener(MainWindow window) {
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			int rowIndex = window.getProductTable().getSelectedRow();
			switch(e.getActionCommand()) {
				default: break;
				case "addProduct":						
					Category c;
					/*if no rows selected in table
					 * get selected category in tree,
					 * else continue with null value*/
					if(rowIndex < 0) {
						DefaultMutableTreeNode node = Actions.getSelectedNode(window.getCatTree());
						c =  ((node.getUserObject() instanceof Category)? 
								(Category)node.getUserObject() 
								: null);
					} else {
						Product p = window.getProductTable().
								getProductAtRowIndex(rowIndex);
						c = (p!=null)? p.getCategory() : null;
					}
					Actions.addProductAction(window, c);			
					break;
				case "removeProduct": {
					if(rowIndex < 0) return;
					Product p = window.getProductTable().
							getProductAtRowIndex(rowIndex);
					Actions.removeProductAction(window, p);
				}
					break;
				case "editProduct": {
					if(rowIndex < 0) return;
					Product p = window.getProductTable().
							getProductAtRowIndex(rowIndex);
					if(p == null) return;
					Actions.editProductAction(window, p);
				}
					break;
				case "viewProductAtWh":
					break;				
			}
		} catch (DataLoadException | XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private MainWindow window;
}
