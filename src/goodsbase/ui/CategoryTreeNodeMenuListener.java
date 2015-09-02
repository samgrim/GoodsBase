package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

class CategoryTreeNodeMenuListener implements ActionListener {
				
		public CategoryTreeNodeMenuListener(MainWindow window) {
			this.window = window;			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			DefaultMutableTreeNode node = Actions.getSelectedNode(window.getCatTree());
			Category c =  ((node.getUserObject() instanceof Category)? 
					(Category)node.getUserObject() 
					: null);
			try {
				if("addCategory".equals(e.getActionCommand())) {		
					Actions.addCategoryAction(window, c);
				} else if ("editCategory".equals(e.getActionCommand())){
					Actions.editCategoryAction(window, c);
				} else if ("removeCategory".equals(e.getActionCommand())) {
					Actions.removeCategoryAction(window, c, node);
				} else if("addProduct".equals(e.getActionCommand())) {
					Actions.addProductAction(window, c);
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
