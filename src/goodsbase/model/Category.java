package goodsbase.model;

import goodsbase.qserver.QRequest;
import goodsbase.qserver.QServer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**Describes category of goods*/
public class Category {
	
	private int id;
	private String name;
	private String description;
	private int parentId;
	
	/**@throws NullPointerException if name == null*/
	private Category(int id, int parentId, String name, String description) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		this.id = id;
		this.parentId = parentId;
		this.name = name; 
		this.description = description;
	}
	
	/**Creates a new category as a child of this
	 * @throws NullPointerException if name == null*/
	public Category makeChild(String name, String description) {		
		Category cat = new Category(0, this.id, name, description);
		return cat;
	}

	public String getName() {
		return name;
	}
	
	/**@throws NullPointerException if name == null*/
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

	public int getParentId() {
		return parentId;
	}
	
	public int getId(){
		return this.id;
	}
	
	/**@return name of the category*/
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 16;
		return name.hashCode()*prime;
	}		
	
	/**Loads list of categories from server 
	 * @throws DataLoadException 
	 **/
	public static Set<Category> load() throws DataLoadException {		
		Document doc = DataLoader.load(getRequest());
		try {
			return parse(doc);
		} catch (XPathExpressionException e) {
			throw new DataLoadException(e);
		}
	}
	
	private static QRequest getRequest() {
		QRequest req = new QRequest(QRequest.Type.SELECT);
		req.addQuery("SELECT * FROM categories;");
		return req;
	}
	
	private static Set<Category> parse(Document doc) throws XPathExpressionException {
		Set<Category> cat = new HashSet<Category>();
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath xpath = xpfactory.newXPath();
		NodeList nodes = (NodeList)xpath.evaluate("result/line", doc, XPathConstants.NODESET);
		for(int i = 0; i< nodes.getLength(); i++) {
			Node n = nodes.item(i);
			//must be unique and not null
			int id = Integer.valueOf(xpath.evaluate("ID", n));
			//can be null, so..
			int parentId;
			try{
				parentId = Integer.valueOf(xpath.evaluate("PARENT_ID", n));			
			} catch (NumberFormatException e) {
				parentId = 0;
			}
			Category c = new Category(id,
					parentId,
					xpath.evaluate("NAME", n),
					xpath.evaluate("DESCRIPTION", n));
			cat.add(c);
		}
		return cat;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Category other = (Category) obj;
		if (!name.equals(other.name))
			return false;
		return true;
	}
	
	//TODO:remove
	public static void main(String[] args) throws Exception {
		QServer.start();
		Set<Category> cat = Category.load();
	
		for(Category c:cat) {
			System.out.println(c);
		}
		cat = Category.load();
		QServer.stop();
	}
	

}