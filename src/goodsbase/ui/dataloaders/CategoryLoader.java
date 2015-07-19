package goodsbase.ui.dataloaders;

import goodsbase.model.Category;
import goodsbase.model.Warehouse;
import goodsbase.ui.dataloaders.interfaces.CategoryTreeLoader;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class CategoryLoader implements CategoryTreeLoader {

	@Override
	public TreeNode getCatTree() {
		Warehouse wh = new Warehouse("MyWh", "MyWh address");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(wh);
		DefaultMutableTreeNode cat1 = new DefaultMutableTreeNode(new Category("Cat 1"));
		cat1.add(new DefaultMutableTreeNode(new Category("SubCat 1")));
		cat1.add(new DefaultMutableTreeNode(new Category("SubCat 2")));
		cat1.add(new DefaultMutableTreeNode(new Category("SubCat 3")));
		DefaultMutableTreeNode cat2 = new DefaultMutableTreeNode(new Category("Cat 2"));
		cat2.add(new DefaultMutableTreeNode(new Category("SubCat 1")));
		cat2.add(new DefaultMutableTreeNode(new Category("SubCat 2")));
		cat2.add(new DefaultMutableTreeNode(new Category("SubCat 3")));
		DefaultMutableTreeNode cat3 = new DefaultMutableTreeNode(new Category("Cat 3"));
		cat3.add(new DefaultMutableTreeNode(new Category("SubCat 1")));
		cat3.add(new DefaultMutableTreeNode(new Category("SubCat 2")));
		cat3.add(new DefaultMutableTreeNode(new Category("SubCat 3")));
		root.add(cat1);
		root.add(cat2);
		root.add(cat3);
		return root;
	}

}
