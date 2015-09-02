package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Product;
import goodsbase.util.Loaders;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.xml.xpath.XPathExpressionException;

import java.awt.Toolkit;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProductDialog extends JDialog implements ActionListener {
	
	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			EditProductDialog dialog = new EditProductDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Create the dialog for adding new product
	 * @throws XPathExpressionException 
	 */
	public static EditProductDialog getAddDialog(Frame owner, Category cat) throws XPathExpressionException {
		EditProductDialog dialog = null;
		try {
			dialog = new EditProductDialog(owner, INSERT_MODE, cat, null);
		} catch (DataLoadException e) {}	//will be never thrown
		return dialog;
	}
	
	/**
	 * Create the dialog for editing existing product
	 * @throws DataLoadException 
	 * @throws XPathExpressionException 
	 */
	public static EditProductDialog getEditDialog(Frame owner, Product prod) throws DataLoadException, XPathExpressionException {
		return new EditProductDialog(owner, EDIT_MODE, null, prod);
	}
	
	public Product getResult(){
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")){
			this.dispose();
		} else if(e.getActionCommand().equals("OK")) {
			//TODO: validate values
			switch(mode){
				default:
				case EDIT_MODE:
					result = prod;	
					result.setName(name.getText());
					result.setDescription(description.getText());
					result.setCategory((Category)((JComboBox<Category>)categoryComp)
										.getSelectedItem());
					result.setManufacturer(manufacturer.getText());
					result.setTradeMark(tradeMark.getText());
					break;
				case INSERT_MODE:
					result = new Product(name.getText(), description.getText(),
							tradeMark.getText(), manufacturer.getText(), category);	
					break;
			}
			this.dispose();
			
		}
		
	}

	
	private EditProductDialog(Frame owner, int mode, Category cat, Product prod) throws DataLoadException, XPathExpressionException {
		super(owner);
		this.mode = mode;
		this.category = cat;
		this.prod = prod;
		setSize(450, 300);
		setLocation((int)owner.getLocation().getX()+owner.getWidth()/4,
				(int)owner.getLocation().getY()+owner.getHeight()/4);		
	
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditProductDialog.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setTitle("Add a new product");
		setResizable(false);
		//setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblCategory = new JLabel("Category: ");
		}

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
		
		switch(mode){
		default:
		case EDIT_MODE:
			setTitle("Edit product");
			Category[] cats = Loaders.getCategoriesByNameAsArray();
			JComboBox<Category> catBox = new JComboBox<Category>(cats);
			catBox.setSelectedItem(prod.getCategory());
			categoryComp = catBox;
			name.setText(prod.getName());
			tradeMark.setText(prod.getTradeMark());
			manufacturer.setText(prod.getManufacturer());
			break;
		case INSERT_MODE:
			setTitle("Add a new product");
			categoryComp = new JLabel(category.getName());
			break;
		}
		
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCategory)
								.addComponent(lblName)
								.addComponent(lblDescription))
							.addGap(32)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(description, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
								.addComponent(name, GroupLayout.PREFERRED_SIZE, 282, GroupLayout.PREFERRED_SIZE)
								.addComponent(categoryComp, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblManufacturer)
								.addComponent(lblTradeMark))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(tradeMark, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
								.addComponent(manufacturer, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))))
					.addContainerGap(52, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCategory)
						.addComponent(categoryComp))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblName)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDescription)
						.addComponent(description, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblManufacturer)
						.addComponent(manufacturer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTradeMark)
						.addComponent(tradeMark, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
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
	
	private int mode;
	private Category category;
	private Product prod;
	private Product result;
	
	private final JPanel contentPanel = new JPanel();
	private JLabel lblCategory;
	private JTextField name;
	private JTextField manufacturer;
	private JTextField tradeMark;
	private JTextArea description;
	private Component categoryComp;

}
