package goodsbase.model;

/**Describes good*/
public class Good {

	private String name;
	private String description;
	private Manufacturer manufacturer;
	private Category category;
	private String tradeMark;
	
	/**@throws NullPointerException if name, manufacturer or category is null*/
	public Good(String name, String tradeMark, Manufacturer manufacturer, Category category) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		if(manufacturer == null) throw new  NullPointerException("Manufacturer can't be null");
		if(category == null) throw new  NullPointerException("Category can't be null");
		this.name = name;
		this.manufacturer = manufacturer;
		this.category = category;
		/*tradeMark can be null*/
		this.setTradeMark(tradeMark);
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

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	/**@throws NullPointerException if manufacturer is null*/
	public void setManufacturer(Manufacturer manufacturer) {
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
		
		Good other = (Good) obj;
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
	
	
	
	
}