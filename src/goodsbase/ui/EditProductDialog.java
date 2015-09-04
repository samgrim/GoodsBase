package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Loaders;
import goodsbase.model.Product;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.xml.xpath.XPathExpressionException;

/**
 * A modal dialog for adding and editing products
 * 
 * @author Daria
 * 
 */
public class EditProductDialog extends JDialog implements ActionListener {

	/**
	 * Create the dialog for adding new product
	 * 
	 * @param owner
	 *            - a parent frame of dialog
	 * @param category
	 *            - a category to contain new product
	 * @throws XPathExpressionException
	 * @throws DataLoadException
	 */
	public static EditProductDialog getAddDialog(Frame owner, Category cat)
			throws XPathExpressionException, DataLoadException {
		return new EditProductDialog(owner, INSERT_MODE, cat, null);
	}

	/**
	 * Create the dialog for editing existing product
	 * 
	 * @param owner
	 *            - a parent frame of dialog
	 * @param prod
	 *            - a product to be edited
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 */
	public static EditProductDialog getEditDialog(Frame owner, Product prod)
			throws DataLoadException, XPathExpressionException {
		return new EditProductDialog(owner, EDIT_MODE, null, prod);
	}

	public Product getResult() {
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			this.dispose();
		} else if (e.getActionCommand().equals("OK")) {
			switch (mode) {
			default:
			case EDIT_MODE:
				result = initProd;
				result.setDescription(description.getText());
				result.setName(name.getText());
				result.setCategory((Category) categoryBox.getSelectedItem());
				result.setManufacturer(manufacturer.getText());
				result.setTradeMark(tradeMark.getText());
				break;
			case INSERT_MODE:
				result = new Product(name.getText(), description.getText(),
						tradeMark.getText(), manufacturer.getText(),
						(Category) categoryBox.getSelectedItem());
				break;
			}
			
			this.dispose();
		}
	}

	private EditProductDialog(Frame owner, int mode, Category cat, Product prod)
			throws DataLoadException, XPathExpressionException {
		super(owner);
		this.mode = mode;
		this.initProd = prod;
		setSize(450, 300);
		setLocation((int) owner.getLocation().getX() + owner.getWidth() / 4,
				(int) owner.getLocation().getY() + owner.getHeight() / 4);

		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EditProductDialog.class
						.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setTitle("Add a new product");
		setResizable(false);

		getContentPane().setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lblCategory = new JLabel("Category: ");
		JLabel lblName = new JLabel("Name:");
		JLabel lblDescription = new JLabel("Description:");
		JLabel lblManufacturer = new JLabel("Manufacturer:");
		JLabel lblTradeMark = new JLabel("Trade mark:");

		name = new JTextField();
		name.setColumns(50);

		description = new JTextArea();
		description.setFont(new Font("Tahoma", Font.PLAIN, 11));

		manufacturer = new JTextField();
		manufacturer.setColumns(50);

		tradeMark = new JTextField();
		tradeMark.setColumns(50);

		Category[] cats = Loaders.getCategoriesByNameAsArray();
		categoryBox = new JComboBox<Category>(cats);
		switch (mode) {
		default:
		case EDIT_MODE:
			setTitle("Edit product");
			categoryBox.setSelectedItem(prod.getCategory());
			name.setText(prod.getName());
			tradeMark.setText(prod.getTradeMark());
			manufacturer.setText(prod.getManufacturer());
			description.setText(prod.getDescription());
			break;
		case INSERT_MODE:
			setTitle("Add a new product");
			categoryBox.setSelectedItem(cat);
			break;
		}
		
		/*some wild trash by window builder*/
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel
				.setHorizontalGroup(gl_contentPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPanel
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.LEADING,
																false)
														.addGroup(
																gl_contentPanel
																		.createSequentialGroup()
																		.addGroup(
																				gl_contentPanel
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblCategory)
																						.addComponent(
																								lblName)
																						.addComponent(
																								lblDescription))
																		.addGap(32)
																		.addGroup(
																				gl_contentPanel
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								description,
																								GroupLayout.PREFERRED_SIZE,
																								283,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								name,
																								GroupLayout.PREFERRED_SIZE,
																								282,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								categoryBox,
																								GroupLayout.PREFERRED_SIZE,
																								144,
																								GroupLayout.PREFERRED_SIZE)))
														.addGroup(
																gl_contentPanel
																		.createSequentialGroup()
																		.addGroup(
																				gl_contentPanel
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblManufacturer)
																						.addComponent(
																								lblTradeMark))
																		.addGap(18)
																		.addGroup(
																				gl_contentPanel
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								tradeMark,
																								GroupLayout.DEFAULT_SIZE,
																								285,
																								Short.MAX_VALUE)
																						.addComponent(
																								manufacturer,
																								GroupLayout.DEFAULT_SIZE,
																								285,
																								Short.MAX_VALUE))))
										.addContainerGap(52, Short.MAX_VALUE)));
		gl_contentPanel
				.setVerticalGroup(gl_contentPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPanel
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblCategory)
														.addComponent(
																categoryBox))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(lblName)
														.addComponent(
																name,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblDescription)
														.addComponent(
																description,
																GroupLayout.PREFERRED_SIZE,
																94,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblManufacturer)
														.addComponent(
																manufacturer,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblTradeMark)
														.addComponent(
																tradeMark,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(this);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
	}

	private static final int EDIT_MODE = 1;
	private static final int INSERT_MODE = 2;

	private Product result;

	private JTextField name;
	private JTextField manufacturer;
	private JTextField tradeMark;
	private JTextArea description;
	private JComboBox<Category> categoryBox;
	private int mode;
	private Product initProd;

	/**
	 * 
	 */
	private static final long serialVersionUID = -9175541733259074407L;

}
