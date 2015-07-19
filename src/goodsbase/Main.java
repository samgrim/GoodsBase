package goodsbase;

import goodsbase.database.DbConnection;
import goodsbase.database.DbInitException;
import goodsbase.ui.MainWindow;

public class Main {

	public static void main(String[] args) {
		try {
			DbConnection.init();
		} catch (DbInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainWindow.launch();
	}

}
