package goodsbase.ui;

import goodsbase.Main;
import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.User;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.xpath.XPathExpressionException;

class CategoryTreeMouseAdapter extends MouseAdapter{

	public CategoryTreeMouseAdapter(MainWindow window) {
		this.window = window;
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
				Main.getCurrentUser().getRole().equals(User.WH_MANAGER)) {
		ActionListener listener = new CategoryTreeNodeMenuListener(window);
		/*menu init*/
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
		} else {
			treeMenu = null;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
				Main.getCurrentUser().getRole().equals(User.WH_MANAGER))
			if (e.isPopupTrigger()) popupEvent(e);
	}
		
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if(Main.getCurrentUser().getRole().equals(User.ADMIN) ||
					Main.getCurrentUser().getRole().equals(User.WH_MANAGER))
				popupEvent(e);
		} else if (e.getClickCount() ==2) {
				doubleClickEvent();
		}
	}
	
	
	private void doubleClickEvent(){
		DefaultMutableTreeNode node = 
					(DefaultMutableTreeNode)window.getCatTree().getSelectionPath().getLastPathComponent();
		/*return if errors in category tree*/
		if(node.getUserObject() instanceof String 
				&& ((String)node.getUserObject()).equals(CategoryTree.ERROR_STRING)) {
			return;
		}		
		try {
			Category cat = (node.getUserObject() instanceof Category)?
					(Category)node.getUserObject()
					: null;
			window.getProductTable().loadItems(cat);					
		} catch (XPathExpressionException | DataLoadException e1) {
			JOptionPane.showMessageDialog(window.getCatTree().getParent(), "Failed to load products");
		}	
	}
	
	private void popupEvent(MouseEvent e) {
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
		


	private final MainWindow window;
	private final JPopupMenu treeMenu;
}
