package goodsbase.model;

import goodsbase.util.WarehouseManager;


/**Describes warehouse*/
public class Warehouse {
	private String name;
	private String address;
	
	public Warehouse(String name, String address) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		if(address == null) throw new  NullPointerException("Address can't be null");
		this.name = name;
		this.address = address;
	}
	
	public WarehouseManager getManager(){
		return null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name == null) throw new  NullPointerException("Name can't be null");
		this.name = name;
	}
	public String getAddress() {
		if(address == null) throw new  NullPointerException("Address can't be null");
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + address.hashCode();
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
		Warehouse other = (Warehouse) obj;
		if (!address.equals(other.address))
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}