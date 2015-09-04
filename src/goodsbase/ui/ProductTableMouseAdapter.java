package goodsbase.ui;

import goodsbase.model.Product;

import java.awt.EventQueue;
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
		final ProductTable table = window.getProductTable();
	    final int currentRow = table.rowAtPoint(point);
	    window.getProductTable().getSelectionModel()
	    	.setSelectionInterval(currentRow, currentRow);
	    if(e.getClickCount() == 2) {
	    	EventQueue.invokeLater(new Runnable() {
				public void run() {	
					Product p = table.getProductAtRowIndex(currentRow);
					WhItemsWindow wd = new WhItemsWindow(p);
					wd.getFrame().setVisible(true);					
				}
			});
	    }
	}
	
	private MainWindow window;
}
