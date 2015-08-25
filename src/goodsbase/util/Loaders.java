/**
 * 
 */
package goodsbase.util;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;

import java.util.Arrays;
import java.util.Set;

/**Contains useful methods for loading data
 * @author Daria
 *
 */
public class Loaders {

	/**Loads categories and sorts them by name
	 * @throws DataLoadException
	 * */
	public static Category[] getSortedByNameCategories() throws DataLoadException{		 
		Set<Category> cats = Category.load();
		Category[] catsArr = cats.toArray(new Category[0]);
		Arrays.sort(catsArr, Category.BY_NAME);
		return catsArr;			
	}
	
}
