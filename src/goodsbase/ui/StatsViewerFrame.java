package goodsbase.ui;

import goodsbase.model.DataExecutor;
import goodsbase.model.DataLoadException;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StatsViewerFrame extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StatsViewerFrame frame = new StatsViewerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 */
	public StatsViewerFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(StatsViewerFrame.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setTitle("Statistics view");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		scrollPane.setViewportView(textPane);
		
		initStyle();
		initDoc(textPane);
		
		menuBar = new JMenuBar();
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSave = new JMenuItem("Save");
		mntmSave.setAction(action);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
	}
	
	
	private void initDoc(JTextPane pane) {
		HTMLDocument doc = (HTMLDocument) pane.getDocument();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			StringBuilder line = new StringBuilder();
			line.append("<h1>Goods Base statistics</h1><ul>");
			Document sumStats = getWhSumStats();
			line.append("<li>Categories total: "+xpath.evaluate("result/line/CAT_COUNT", sumStats)+"</li>");
			line.append("<li>Products total: "+xpath.evaluate("result/line/PROD_COUNT", sumStats)+"</li>");
			line.append("<li>Sum at warehouse: "+xpath.evaluate("result/line/WH_SUM", sumStats)+"</li>");		
			line.append("</ul><div id = \"categories\"></div>");
			doc.insertAfterStart(doc.getDefaultRootElement().getElement(0), line.toString());
			Document catStats = getCategoryStats();
			Document prodStats = getProdStats();
			
			NodeList cats = (NodeList) xpath.evaluate("result/line", catStats, XPathConstants.NODESET);
			String id, parent;			
			Element elem;
			Node item;
			for(int i = 0; i < cats.getLength(); i++){
				item = cats.item(i);
				parent = xpath.evaluate("CAT_PARENT_ID", item);
				if(parent.equals("0") || parent.equals("")){
					elem = doc.getElement("categories");
				} else {
					elem = doc.getElement(parent);
				}
				id = xpath.evaluate("CAT_ID", item);
				line = new StringBuilder();
				line.append("<div id=\""+id+"\" class = \""+id+"\">");
					line.append("<strong>");
						line.append(xpath.evaluate("CAT_NAME", item));
						line.append(" - ");
						line.append(xpath.evaluate("CAT_DESCRIPTION", item));
					line.append("</strong><br>");
					line.append("<table>");
						line.append("<tr><th>Subcategories count</th><th>Products in category</th>"
						+ "<th>Sum at warehouse</th><th>Arrivals count</th><th>Arrivals sum</th><th>Write-offs count</th>"
						+ "<th>Write-offs sum</th></tr>");
						line.append("<tr>");
							line.append("<td>" + xpath.evaluate("SUBCATS", item) +"</td>");
							line.append("<td>" + xpath.evaluate("PRODUCTS_IN_CAT", item) + "</td>");
							line.append("<td>" + xpath.evaluate("WH_SUM", item) + "</td>");
							line.append("<td>" + xpath.evaluate("ARRIVALS", item) + "</td>");
							line.append("<td>" + xpath.evaluate("ARRIVALS_SUM", item) + "</td>");
							line.append("<td>" + xpath.evaluate("WRITEOFFS", item) + "</td>");
							line.append("<td>" + xpath.evaluate("WRITEOFFS_SUM", item) + "</td>");
						line.append("</tr>");
					line.append("</table>");
				line.append("</div>");				
				doc.insertBeforeEnd(elem, line.toString());
				writeProdStats(doc, prodStats, id, xpath);
			}
		} catch (BadLocationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataLoadException | XPathExpressionException e) {
			String message= "Failed to load statistics";
			log.log(Level.WARNING, message, e);
			JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private Document getCategoryStats() throws DataLoadException{
		String query = "SELECT CAT_ID,"
				+" CAT_NAME,"
				+"  CAT_DESCRIPTION, CAT_PARENT_ID, "
				+" (SELECT COUNT(*) FROM CATEGORIES WHERE CATEGORIES.CAT_PARENT_ID = CATS.CAT_ID) AS SUBCATS,"
				+"  (SELECT COUNT(*) FROM PRODUCTS INNER JOIN SUPPLIES WHERE PROD_ID = SUPPLIES_PRODUCT_ID AND"
				+" SUPPLIES_TYPE = 'ARRIVAL' AND PROD_CATEGORY_ID = CATS.CAT_ID) AS ARRIVALS,"
				+"  (SELECT SUM(SUPPLIES_PRICE*SUPPLIES_QUANTITY)FROM PRODUCTS INNER JOIN SUPPLIES WHERE PROD_ID = SUPPLIES_PRODUCT_ID AND"
				+" SUPPLIES_TYPE = 'ARRIVAL' AND PROD_CATEGORY_ID = CATS.CAT_ID) AS ARRIVALS_SUM,"
				+"  (SELECT COUNT(*) FROM PRODUCTS INNER JOIN SUPPLIES WHERE PROD_ID = SUPPLIES_PRODUCT_ID AND"
				+" SUPPLIES_TYPE = 'WRITEOFF' AND PROD_CATEGORY_ID = CATS.CAT_ID) AS WRITEOFFS,"
				+"  (SELECT SUM(SUPPLIES_PRICE*SUPPLIES_QUANTITY)FROM PRODUCTS INNER JOIN SUPPLIES WHERE PROD_ID = SUPPLIES_PRODUCT_ID AND"
				+"  SUPPLIES_TYPE = 'WRITEOFF' AND PROD_CATEGORY_ID = CATS.CAT_ID) AS WRITEOFFS_SUM,"
				+" (SELECT SUM(WH_PRICE*WH_QUANTITY) FROM PRODUCTS INNER JOIN WH_ITEMS WHERE PROD_ID = WH_PRODUCT_ID AND PROD_CATEGORY_ID = CATS.CAT_ID) AS WH_SUM,"
				+" COUNT(PRODUCTS.PROD_ID) AS PRODUCTS_IN_CAT"
				+" FROM CATEGORIES AS CATS LEFT JOIN PRODUCTS ON PRODUCTS.PROD_CATEGORY_ID = CATS.CAT_ID GROUP BY CATS.CAT_ID ORDER BY CAT_PARENT_ID, CAT_NAME;";
	
		return DataExecutor.executeSelect(query);
	}
	
	private Document getWhSumStats() throws DataLoadException{
		String query = "SELECT COUNT(*) AS CAT_COUNT, "
				+ "(SELECT COUNT(*) FROM PRODUCTS) AS PROD_COUNT, "
				+ "(SELECT SUM(WH_PRICE*WH_QUANTITY) FROM WH_ITEMS) AS WH_SUM FROM CATEGORIES;";
		return DataExecutor.executeSelect(query);
	}
	
	
	private void writeProdStats(HTMLDocument stats, Document doc, String catId, XPath xpath) throws XPathExpressionException, BadLocationException, IOException{
		NodeList prods = (NodeList) xpath.evaluate("result/line[PROD_CATEGORY_ID="+ catId +"]", doc, XPathConstants.NODESET);
		if(prods.getLength()>0) {
			StringBuilder prodTbl = new StringBuilder("<table class = \"products\"><caption>Products</caption>");
			prodTbl.append("<tr><th>Product</th><th>Positions at warehouse</th><th>Sum at warehouse</th><th>Arrivals count</th><th>Arrivals sum</th><th>Write-offs count</th>"
						+ "<th>Write-offs sum</th></tr>");
			Node item;
			for(int i = 0; i < prods.getLength(); i++) {
				item = prods.item(i);
				prodTbl.append("<tr>");
				prodTbl.append("<td><strong>");
					prodTbl.append(xpath.evaluate("PROD_NAME", item));
					prodTbl.append("</strong> - ");
					prodTbl.append(xpath.evaluate("PROD_DESCRIPTION", item));
					prodTbl.append(" TM: ");
					prodTbl.append(xpath.evaluate("PROD_TRADE_MARK", item));
					prodTbl.append(" Manufacturer: ");
					prodTbl.append(xpath.evaluate("PROD_MANUFACTURER", item));
				prodTbl.append("</td>");
				prodTbl.append("<td>" + xpath.evaluate("WH_POSITIONS", item) + "</td>");
				prodTbl.append("<td>" + xpath.evaluate("WH_SUM", item) + "</td>");
				prodTbl.append("<td>" + xpath.evaluate("ARRIVALS", item) + "</td>");
				prodTbl.append("<td>" + xpath.evaluate("ARRIVALS_SUM", item) + "</td>");
				prodTbl.append("<td>" + xpath.evaluate("WRITEOFFS", item) + "</td>");
				prodTbl.append("<td>" + xpath.evaluate("WRITEOFFS_SUM", item) + "</td>");	
				prodTbl.append("</tr>");
			}
			prodTbl.append("</table>");
			stats.insertBeforeEnd(stats.getElement(catId), prodTbl.toString());
		}
	}
	
	private void initStyle(){		
		StyleSheet style = new StyleSheet();
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		style.addRule("html{ font-family: Helvetica, Arial, Sans-Serif;}");
		style.addRule("h1 { color: #000066; font-size: 1.5em;margin: 10px;}");
		style.addRule("h2 {font-style: italic;font-size: 1em;margin: 10px;}");
		style.addRule("div {margin: 0px;padding: 10px 0px 5px 15px;}");
		style.addRule("table { font-size: 1em;border: 1px solid #666699;}");
		style.addRule("table tr:nth-child(even){background-color: #EEE;}");
		style.addRule("table tr:first-child {border-bottom: 1px dashed;background-color: #9999CC;font-weight: bold;margin-bottom 5px;}");
		style.addRule(".products { font-size: 1em;clear:both;margin-left: 20px;font-style: italic;}");
		style.addRule(".products caption { text-align:left;font-weight:bold;}");
		style.addRule(".products tr:first-child {background-color: #666699;}");
		style.addRule("ul {font-weight: bold;}");
		htmlEditorKit.setStyleSheet(style);
		HTMLDocument doc = (HTMLDocument)htmlEditorKit.createDefaultDocument();
		textPane.setEditorKit(htmlEditorKit);
		textPane.setDocument(doc);
	}
	
	private Document getProdStats() throws DataLoadException{
		String query = "SELECT PRODUCTS.PROD_ID, PRODUCTS.PROD_NAME, PRODUCTS.PROD_DESCRIPTION, PRODUCTS.PROD_MANUFACTURER,"
							 +" PRODUCTS.PROD_TRADE_MARK, PRODUCTS.PROD_CATEGORY_ID,"
							 +" (SELECT SUM(WH_PRICE*WH_QUANTITY) FROM WH_ITEMS WHERE WH_ITEMS.WH_PRODUCT_ID = PRODUCTS.PROD_ID) AS WH_SUM,"
							 +" (SELECT COUNT(*) FROM WH_ITEMS WHERE WH_ITEMS.WH_PRODUCT_ID = PRODUCTS.PROD_ID) AS WH_POSITIONS,"
							   +" (SELECT COUNT(*) FROM SUPPLIES WHERE PRODUCTS.PROD_ID = SUPPLIES.SUPPLIES_PRODUCT_ID AND"
							  + " SUPPLIES_TYPE = 'ARRIVAL') AS ARRIVALS,"
							  + " (SELECT SUM(SUPPLIES_PRICE*SUPPLIES_QUANTITY)FROM SUPPLIES WHERE PRODUCTS.PROD_ID = SUPPLIES.SUPPLIES_PRODUCT_ID AND"
							  + " SUPPLIES_TYPE = 'ARRIVAL') AS ARRIVALS_SUM,"
							  + " (SELECT COUNT(*) FROM SUPPLIES WHERE PRODUCTS.PROD_ID = SUPPLIES.SUPPLIES_PRODUCT_ID AND"
							  + " SUPPLIES_TYPE = 'WRITEOFF') AS WRITEOFFS,"
							  + " (SELECT SUM(SUPPLIES_PRICE*SUPPLIES_QUANTITY)FROM SUPPLIES WHERE PRODUCTS.PROD_ID = SUPPLIES.SUPPLIES_PRODUCT_ID AND"
							 + " SUPPLIES_TYPE = 'WRITEOFF') AS WRITEOFFS_SUM"
							+" FROM PRODUCTS ORDER BY PROD_NAME;";
	
		return DataExecutor.executeSelect(query);
	}

	private JPanel contentPane;
	private JTextPane textPane;
	private static final Logger log = Logger.getLogger(StatsViewerFrame.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -249215709865114410L;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmSave;
	private final Action action = new SwingAction();
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Save");
			putValue(SHORT_DESCRIPTION, "Save report");
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File f) {
					String ext = f.getName();
					int index = ext.lastIndexOf('.');
					if(index<0) return false;
					ext = ext.substring(index, 
										ext.length()).toLowerCase();
					if(ext.equals("html"))
						return true;
					return false;
				}

				@Override
				public String getDescription() {					
					return "*.html";
				}
				
			});
			
			int result = chooser.showSaveDialog(StatsViewerFrame.this);
			if(result == JFileChooser.APPROVE_OPTION) {
				HTMLDocument doc = (HTMLDocument)textPane.getDocument();
				HTMLEditorKit kit = (HTMLEditorKit)textPane.getEditorKit();
				String fileName = chooser.getSelectedFile().getAbsolutePath();
				if(fileName.lastIndexOf('.', fileName.length()-5)<0) {
					fileName +=".html";
				}
				try(PrintWriter pw = new PrintWriter(new FileOutputStream(new File(fileName)))) {
					kit.write(pw, doc, 0, doc.getLength());
				} catch (IOException | BadLocationException e1) {
					log.log(Level.WARNING, "Cannot write file", e1);
				}				
			}
		}
	}
}
