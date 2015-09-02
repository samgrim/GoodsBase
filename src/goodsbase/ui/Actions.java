package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Product;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;

/**Contains methods to perform various menu actions
 * @author Daria
 * 
 */
class Actions {
	/**Adds a product to database
	 * @param window - parent window for dialogs
	 * @param selected  - the category will be initially selected at dialog window
	 * @throws DataLoadException if database problems occur
	 * @throws XPathExpressionException */
	public static void addProductAction(MainWindow window, Category selected)
			throws DataLoadException, XPathExpressionException {
		EditProductDialog dialog = EditProductDialog.getAddDialog(
				window.getFrmGoodsBase(), selected);
		dialog.setVisible(true);
		Product res = dialog.getResult();
		if (res == null)
			return;
		String message;
		if (Product.insert(res)) {
			message = "Product succesfully added";
		} else {
			message = "Cannot add product" + selected;
		}
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message,
				"Add Product", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/**Edits product and writes it to database
	 * @param window - parent window for dialogs
	 * @throws DataLoadException if database problems occur
	 * @throws XPathExpressionException 
	 */
	public static void editProductAction(MainWindow window)
			throws DataLoadException, XPathExpressionException {
		JTable table = window.getProductTable();
		int row = table.getSelectedRow();
		if (row < 0)
			return;
		/* говнокоооод */
		Product prod;
		if (table.getColumnCount() == 4) {
			prod = (Product) table.getValueAt(row, 0);
		} else if (table.getColumnCount() == 5) {
			prod = (Product) table.getValueAt(row, 1);
		} else
			return;

		EditProductDialog dialog = EditProductDialog.getEditDialog(
				window.getFrmGoodsBase(), prod);
		dialog.setVisible(true);
		Product res = dialog.getResult();
		if (res == null)
			return;
		String message;
		if (Product.update(res)) {
			message = "Product succesfully changed";
			window.getCatTree().refreshModel();
		} else {
			message = "Cannot modify product" + prod;
		}
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message,
				"Edit Product", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**Adds a category to database and updates view
	 * @param window - parent window for dialogs
	 * @param selected  - the category will be initially selected 
	 * as parent category at dialog window
	 * @throws DataLoadException if database problems occur
	 * @throws XPathExpressionException 
	 */
	public static void addCategoryAction(MainWindow window, Category selected)
			throws DataLoadException, XPathExpressionException {
		EditCategoryDialog dialog = new EditCategoryDialog(
				window.getFrmGoodsBase(), selected,
				EditCategoryDialog.INSERT_MODE);
		dialog.setVisible(true);
		Category res = dialog.getResult();
		if (res == null)
			return;
		String message;

		if (Category.insert(res)) {
			message = "Category succesfully added";
			window.getCatTree().refreshModel();
		} else {
			message = "Cannot add category " + selected;
		}
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message,
				"Category insert", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**Edits category, writes it to database and updates view
	 * @param window - parent window for dialogs
	 * @param selected  - the category to edit
	 * @throws DataLoadException if database problems occur
	 * @throws XPathExpressionException 
	 */
	public static void editCategoryAction(MainWindow window, 
							Category selected) throws DataLoadException, XPathExpressionException {

		EditCategoryDialog dialog = new EditCategoryDialog(
				window.getFrmGoodsBase(), selected,
				EditCategoryDialog.EDIT_MODE);
		dialog.setVisible(true);
		Category res = dialog.getResult();
		if (res == null)
			return;
		String message;
		if (Category.update(res)) {
			message = "Category updated";
			/*can be updated by setting another object
			 * to the node, but at frame it looks like catN...
			 * instead of catName*/
			window.getCatTree().refreshModel();
		} else {
			message = "Cannot update category " + selected;
		}
		JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message,
				"Category update", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	/**Removes category from database and updates view
	 * @param window - parent window for dialogs
	 * @param selected  - the category to edit
	 * @param toRemove - node to be removed after the deletion performed
	 * @throws DataLoadException if database problems occur
	 */
	public static void removeCategoryAction(MainWindow window,
			Category selected, DefaultMutableTreeNode toRemove)
			throws DataLoadException {
		int confirm = JOptionPane.showConfirmDialog(window.getFrmGoodsBase(),
				"Are you confident in deletion of category " + selected + "?",
				"Delete category", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			String message;

			if (Category.delete(selected)) {
				message = "Category deleted";
				window.getCatTree().getCurrentModel()
						.removeNodeFromParent(toRemove);
			} else {
				message = "Cannot delete category " + selected;
			}
			JOptionPane.showMessageDialog(window.getFrmGoodsBase(), message,
					"Category delete", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**@return the selected node of the JTree*/
	public static DefaultMutableTreeNode getSelectedNode(JTree tree) {
		TreePath path = tree.getSelectionPath();
		if (path == null)
			return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		return node;
	}
}
