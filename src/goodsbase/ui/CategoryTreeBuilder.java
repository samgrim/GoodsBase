package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


/**Category tree widget
 * @author Daria
 */

public class CategoryTreeBuilder {		
	
	public static JTree getTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		JTree tree;
		try {
			Set<Category> categories = Category.load();
			if(categories.size()>0) {
				buildTree(categories, root);
			} else {
				root.setUserObject("Right-click to create the first category");
			}
			
			/*pop-up messages*/
			tree = new JTree(root){
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
			};
			tree.setToolTipText("");
			
			/*pop-up menu*/
			final JPopupMenu treeNodeMenu = getPopupMenu(tree);
			MouseAdapter ma = new MouseAdapter() {
				private void myPopupEvent(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					JTree tree = (JTree)e.getSource();
					TreePath path = tree.getPathForLocation(x, y);
					if (path == null)
						return;	
					tree.setSelectionPath(path);
					treeNodeMenu.show(tree, x, y);
				}
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) myPopupEvent(e);
				}
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) myPopupEvent(e);
				}
			};
			
			tree.addMouseListener(ma);
			
			if(categories.size() > 0) 
				tree.setRootVisible(false);	
			
		} catch (DataLoadException e1) {
			log.log(Level.WARNING, "Failed to load categories", e1);
			root.setUserObject("Failed to load categories");
			tree = new JTree(root);
		}
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
	
	private static Category getSelectedCategory(JTree tree) {
		TreePath path = tree.getSelectionPath();
		if(path == null) return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		Object obj = node.getUserObject();
		return (obj instanceof Category)? (Category)obj : null;
	}
	
	/*pop-up menu listener*/
	private static class TreeNodeMenuListener implements ActionListener{
	
		public TreeNodeMenuListener(JTree tree, JPopupMenu treeNodeMenu) {
			this.tree = tree;
			this.treeNodeMenu = treeNodeMenu;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem menuItem = (JMenuItem)e.getSource();
			Category c = getSelectedCategory(tree);
			if(menuItem == treeNodeMenu.getComponent(0)) {
				addAction(c);
			} else if (menuItem == treeNodeMenu.getComponent(1)){
				editAction(c);
			} else if (menuItem == treeNodeMenu.getComponent(2)) {
				removeAction(c);
			}
		}
		
		private void addAction(Category c){
			JOptionPane.showMessageDialog(null, c.getName(), "Add menu action", JOptionPane.INFORMATION_MESSAGE);			
		}
		
		private void editAction(Category c){
			JOptionPane.showMessageDialog(null, c.getName(), "Edit menu action", JOptionPane.INFORMATION_MESSAGE);			
		}
		
		private void removeAction(Category c) {
			JOptionPane.showMessageDialog(null, c.getName(), "Remove menu action", JOptionPane.INFORMATION_MESSAGE);			
		}
		
		private JTree tree;
		private JPopupMenu treeNodeMenu;
	}
	
	private static JPopupMenu getPopupMenu(JTree tree){
		/*Initializing popup menu for category tree*/
		JPopupMenu treeNodeMenu = new JPopupMenu();
		JMenuItem addChild = new JMenuItem("Add");
		TreeNodeMenuListener listener = new TreeNodeMenuListener(tree, treeNodeMenu);
		addChild.addActionListener(listener);
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(listener);
		JMenuItem remove = new JMenuItem("Remove");
		remove.addActionListener(listener);
		treeNodeMenu.add(addChild);
		treeNodeMenu.add(edit);
		treeNodeMenu.add(remove);
		return treeNodeMenu;
	}
	
	private static final Logger log = Logger.getLogger(CategoryTreeBuilder.class.getName());
}
