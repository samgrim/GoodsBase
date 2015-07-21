package goodsbase.ui;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import goodsbase.database.Category;
import goodsbase.database.DataLoader;
import goodsbase.model.Warehouse;


/**Category tree widget*/
public class CategoryTreeBuilder {
	
	private static final Logger log = Logger.getLogger(CategoryTreeBuilder.class.getName());
	private static DataLoader loader;
	
	public static JTree getTree(Warehouse wh) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		try {
			loader = new DataLoader();
			long time  = System.nanoTime();
			buildTree(loader.getCategories(wh), root);		
			time = System.nanoTime() - time;
			log.config(String.format("Category tree loaded in %dns", time));
		} catch (SQLException e) {
			String message = "Failed to load categories";
			log.log(Level.WARNING, message, e);
			root.add(new DefaultMutableTreeNode(message));
		} finally {
			try {
				loader.closeConnection();
			} catch (SQLException e) {
				log.log(Level.WARNING, "Failed to close connection", e);
			}
		}
		JTree tree = new JTree(root);
		tree.setRootVisible(false);
		return tree;
	}
	
	
	/*Builds a tree of categories*/
	private static void buildTree(Set<Category> categories, DefaultMutableTreeNode root) {
		Iterator<Category> it = categories.iterator();
		Category c;
		while (it.hasNext()) {
			c = it.next();
			boolean exp = (c.getParentId() == 0) 
						|| (root.getUserObject()!=null 
							&& c.getParentId() == ((Category)root.getUserObject()).getId());
			if(exp) {
				root.add(new DefaultMutableTreeNode(c));
				it.remove();
			}
		}
		for(int i = 0; i < root.getChildCount(); i++) {
			buildTree(categories, (DefaultMutableTreeNode)root.getChildAt(i));
		}
	}
	

}
