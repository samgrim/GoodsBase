package goodsbase.model;

import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import goodsbase.qserver.QRequest;


/**Describes product*/
public class Product {

	private int id;
	private String name;
	private String description;
	private String manufacturer;
	private Category category;
	private String tradeMark;
	
	/**@throws NullPointerException if name, manufacturer or category is null*/
	public Product(String name, String description, String tradeMark, String manufacturer, Category category) {
		this.setName(name);
		this.setDescription(description);
		this.setTradeMark(tradeMark);
		this.setManufacturer(manufacturer);
		this.setCategory(category);
	}
	
	private Product(int id, String name, String description, String tradeMark,
						String manufacturer, Category category) {
		this(name, description, tradeMark, manufacturer, category);
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	/**@throws NullPointerException if name is null*/
	public void setName(String name) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	/**@throws NullPointerException if manufacturer is null*/
	public void setManufacturer(String manufacturer) {
		if(manufacturer == null) throw new  NullPointerException("Manufacturer can't be null");
		this.manufacturer = manufacturer;
	}

	public Category getCategory() {
		return category;
	}

	/**@throws NullPointerException if category is null*/
	public void setCategory(Category category) {
		if(category == null) throw new  NullPointerException("Category can't be null");
		this.category = category;
	}

	public String getTradeMark() {
		return tradeMark;
	}

	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**@return name + trade mark*/
	@Override
	public String toString() {
		String str = this.name;
		if (this.tradeMark != null) str += " " + tradeMark;
		return str;
	}

	/*description is not included*/
	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + category.hashCode();
		result = prime * result + manufacturer.hashCode();
		result = prime * result + name.hashCode();
		result = prime * result + ((tradeMark == null) ? 0 : tradeMark.hashCode());
		return result;
	}

	/*description is not included*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Product other = (Product) obj;
		if (!category.equals(other.category))
			return false;
		if (!manufacturer.equals(other.manufacturer))
			return false;
		if (!name.equals(other.name))
			return false;
		if (tradeMark == null) {
			if (other.tradeMark != null)
				return false;
		} else if (!tradeMark.equals(other.tradeMark))
			return false;
		return true;
	}
	
	/**Loads set of products of the specified category
	 * from server database
	 * @throws DataLoadException 
	 **/
	public static Set<Product> load(Category c) throws DataLoadException {		
		Document doc = DataLoader.load(getSelectRequest(c));
		try {
			return parse(doc, c);
		} catch (XPathExpressionException e) {
			throw new DataLoadException(e);
		}
	}
	/**Removes product from the server database
	 * @throws DataLoadException 
	 **/
	//TODO: constraints
	public static boolean delete(Product prod) throws DataLoadException {
		int res = DataLoader.execute(getDeleteRequest(prod));
		if (res == QRequest.OK_CODE)
			return true;
		return false;
	}
	


	/**Updates category in the server database
	 * @throws DataLoadException 
	 **/
	public static boolean update(Product prod) throws DataLoadException {
		int res = DataLoader.execute(getUpdateRequest(prod));
		if (res == QRequest.OK_CODE)
			return true;
		return false;
	}
	
	public static boolean insert(Product prod) throws DataLoadException {
		int res = DataLoader.execute(getInsertRequest(prod));
		if (res == QRequest.OK_CODE)
			return true;
		return false;
	}
	
	private static QRequest getSelectRequest(Category cat) {
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery("SELECT id, name, description, trade_mark,"
				+ "manufacturer FROM products WHERE category_id =" + cat.getId() + ";");
		return req;
	}
	
	private static QRequest getInsertRequest(Product prod) {
		QRequest req = new QRequest(QRequest.Type.UPDATE);
		req.addQuery("INSERT INTO products (name, description, trade_mark,"
				+ "manufacturer, category_id) VALUES ( '" + prod.getName() + "','"
						+ prod.getDescription() +"','"
						+ prod.getTradeMark() +"','"
						+ prod.getManufacturer() +"','"
						+ prod.getCategory().getId() +"');");
		return req;
	}
	
	private static QRequest getUpdateRequest(Product prod) {
		QRequest req = new QRequest(QRequest.Type.UPDATE);
		req.addQuery("UPDATE products SET name ='" + prod.getName() 
				+ "', description ='" + prod.getDescription()
				+"', trade_mark = '" + prod.getTradeMark()
				+"', manufacturer = '" + prod.getManufacturer()
				+"', category_id = '" + prod.getCategory().getId()
				+"' WHERE id = "+prod.getId()+";");
		return req;
	}
	
	private static QRequest getDeleteRequest(Product prod) {
		QRequest req = new QRequest(QRequest.Type.UPDATE);
		req.addQuery("DELETE FROM products WHERE id = " + prod.getId() + ";");
		return req;
	}
	
	
	private static Set<Product> parse(Document doc, Category cat) throws XPathExpressionException {
		Set<Product> prod = new HashSet<Product>();
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList)xpath.evaluate("result/line", doc, XPathConstants.NODESET);
		for(int i = 0; i< nodes.getLength(); i++) {
			Node n = nodes.item(i);
			//must be unique and not null
			int id = Integer.valueOf(xpath.evaluate("ID", n));
			//can be null, so..
			
			Product p = new Product(id,
					xpath.evaluate("NAME", n),
					xpath.evaluate("DESCRIPTION", n),
					xpath.evaluate("TRADE_MARK", n),
					xpath.evaluate("MANUFACTURER", n),
					cat);
			prod.add(p);
		}
		return prod;
	}

	
	
}