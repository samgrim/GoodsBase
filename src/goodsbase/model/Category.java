package goodsbase.model;

/**Describes category of goods*/
public class Category {

	private String name;
	private String description;
	private Category parent;
	
	/**@throws NullPointerException if name == null*/
	public Category(String name) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		this.name = name; 
	}
	
	/**Creates a new category as a child of this*/
	public Category makeChild(String name) {		
		Category cat = new Category(name);
		cat.parent = this;
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

	public Category getParent() {
		return parent;
	}
	
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	/**@return name of the category*/
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 16;
		int result = 1;
		result = prime * result + name.hashCode();
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
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
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
}