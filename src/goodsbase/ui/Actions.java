package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Product;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * @author Daria
 *
 */
class Actions {	
	
	public static void addProductAction(MainWindow window) throws DataLoadException {
		DefaultMutableTreeNode node = getSelectedNode(window.getCatTree());
		Category c =  ((node.getUserObject() instanceof Category)? 
				(Category)node.getUserObject() 
				: null);
		EditProductDialog dialog = EditProductDialog.getAddDialog(window.getFrmGoodsBase(), c);
		dialog.setVisible(true);
		Product res = dialog.getResult();
		if(res == null) return;
		String message;
	
		if(Product.insert(res)){
			message = "Product succesfully added";
			window.getCatTree().refreshModel();
		} else {
			message = "Cannot add product" + c;
		}
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message, "Add Product", JOptionPane.INFORMATION_MESSAGE);		
	}

	public static void addCategoryAction(MainWindow window) throws DataLoadException{
		DefaultMutableTreeNode node = getSelectedNode(window.getCatTree());
		Category c =  ((node.getUserObject() instanceof Category)? 
				(Category)node.getUserObject() 
				: null);
		EditCategoryDialog dialog = new EditCategoryDialog(window.getFrmGoodsBase(), c, EditCategoryDialog.INSERT_MODE);
		dialog.setVisible(true);
		Category res = dialog.getResult();
		if(res == null) return;
		String message;
	
		if(Category.insert(res)){
			message = "Category succesfully added";
			window.getCatTree().refreshModel();
		} else {
			message = "Cannot add category " + c;
		}
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message, "Category insert", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	public static void editCategoryAction(MainWindow window) throws DataLoadException{
		DefaultMutableTreeNode node = getSelectedNode(window.getCatTree());
		Category c =  ((node.getUserObject() instanceof Category)? 
				(Category)node.getUserObject() 
				: null);
		EditCategoryDialog dialog = new EditCategoryDialog(window.getFrmGoodsBase(), c, EditCategoryDialog.EDIT_MODE);				
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
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message, "Category update", JOptionPane.INFORMATION_MESSAGE);
		window.getCatTree().refreshModel();	
	}
	
	public static void removeCategoryAction(MainWindow window) throws DataLoadException {
		DefaultMutableTreeNode node = getSelectedNode(window.getCatTree());
		Category c =  ((node.getUserObject() instanceof Category)? 
				(Category)node.getUserObject() 
				: null);
		int confirm = JOptionPane.showConfirmDialog(window.getFrmGoodsBase(), 
				"Are you confident in deletion of category " + c + "?", "Delete category", JOptionPane.YES_NO_OPTION);
		if(confirm == JOptionPane.YES_OPTION) {
			String message;
		
			if(Category.delete(c)) {
				message = "Category deleted";
				window.getCatTree().getCurrentModel().removeNodeFromParent(node);
			} else {
				message = "Cannot delete category " + c;
			}
			JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message, "Category delete", JOptionPane.INFORMATION_MESSAGE);			
		}
	}
	
	
	private static DefaultMutableTreeNode getSelectedNode(JTree tree) {
		TreePath path = tree.getSelectionPath();
		if (path == null)
			return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		return node;
	}
}
