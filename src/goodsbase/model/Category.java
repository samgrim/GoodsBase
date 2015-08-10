package goodsbase.model;

/**Describes category of goods*/
public class Category {
	
	private int id;
	private String name;
	private String description;
	private int parentId;
	
	/**@throws NullPointerException if name == null*/
	public Category(int id, int parentId, String name, String description) {
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
}