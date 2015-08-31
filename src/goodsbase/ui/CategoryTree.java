package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**Category tree widget
 * @author Daria
 */
class CategoryTree extends JTree {
	
	public static final String ERROR_STRING = "Failed to load categories";
	
	public CategoryTree() {
		/*disable double-click expand*/
		this.setToggleClickCount(0);		
		this.setToolTipText("");	
		this.refreshModel();
	}

	public DefaultTreeModel getCurrentModel() {
		return model;
	}

	/*tooltips*/
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
	
	private DefaultTreeModel model;	
	private static final Logger log = Logger.getLogger(CategoryTree.class.getName());			
	/**
	 * 
	 */
	private static final long serialVersionUID = -8792924240955329629L;
}
