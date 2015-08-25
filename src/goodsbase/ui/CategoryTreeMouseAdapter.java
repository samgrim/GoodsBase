package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.util.Loaders;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;

class CategoryTreeMouseAdapter extends MouseAdapter{

	public CategoryTreeMouseAdapter(CategoryTree tree, JTable table) {
		this.tree = tree;
		this.table = table;
	}
	
	private void myPopupEvent(MouseEvent e) {
		int x = e.getX();
			int y = e.getY();
			
			TreePath path = tree.getPathForLocation(x, y);
			if (path == null)
				return;	
			tree.setSelectionPath(path);
			DefaultMutableTreeNode node = 
					(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();	
			/*do not show menu if loading errors*/
			if(node.getUserObject()==CategoryTree.ERROR_STRING) {
				return;
				/*only categories are editable and can store products*/
			} else if (node.getUserObject() instanceof String) {
				tree.getPopupMenu().getEditCategoryMenuItem().setEnabled(false);
				tree.getPopupMenu().getAddProductMenuItem().setEnabled(false);
			} else {
				tree.getPopupMenu().getEditCategoryMenuItem().setEnabled(true);
				tree.getPopupMenu().getAddProductMenuItem().setEnabled(true);
			}
			/*can delete only leaves*/
			if(node.isLeaf()) {						
				tree.getPopupMenu().getRemoveCategoryMenuItem().setEnabled(true);
			} else {
				tree.getPopupMenu().getRemoveCategoryMenuItem().setEnabled(false);
			}
			tree.getPopupMenu().show(tree, x, y);
	}
		
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) myPopupEvent(e);
		/*double-click action*/
		else if (e.getClickCount() ==2) {
			DefaultMutableTreeNode node = 
				(DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
			if(node.getUserObject() instanceof Category) {
				DefaultTableModel tableModel;
				try {
					String[] colomns = {"Name", "Trade Mark", "Manufacturer", "Positions available in warehouse"};
					tableModel = new DefaultTableModel(Loaders.getProductsAsArray((Category)node.getUserObject()), colomns){
						
						@Override
						    public boolean isCellEditable(int row, int column) {
						       //all cells false
						       return false;
						    }
				
						private static final long serialVersionUID = 2901615843314533198L;
					};	
					
				} catch (XPathExpressionException | DataLoadException e1) {
					tableModel = new DefaultTableModel();
					JOptionPane.showMessageDialog(tree.getParent(), "Failed to load products");
				}
				table.setModel(tableModel);
			}
		}
	}
		
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) myPopupEvent(e);
	}
	
	private CategoryTree tree;
	private JTable table;

}
