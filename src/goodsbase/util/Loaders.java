/**
 * 
 */
package goodsbase.util;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.DataLoader;
import goodsbase.model.Product;
import goodsbase.qserver.QRequest;

import java.util.Arrays;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
	
	/**Loads data for the products table
	 * @throws DataLoadException 
	 * @throws XPathExpressionException */
	public static Object[][] getProductsAsArray(Category cat) throws DataLoadException, XPathExpressionException{
		String query = "SELECT id, name, description, trade_mark,"
				+ "manufacturer,(SELECT COUNT(*) FROM wh_items WHERE wh_items.product_id = products.id)"
				+ " as availability FROM products WHERE category_id =" + cat.getId() + ";";
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);		
		Document doc = DataLoader.load(req);		
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList)xpath.evaluate("result/line", doc, XPathConstants.NODESET);
		
		Object[][] result = new Object[nodes.getLength()][4];
		Product p;
		for(int i = 0; i < result.length; i++) {
			p = Product.parse(xpath, nodes.item(i), cat);
			result[i][0] = p;
			result[i][1] = p.getManufacturer();
			result[i][2] = p.getTradeMark();
			result[i][3] = xpath.evaluate("availability", nodes.item(i));
		}
		return result;
	}
	
}
