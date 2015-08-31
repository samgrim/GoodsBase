package goodsbase.ui;

import goodsbase.model.DataLoadException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

class CategoryTreeNodeMenuListener implements ActionListener {
				
		public CategoryTreeNodeMenuListener(MainWindow window) {
			this.window = window;			
		}

		@Override
		public void actionPerformed(ActionEvent e) {				
			try {
				if("addCategory".equals(e.getActionCommand())) {		
					Actions.addCategoryAction(window);
				} else if ("editCategory".equals(e.getActionCommand())){
					Actions.editCategoryAction(window);
				} else if ("removeCategory".equals(e.getActionCommand())) {
					Actions.removeCategoryAction(window);
				} else if("addProduct".equals(e.getActionCommand())) {
					Actions.addProductAction(window);
				}
			} catch (DataLoadException e1) {
				JOptionPane.showMessageDialog(window.getFrmGoodsBase(), 
						ERROR_STRING,"Error message", 
						JOptionPane.ERROR_MESSAGE);
				log.log(Level.WARNING, "Add category action failed with exceptions", e1);
			}			
		}

		private final MainWindow window;
		private static final String ERROR_STRING = "Errors occurred during action. See log files for details.";
		private static final Logger log = Logger.getLogger(CategoryTreeNodeMenuListener.class.getName());
}
