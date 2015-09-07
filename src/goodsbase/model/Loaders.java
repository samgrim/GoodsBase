/**
 * 
 */
package goodsbase.model;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import goodsbase.qserver.QRequest;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Contains methods for loading data
 * from database
 * @author Daria
 * 
 */
public class Loaders {

	/**
	 * @return array of categories sorted by name
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 **/
	public static Category[] getCategoriesByNameAsArray() throws DataLoadException,
			XPathExpressionException {
		String query = "SELECT * FROM categories ORDER BY cat_name;";
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);
		Document doc = DataLoader.load(req);
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList) xpath.evaluate("result/line", doc,
				XPathConstants.NODESET);
		Category[] result = new Category[nodes.getLength()];
		for (int i = 0; i < result.length; i++) {
			result[i] = Category.parseCategory(nodes.item(i), xpath);
		}
		return result;
	}

	/**
	 * Loads data for the products table
	 * 
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 */
	public static Object[][] getProductsAsArray(Category cat)
			throws DataLoadException, XPathExpressionException {
		String query = "SELECT prod_id, prod_name, prod_description, prod_trade_mark,"
				+ "prod_manufacturer,(SELECT COUNT(*) FROM wh_items WHERE wh_items.wh_product_id = products.prod_id)"
				+ " as availability FROM products WHERE prod_category_id ="
				+ cat.getId() + ";";
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);
		Document doc = DataLoader.load(req);
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList) xpath.evaluate("result/line", doc,
				XPathConstants.NODESET);

		Object[][] result = new Object[nodes.getLength()][4];
		Product p;
		for (int i = 0; i < result.length; i++) {
			p = Product.parse(xpath, nodes.item(i), cat);
			result[i][0] = p;
			result[i][1] = p.getTradeMark();
			result[i][2] = p.getManufacturer();
			result[i][3] = xpath.evaluate("availability", nodes.item(i));
			if (Integer.valueOf((String) result[i][3]) > 0)
				result[i][3] = "YES";
			else
				result[i][3] = "NO";
		}
		return result;
	}
	
	/**
	 * Loads data for the products table
	 * 
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 */
	public static Object[][] getProductsAsArray() throws DataLoadException,
			XPathExpressionException {
		String query = "SELECT products.prod_id, products.prod_name, products.prod_description, products.prod_trade_mark,"
				+ "products.prod_manufacturer, categories.cat_id, categories.cat_name,"
				+ " categories.cat_description, categories.cat_parent_id, (SELECT COUNT(*) FROM wh_items WHERE wh_items.wh_product_id = products.prod_id)"
				+ " as availability FROM products INNER JOIN categories ON"
				+ " products.prod_category_id = categories.cat_id;";
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);
		Document doc = DataLoader.load(req);
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList) xpath.evaluate("result/line", doc,
				XPathConstants.NODESET);
		Object[][] result = new Object[nodes.getLength()][5];
		Product p;
		Category c;
		for (int i = 0; i < result.length; i++) {
			c = Category.parseCategory(nodes.item(i), xpath);
			p = Product.parse(xpath, nodes.item(i), c);
			result[i][0] = p.getCategory();
			result[i][1] = p;
			result[i][2] = p.getTradeMark();
			result[i][3] = p.getManufacturer();
			result[i][4] = xpath.evaluate("availability", nodes.item(i));
			if (Integer.valueOf((String) result[i][4]) > 0)
				result[i][4] = "YES";
			else
				result[i][4] = "NO";
		}
		return result;
	}
	/**
	 * Loads data for warehouse items
	 * 
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 */
	public static Object[][] getWhItemsOn(Product p) throws DataLoadException, XPathExpressionException {
		String query = "SELECT wh_quantity, wh_units, wh_price, wh_price*wh_quantity AS wh_total"
				+ " FROM wh_items WHERE wh_product_id =" + p.getId() + ";";
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);
		Document doc = DataLoader.load(req);
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList) xpath.evaluate("result/line", doc,
				XPathConstants.NODESET);
		Object[][] result = new Object[nodes.getLength()][4];
		for (int i = 0; i < result.length; i++) {
			result[i][0] = xpath.evaluate("WH_QUANTITY", nodes.item(i));
			result[i][1] = xpath.evaluate("WH_UNITS", nodes.item(i));
			result[i][2] = xpath.evaluate("WH_PRICE", nodes.item(i));
			result[i][3] = xpath.evaluate("wh_total", nodes.item(i));
		}
		return result;
	}
	
	/**
	 * Loads data for supplies view
	 * 
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 */
	public static Object[][] getSuppliesOn(Product p) throws DataLoadException, XPathExpressionException {
		String query = "SELECT supplies_date, supplies_type, supplies_price, supplies_units, supplies_quantity, supplies_price*supplies_quantity AS supplies_total"
				+ " FROM supplies WHERE supplies_product_id =" + p.getId() + ";";
		return getSupplies(query);
	}
	
	private static Object[][] getSupplies(String query) throws DataLoadException, XPathExpressionException {
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery(query);
		Document doc = DataLoader.load(req);
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList) xpath.evaluate("result/line", doc,
				XPathConstants.NODESET);
		Object[][] result = new Object[nodes.getLength()][6];
		DateFormat format = new SimpleDateFormat("MMM. dd, yyyy - HH:mm");
		Date d;
		for (int i = 0; i < result.length; i++) {
			d = new Date(Long.valueOf(xpath.evaluate("SUPPLIES_DATE", nodes.item(i))));
			result[i][0] = format.format(d);
			result[i][1] = xpath.evaluate("SUPPLIES_TYPE", nodes.item(i));
			result[i][2] = xpath.evaluate("SUPPLIES_PRICE", nodes.item(i));
			result[i][3] = xpath.evaluate("SUPPLIES_UNITS", nodes.item(i));
			result[i][4] = xpath.evaluate("SUPPLIES_QUANTITY", nodes.item(i));
			result[i][5] = xpath.evaluate("supplies_total", nodes.item(i));
		}
		return result;
	}

}
