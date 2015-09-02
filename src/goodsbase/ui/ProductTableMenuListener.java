
package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

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
			switch(e.getActionCommand()) {
				default: break;
				case "addProduct":	
					DefaultMutableTreeNode node = Actions.getSelectedNode(window.getCatTree());
					Category c =  ((node.getUserObject() instanceof Category)? 
							(Category)node.getUserObject() 
							: null);
					Actions.addProductAction(window, c);			
					break;
				case "removeProduct":
					break;
				case "editProduct":
					Actions.editProductAction(window);
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
