package goodsbase.ui;

import goodsbase.database.Category;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**Category tree widget*/
public class CategoryTree extends JTree {
	
	/*Max number of tree levels*/
	private static final int MAX_DEPTH = 5;
	
	private static final Logger log = Logger.getLogger(CategoryTree.class.getName());

	public CategoryTree(Set<Category> category) {
		super(new DefaultMutableTreeNode());
		DefaultTreeModel model = (DefaultTreeModel)getModel();
		setRootVisible(false);
		/*will be placed somewhere else.. may be*/
		/*adding work to garbage collector*/	
		System.out.println(category);
		Set<Category> clone = new HashSet<Category>();
		clone.addAll(category);		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		long time  = System.nanoTime();
		buildTree(clone, root);		
		time = System.nanoTime() - time;
		log.config(String.format("Category tree loaded in %dns", time));
	}
	
	
	/*Builds a tree of categories*/
	private void buildTree(Set<Category> categories, DefaultMutableTreeNode root) {
		Iterator<Category> it = categories.iterator();
		Category c;
		while (it.hasNext()) {
			c = it.next();
			if( (c.getParentId() == 0) || (c.getParentId() == ((Category)root.getUserObject()).getId())) {
				root.add(new DefaultMutableTreeNode(c));
				it.remove();
			}
		}
		for(int i = 0; i < root.getChildCount(); i++) {
			buildTree(categories, (DefaultMutableTreeNode)root.getChildAt(i));
		}
	}
	

}
