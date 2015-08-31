package goodsbase.ui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Daria
 *
 */
public class ProductTableMouseAdapter extends MouseAdapter {

	/**
	 * Creates a new mouse adapter for the window
	 */
	public ProductTableMouseAdapter(MainWindow window) {
		this.window = window;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		/*select on click*/
		//TODO: doesn't work fine
		Point point = e.getPoint();
	    int currentRow = window.getProductTable().rowAtPoint(point);
	    window.getProductTable().getSelectionModel()
	    	.setSelectionInterval(currentRow, currentRow);
	}
	
	private MainWindow window;
}
