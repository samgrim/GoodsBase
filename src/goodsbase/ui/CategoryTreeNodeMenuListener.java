package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

class CategoryTreeNodeMenuListener implements ActionListener {
		
		public CategoryTreeNodeMenuListener(CategoryTree tree) {
			this.tree = tree;			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem menuItem = (JMenuItem)e.getSource();
			DefaultMutableTreeNode node = getSelectedNode(tree);
			Category c =  ((node.getUserObject() instanceof Category)? 
					(Category)node.getUserObject() 
					: null);
			if("addCategory".equals(e.getActionCommand())) {
				addCategoryAction(c);
			} else if ("editCategory".equals(e.getActionCommand())){
				editCategoryAction(c, node);
			} else if ("removeCategory".equals(e.getActionCommand())) {
				removeAction(c, node);
			} else if("addProduct".equals(e.getActionCommand())) {
				addProductAction(c);
			}
		}
		
		private void addProductAction(Category c) {
			EditProductDialog dialog = EditProductDialog.getAddDialog(tree.getMainWindow(), c);
			dialog.setVisible(true);
			Product res = dialog.getResult();
			if(res == null) return;
			String message;
			try {
				if(Product.insert(res)){
					message = "Product succesfully added";
					tree.refreshModel();
				} else {
					message = "Cannot add product" + c;
				}
				JOptionPane.showMessageDialog(tree.getParent(), message, "Add Product", JOptionPane.INFORMATION_MESSAGE);		
			} catch (DataLoadException e) {
				JOptionPane.showMessageDialog(tree.getParent(), "Insertion produced an error. "
						+ "See log file for details", "Error",  JOptionPane.ERROR_MESSAGE);
				log.log(Level.WARNING, "Exception caught when inserting category", e);
			}
		}

		private void addCategoryAction(Category c){
			EditCategoryDialog dialog = null;
			try { //does not throw in insert mode
				dialog = new EditCategoryDialog(tree.getMainWindow(), c, EditCategoryDialog.INSERT_MODE);
			} catch (DataLoadException e1) {}
			dialog.setVisible(true);
			Category res = dialog.getResult();
			if(res == null) return;
			String message;
			try {
				if(Category.insert(res)){
					message = "Category succesfully added";
					tree.refreshModel();
				} else {
					message = "Cannot add category " + c;
				}
				JOptionPane.showMessageDialog(tree.getMainWindow(), message, "Category insert", JOptionPane.INFORMATION_MESSAGE);		
			} catch (DataLoadException e) {
				JOptionPane.showMessageDialog(tree.getMainWindow(), "Insertion produced an error. "
						+ "See log file for details", "Error",  JOptionPane.ERROR_MESSAGE);
				log.log(Level.WARNING, "Exception caught when inserting category", e);
			}
		}
		
		private void editCategoryAction(Category c, DefaultMutableTreeNode node){
			try { 
				EditCategoryDialog dialog = new EditCategoryDialog(tree.getMainWindow(), c, EditCategoryDialog.EDIT_MODE);				
				dialog.setVisible(true);
				Category res = dialog.getResult();
				if(res == null) return;
				String message;
				if(Category.update(res)){
					message = "Category updated";
					node.setUserObject(res);
				} else {
					message = "Cannot update category " + c;
				}
				JOptionPane.showMessageDialog(tree.getMainWindow(), message, "Category update", JOptionPane.INFORMATION_MESSAGE);
				tree.refreshModel();
			} catch (DataLoadException e) {
				JOptionPane.showMessageDialog(tree.getMainWindow(), "Update produced an error. "
						+ "See log file for details", "Error",  JOptionPane.ERROR_MESSAGE);
				log.log(Level.WARNING, "Exception caught when updating category", e);
			}		
		}
		
		private void removeAction(Category c, DefaultMutableTreeNode node) {
			int confirm = JOptionPane.showConfirmDialog(tree.getMainWindow(), 
					"Are you confident in deletion of category " + c + "?", "Delete category", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.YES_OPTION) {
			String message;
			try {
				if(Category.delete(c)) {
					message = "Category deleted";
					tree.getCurrentModel().removeNodeFromParent(node);
				} else {
					message = "Cannot delete category " + c;
				}
				JOptionPane.showMessageDialog(tree.getMainWindow(), message, "Category delete", JOptionPane.INFORMATION_MESSAGE);		
			} catch (DataLoadException e) {
				JOptionPane.showMessageDialog(tree.getMainWindow(), "Deletion produced an error. "
						+ "See log file for details", "Error",  JOptionPane.ERROR_MESSAGE);
				log.log(Level.WARNING, "Exception caught when deleting category", e);
			}
			}
		}
		
		
		private static DefaultMutableTreeNode getSelectedNode(JTree tree) {
			TreePath path = tree.getSelectionPath();
			if(path == null) return null;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
			return node;
		}
		
		private CategoryTree tree;
		
		private static final Logger log = Logger.getLogger(CategoryTreeNodeMenuListener.class.getName());
}
