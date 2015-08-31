package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.util.Loaders;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;

class CategoryTreeMouseAdapter extends MouseAdapter{

	public CategoryTreeMouseAdapter(MainWindow window) {
		this.window = window;
	
		ActionListener listener = new CategoryTreeNodeMenuListener(window);
		treeMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Add Category");
		item.setActionCommand("addCategory");
		item.addActionListener(listener);
		treeMenu.add(item);
		item = new JMenuItem("Edit Category");
		item.setActionCommand("editCategory");
		item.addActionListener(listener);
		treeMenu.add(item);
		item = new JMenuItem("Remove Category");
		item.setActionCommand("removeCategory");
		item.addActionListener(listener);
		treeMenu.add(item);
		item = new JMenuItem("Add Product");
		item.setActionCommand("addProduct");
		item.addActionListener(listener);
		treeMenu.add(item);
	}
	
	private void myPopupEvent(MouseEvent e) {
		int x = e.getX();
			int y = e.getY();
			
			TreePath path = window.getCatTree().getPathForLocation(x, y);
			if (path == null)
				return;	
			window.getCatTree().setSelectionPath(path);
			DefaultMutableTreeNode node = 
					(DefaultMutableTreeNode)window.getCatTree().getLastSelectedPathComponent();	
			/*do not show menu if loading errors*/
			if(node.getUserObject()==CategoryTree.ERROR_STRING) {
				return;
				/*only categories are editable and can store products*/
			} else if (node.getUserObject() instanceof String) {
				treeMenu.getComponent(1).setEnabled(false); //add category
				treeMenu.getComponent(3).setEnabled(false);//add product
			} else {
				treeMenu.getComponent(1).setEnabled(true);//add category
				treeMenu.getComponent(3).setEnabled(true);//add product
			}
			/*can remove only leaves*/
			if(node.isLeaf()) {						
				treeMenu.getComponent(2).setEnabled(true);//remove category
			} else {
				treeMenu.getComponent(2).setEnabled(false);
			}
			treeMenu.show(window.getCatTree(), x, y);
	}
		
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) myPopupEvent(e);
		/*double-click action*/
		else if (e.getClickCount() ==2) {
			DefaultMutableTreeNode node = 
				(DefaultMutableTreeNode)window.getCatTree().getSelectionPath().getLastPathComponent();
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
					JOptionPane.showMessageDialog(window.getCatTree().getParent(), "Failed to load products");
				}
				window.getProductTable().setModel(tableModel);
			}
		}
	}
		
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) myPopupEvent(e);
	}

	private final MainWindow window;
	private final JPopupMenu treeMenu;
}
