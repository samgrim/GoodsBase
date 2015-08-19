package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

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
			if(menuItem == tree.getPopupMenu().getAddCategoryMenuItem()) {
				addAction(c);
			} else if (menuItem == tree.getPopupMenu().getEditCategoryMenuItem()){
				editAction(c);
			} else if (menuItem == tree.getPopupMenu().getRemoveCategoryMenuItem()) {
				if(c!=null) {
					removeAction(c, node);
				}
			}
		}
		
		private void addAction(Category c){
			AddCategoryDialog dialog = new AddCategoryDialog(tree.getMainWindow(), c);
			dialog.setVisible(true);
			Category res = dialog.getResult();
			String message;
			try {
				if(Category.insert(res)){
					message = "Category inserted";
					tree.refreshModel();
				} else {
					message = "Cannot insert category " + c;
				}
				JOptionPane.showMessageDialog(tree.getParent(), message, "Category insert", JOptionPane.INFORMATION_MESSAGE);		
			} catch (DataLoadException e) {
				JOptionPane.showMessageDialog(tree.getParent(), "Insertion produced an error. "
						+ "See log file for details", "Error",  JOptionPane.ERROR_MESSAGE);
				log.log(Level.WARNING, "Exception caught when inserting category", e);
			}
		}
		
		private void editAction(Category c){
			JOptionPane.showMessageDialog(null, c.getName(), "Edit menu action", JOptionPane.INFORMATION_MESSAGE);			
		}
		
		private void removeAction(Category c, DefaultMutableTreeNode node) {
			int confirm = JOptionPane.showConfirmDialog(tree.getParent(), 
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
				JOptionPane.showMessageDialog(tree.getParent(), message, "Category delete", JOptionPane.INFORMATION_MESSAGE);		
			} catch (DataLoadException e) {
				JOptionPane.showMessageDialog(tree.getParent(), "Deletion produced an error. "
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
