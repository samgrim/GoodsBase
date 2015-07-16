package goodsbase.ui.dataloaders;

import goodsbase.model.Category;
import goodsbase.ui.dataloaders.interfaces.CategoryTreeLoader;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class CategoryLoader implements CategoryTreeLoader {

	@Override
	public TreeNode getCatTree() {
		Warehouse wh = new Warehouse("MyWh", "MyWh address");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(wh);
		DefaultMutableTreeNode cat1 = new DefaultMutableTreeNode(new Category("Cat 1"));
		DefaultMutableTreeNode cat2 = new DefaultMutableTreeNode(new Category("Cat 2"));
		DefaultMutableTreeNode cat3 = new DefaultMutableTreeNode(new Category("Cat 3"));
		return root;
	}

}
