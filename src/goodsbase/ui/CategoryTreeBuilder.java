package goodsbase.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
		JTree tree = new JTree(root){
			@Override
			public String getToolTipText(MouseEvent e) {
 
				TreePath p = getPathForLocation(e.getX(), e.getY());
				if (p == null)
					return null;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) p.getLastPathComponent();
				Category c = (Category)node.getUserObject();
				return c.getDescription();
			}
		};
		tree.setToolTipText("");
		
		MouseAdapter ma = new MouseAdapter() {
			private void myPopupEvent(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				JTree tree = (JTree)e.getSource();
				TreePath path = tree.getPathForLocation(x, y);
				if (path == null)
					return;	

				tree.setSelectionPath(path);

				DefaultMutableTreeNode obj = (DefaultMutableTreeNode)path.getLastPathComponent();

				String label = "popup: " + obj.toString();
				JPopupMenu popup = new JPopupMenu();
				popup.add(new JMenuItem(label));
				popup.show(tree, x, y);
			}
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) myPopupEvent(e);
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) myPopupEvent(e);
			}
		};
		tree.addMouseListener(ma);
		tree.setRootVisible(false);
		return tree;
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
	

}
