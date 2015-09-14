package goodsbase.model;

import goodsbase.qserver.QRequest;

import java.util.Date;

/**Describes a product at warehouse*/
public class Supply {
	
	public enum Type {
		ARRIVAL, WRITEOFF;
	}

	/**@throws NullPointerException if good or unit is null*/
	/**@throws IllegalArgumentException if quantity or price is 0 or lesser*/
	public Supply(Product prod, double quantity, Unit unit, double price, Type type) {
		setProduct(prod);
		setQuantity(quantity);
		setUnit(unit);
		setPrice(price);
		setType(type);
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		if(product == null) throw new  NullPointerException("product can't be null");
		this.product = product;
	}

	/**@throws IllegalArgumentException if quantity is 0 or lesser*/
	public void setQuantity(double quantity) {
		if(quantity <= 0) throw new  IllegalArgumentException("Quantity can't be 0 or negative");
		this.quantity = quantity;
	}

	/**@throws NullPointerException if unit is null*/
	public void setUnit(Unit unit) {
		if(unit == null) throw new  NullPointerException("Unit can't be null");
		this.unit = unit;
	}
	
	/**@throws IllegalArgumentException if price is 0 or lesser*/
	public void setPrice(double price) {
		if(price <= 0) throw new  IllegalArgumentException("Price can't be 0 or negative");
		this.price = price;
	}	

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		if(type == null) throw new  NullPointerException("Type can't be null");
		this.type = type;
	}

	
	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**Writes supply to database
	 * @throws DataLoadException*/
	public static boolean updateSupplies(Supply s) throws DataLoadException{
		String query = "INSERT INTO supplies (supplies_date, supplies_type, supplies_product_id, supplies_quantity,"
				+ " supplies_units, supplies_price) VALUES ("
				+ new Date().getTime() + ", '"
				+s.type + "', "
				+s.product.getId() + ", "
				+s.quantity + ", '"
				+s.unit + "', "
				+s.price +");";
	
		int res = DataExecutor.executeUpdate(query);
		if (res == QRequest.OK_CODE)
			return true;
		return false;
	}

	private Product product;
	private double quantity;
	private Unit unit;
	private double price;
	private Type type;
}