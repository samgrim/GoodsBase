package goodsbase.ui.dataloaders.interfaces;

import java.sql.SQLException;

import javax.swing.tree.TreeNode;
/**
 * Loads tree of categories
 */
public interface CategoryTreeLoader {
	
	TreeNode getCatTree() throws SQLException;

}
