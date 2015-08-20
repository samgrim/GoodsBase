package goodsbase.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

class CategoryTreeMouseAdapter extends MouseAdapter{

	public CategoryTreeMouseAdapter(CategoryTree tree) {
		this.tree = tree;
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
				/*only categories are editable*/
			} else if (node.getUserObject() instanceof String) {
				tree.getPopupMenu().getEditCategoryMenuItem().setEnabled(false);
			} else {
				tree.getPopupMenu().getEditCategoryMenuItem().setEnabled(true);
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
	}
		
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) myPopupEvent(e);
	}
	
	private CategoryTree tree;

}
