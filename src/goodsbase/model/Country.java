package goodsbase.model;

public class Country {
	/**Official name of country*/
	public final String name;
	
	/**Contains 2 uppercase letters of latin alphabet and is unique*/
	public final String abbreviation;
	
	/**@throws NullPointerException if any of parameters is null
	 * @throws IllegalArgumentException if */
	public Country(String name, String abbreviation) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		if(abbreviation == null) throw new  NullPointerException("Abbreviation can't be null");
		if(abbreviation.length() != 2) throw new IllegalArgumentException("Abbreviation must contain 2 symbols");
		this.name = name;
		this.abbreviation = abbreviation.toUpperCase();
	}
	
	@Override
	public String toString() {
		return abbreviation;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Country)) return false;
		if(((Country)obj).abbreviation.equals(this.abbreviation)) return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		return abbreviation.hashCode();
	}

}
