package goodsbase.model;

/**Describes a good at warehouse*/
public class WarehouseItem {


	/**@throws NullPointerException if good or unit is null*/
	/**@throws IllegalArgumentException if quantity or price is 0 or lesser*/
	public WarehouseItem(Product prod, double quantity, Unit unit, double price) {
		setProduct(prod);
		setQuantity(quantity);
		setUnit(unit);
		setPrice(price);
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		if(quantity <= 0) throw new  NullPointerException("product can't be null");
		this.product = product;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
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


	@Override
	public int hashCode() {
		final int prime = 15;
		int result = 1;
		result = prime * result + product.hashCode();
		long temp;
		temp = Double.doubleToLongBits(price);
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
		if (!product.equals(other.product))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (unit != other.unit)
			return false;
		return true;
	}
	
	private int id;
	private Product product;
	private double quantity;
	private Unit unit;
	private double price;
}