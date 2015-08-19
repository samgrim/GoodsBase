package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**Category tree widget
 * @author Daria
 */
class CategoryTree extends JTree {
	
	public CategoryTree(JFrame mainWindow) {
		this.mainWindow = mainWindow;
		this.setToolTipText("");
		this.popupMenu = new TreePopupMenu();		
		this.addMouseListener(new PopupMouseAdapter());
		this.refreshModel();
		ActionListener listener = new CategoryTreeNodeMenuListener(this);
		this.popupMenu.addMenuListenerToAllItems(listener);
	}
	
/*	@Override
	public DefaultTreeModel getModel() {
		return (DefaultTreeModel)this.getModel();
	}*/
	

	public DefaultTreeModel getCurrentModel() {
		return model;
	}

	/*pop-up messages*/
	@Override
	public String getToolTipText(MouseEvent e) {	 
		TreePath p = getPathForLocation(e.getX(), e.getY());
		if (p == null)
			return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
		if(node.getUserObject() instanceof Category) {
			Category c = (Category)node.getUserObject();
			return c.getDescription();
		} else return "";
	}

	/**Creates and sets a new model for the tree*/
	public void refreshModel() {
		model = buildModel();
		this.setModel(model);
		this.setExpandedState(this.getPathForRow(0), true);
	/*	if(model.getChildCount(model.getRoot()) > 0) 
			this.setRootVisible(false);	
		else {
			this.setRootVisible(true);
		}*/
	}
	
	/**@return pop up menu of the tree*/
	public TreePopupMenu getPopupMenu() {
		return popupMenu;
	}
	
	/**
	 * @return the mainWindow
	 */
	public JFrame getMainWindow() {
		return mainWindow;
	}


	/**Pop up menu for CategoryTree*/	
	class TreePopupMenu extends JPopupMenu{		
		
		private TreePopupMenu(){
			addCategoryMenuItem =  new JMenuItem("Add Category");
			editCategoryMenuItem = new JMenuItem("Edit Category");
			removeCategoryMenuItem = new JMenuItem("Remove Category");
			this.add(addCategoryMenuItem);
			this.add(editCategoryMenuItem);
			this.add(removeCategoryMenuItem);
		}
	
		public JMenuItem getAddCategoryMenuItem() {
			return addCategoryMenuItem;
		}
		
		public JMenuItem getEditCategoryMenuItem() {
			return editCategoryMenuItem;
		}
		
		public JMenuItem getRemoveCategoryMenuItem() {
			return removeCategoryMenuItem;
		}
		
		public void addMenuListenerToAllItems(ActionListener listener) {
			addCategoryMenuItem.addActionListener(listener);
			editCategoryMenuItem.addActionListener(listener);
			removeCategoryMenuItem.addActionListener(listener);
		}

		private final JMenuItem addCategoryMenuItem;
		private final JMenuItem editCategoryMenuItem;
		private final JMenuItem removeCategoryMenuItem;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2951480151945473351L;
	}
	
	
	private class PopupMouseAdapter extends MouseAdapter{
		private void myPopupEvent(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			
			TreePath path = CategoryTree.this.getPathForLocation(x, y);
			if (path == null)
				return;	
			CategoryTree.this.setSelectionPath(path);
			DefaultMutableTreeNode node = 
					(DefaultMutableTreeNode)CategoryTree.this.getLastSelectedPathComponent();					
			if(node.getUserObject()==ERROR_STRING) {
				return;
				/*only categories are editable*/
			} else if (node.getUserObject() instanceof String) {
				popupMenu.editCategoryMenuItem.setEnabled(false);
			} else {
				popupMenu.editCategoryMenuItem.setEnabled(true);
			}
			/*can delete only leaves*/
			if(node.isLeaf()) {						
				popupMenu.removeCategoryMenuItem.setEnabled(true);
			} else {
				popupMenu.removeCategoryMenuItem.setEnabled(false);
			}
			popupMenu.show(CategoryTree.this, x, y);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) myPopupEvent(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) myPopupEvent(e);
		}
	}
	
	/*creates a new model for the tree*/
	private static DefaultTreeModel buildModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		try {
			Set<Category> categories = Category.load();
			if(categories.size()>0) {
				buildTree(categories, root);
				root.setUserObject("Categories");
			} else {
				root.setUserObject("Right-click to create the first category");
			}
		} catch (DataLoadException e1) {
			log.log(Level.WARNING, ERROR_STRING, e1);
			root.setUserObject(ERROR_STRING);
		}
		return new DefaultTreeModel(root);
	}
	
	/*Builds a tree of categories*/
	private static void buildTree(Set<Category> categories, DefaultMutableTreeNode root) {
		Iterator<Category> it = categories.iterator();
		Category c;
		int rootCatId = (root.getUserObject()==null)? 0
							:((Category)root.getUserObject()).getId();
		while (it.hasNext()) {
			c = it.next();
			if(c.getParentId() == rootCatId) {
				root.add(new DefaultMutableTreeNode(c));
				it.remove();
			}
		}
		for(int i = 0; i < root.getChildCount(); i++) {
			buildTree(categories, (DefaultMutableTreeNode)root.getChildAt(i));
		}
		
	}

	private final TreePopupMenu popupMenu;
	private DefaultTreeModel model;
	
	private JFrame mainWindow;
	
	private static final Logger log = Logger.getLogger(CategoryTree.class.getName());
	private static final String ERROR_STRING = "Failed to load categories";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8792924240955329629L;

}
