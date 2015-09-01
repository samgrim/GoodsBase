
package goodsbase.ui;

import goodsbase.model.DataLoadException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
					Actions.addProductAction(window);			
					break;
				case "removeProduct":
					break;
				case "editProduct":
					Actions.editProductAction(window);
					break;
				case "viewProductAtWh":
					break;				
			}
		} catch (DataLoadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private MainWindow window;
}
