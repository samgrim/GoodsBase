package goodsbase.model;

/**Describes manufacturer of goods*/
public class Manufacturer {

	private String name;
	private String address;
	private Country country;
	
	/**@throws NullPointerException if any of parameters is null*/
	public Manufacturer(String name, Country country) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		if(country == null) throw new  NullPointerException("Country can't be null");
		this.name = name;
		this.setCountry(country);
	}
	
	public String getName() {
		return name;
	}
	
	/**@throws NullPointerException if name == null*/
	public void setName(String name) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Country getCountry() {
		return country;
	}

	/**@throws NullPointerException if country == null*/
	public void setCountry(Country country) {
		if(country == null) throw new  NullPointerException("Country can't be null");
		this.country = country;
	}
	
	@Override
	public String toString() {
		return this.name + " " + this.country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + country.hashCode();
		result = prime * result + name.hashCode();
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
		Manufacturer other = (Manufacturer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address)) {
			return false;
		}
		if (!country.equals(other.country)) {
			return false;
		}			
		if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	

}