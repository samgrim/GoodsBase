package goodsbase.model;

/**Describes a good at warehouse*/
public class WarehouseItem {

	private Good good;
	private double quantity;
	private Unit unit;
	private double price;

	/**@throws NullPointerException if good or unit is null*/
	/**@throws IllegalArgumentException if quantity or price is 0 or lesser*/
	public WarehouseItem(Good good, double quantity, Unit unit, double price) {
		if(good == null) throw new  NullPointerException("Good can't be null");
		if(quantity <= 0) throw new  IllegalArgumentException("Quantity can't be 0 or negative");
		if(unit == null) throw new  NullPointerException("Unit can't be null");
		if(price <= 0) throw new  IllegalArgumentException("Price can't be 0 or negative");
		this.good = good;
		this.quantity = quantity;
		this.unit = unit;
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	/**@throws IllegalArgumentException if quantity is 0 or lesser*/
	public void setQuantity(double quantity) {
		if(quantity <= 0) throw new  IllegalArgumentException("Quantity can't be 0 or negative");
		this.quantity = quantity;
	}

	public Unit getUnit() {
		return unit;
	}

	/**@throws NullPointerException if unit is null*/
	public void setUnit(Unit unit) {
		if(unit == null) throw new  NullPointerException("Unit can't be null");
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}
	
	/**@throws IllegalArgumentException if price is 0 or lesser*/
	public void setPrice(double price) {
		if(price <= 0) throw new  IllegalArgumentException("Price can't be 0 or negative");
		this.price = price;
	}

	public String getName() {
		return good.getName();
	}

	public String getDescription() {
		return good.getDescription();
	}

	public Manufacturer getManufacturer() {
		return good.getManufacturer();
	}

	public String getTradeMark() {
		return good.getTradeMark();
	}

	@Override
	public int hashCode() {
		final int prime = 15;
		int result = 1;
		result = prime * result + good.hashCode();
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(quantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + unit.hashCode();
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
		
		WarehouseItem other = (WarehouseItem) obj;
		if (!good.equals(other.good))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (Double.doubleToLongBits(quantity) != Double
				.doubleToLongBits(other.quantity))
			return false;
		if (unit != other.unit)
			return false;
		return true;
	}
	
}