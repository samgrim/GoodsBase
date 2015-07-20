package goodsbase.ui;

import goodsbase.model.Category;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


/**Category tree widget*/
public class CategoryTree extends JTree {
	
	/*Max number of tree levels*/
	private static final int MAX_DEPTH = 5;
	
	private static final Logger log = Logger.getLogger(CategoryTree.class.getName());

	public CategoryTree(Set<Category> category) {
		setRootVisible(false);
		/*will be placed somewhere else.. may be*/
		/*adding work to garbage collector*/	
		Set<Category> clone = new HashSet<Category>();
		clone.addAll(category);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
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
			if( (c.getParent() == null) || (c.getParent() == root.getUserObject())) {
				root.add(new DefaultMutableTreeNode(c));
				it.remove();
			}
		}
		for(int i = 0; i < root.getChildCount(); i++) {
			buildTree(categories, (DefaultMutableTreeNode)root.getChildAt(i));
		}
	}
	
	public static void main(String[] args) {
		Category cat1 = new Category("cat1");
		Category cat2 = new Category("cat2");
		Category cat3 = new Category("cat3");
		Set<Category> cats = new HashSet<Category>();
		cats.add(cat1);
		cats.add(cat2);
		cats.add(cat3);
		cat1 = cat1.makeChild("cat11");
		cat2 = cat3.makeChild("Cat31");
		cats.add(cat1);
		cats.add(cat2);
		CategoryTree tree = new CategoryTree(cats);
	}

}
