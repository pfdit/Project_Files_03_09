package order;

import retailSystem.Product;

public class StockItem {
	private Product product;
	private int quantity;

	public StockItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public StockItem() {
		
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
