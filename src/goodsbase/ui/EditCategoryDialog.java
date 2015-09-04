package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.model.Loaders;

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
import javax.swing.border.EmptyBorder;
import javax.xml.xpath.XPathExpressionException;

/**
 * A modal dialog for adding and editing categories
 * 
 * @author Daria
 * 
 */
public class EditCategoryDialog extends JDialog implements ActionListener {

	/**
	 * Creates new dialog for category adding
	 * 
	 * @param owner
	 *            - a parent frame of dialog
	 * @param category
	 *            - a parent category of a new one
	 * @return a new EditCategoryDialog for adding
	 * @throws XPathExpressionException
	 * @throws DataLoadException
	 */
	public static EditCategoryDialog getAddDialog(Frame owner, Category category)
			throws XPathExpressionException, DataLoadException {
		return new EditCategoryDialog(owner, category, INSERT_MODE);
	}

	/**
	 * Creates new dialog for category editing
	 * 
	 * @param owner
	 *            - a parent frame of dialog
	 * @param category
	 *            - to be edited
	 * @return a new EditCategoryDialog for editing
	 * @throws XPathExpressionException
	 * @throws DataLoadException
	 */
	public static EditCategoryDialog getEditDialog(Frame owner, Category category)
			throws XPathExpressionException, DataLoadException {
		return new EditCategoryDialog(owner, category, EDIT_MODE);
	}

	/**
	 * Create the dialog.
	 * 
	 * @throws DataLoadException
	 * @throws XPathExpressionException
	 */
	private EditCategoryDialog(Frame owner, Category category, int mode)
			throws DataLoadException, XPathExpressionException {
		super(owner);	
		
		this.initCategory = category;
		this.mode = mode;
		
		setModal(true);
		setSize(450, 300);
		setLocation((int) owner.getLocation().getX() + owner.getWidth() / 4,
				(int) owner.getLocation().getY() + owner.getHeight() / 4);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EditCategoryDialog.class
						.getResource("/Coin - Stacks (Silver)_24x24.gif")));

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblParentCategory = new JLabel("Parent category:");

		JLabel lblName = new JLabel("Name:");
		name = new JTextField();
		name.setColumns(20);
		JLabel lblDescription = new JLabel("Description:");
		description = new JTextArea();
		description.setFont(new Font("Tahoma", Font.PLAIN, 11));

		Category[] cats = Loaders.getCategoriesByNameAsArray();
		parentCat = new JComboBox<Category>(cats);
		parentCat.addItem(null);
		switch (mode) {
		default:
		case EDIT_MODE:
			setTitle("Edit category");
			parentCat.setSelectedIndex(getParentCategoryIndex(category, cats));
			name.setText(category.getName());
			description.setText(category.getDescription());
			break;
		case INSERT_MODE:
			setTitle("Add a new category");
			parentCat.setSelectedItem(category);
			break;
		}

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPanel
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_contentPanel
										.createParallelGroup(Alignment.LEADING)
										.addComponent(lblParentCategory)
										.addComponent(lblName)
										.addComponent(lblDescription))
						.addGap(18)
						.addGroup(
								gl_contentPanel
										.createParallelGroup(Alignment.LEADING,
												false)
										.addComponent(parentCat,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(name)
										.addComponent(description,
												GroupLayout.DEFAULT_SIZE, 230,
												Short.MAX_VALUE))
						.addContainerGap(84, Short.MAX_VALUE)));
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
																lblParentCategory)
														.addComponent(parentCat))
										.addGap(18)
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(lblName)
														.addComponent(
																name,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												gl_contentPanel
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblDescription)
														.addComponent(
																description,
																GroupLayout.PREFERRED_SIZE,
																99,
																GroupLayout.PREFERRED_SIZE))
										.addContainerGap(39, Short.MAX_VALUE)));
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			this.dispose();
		} else if (e.getActionCommand().equals("OK")) {
			switch (mode) {
			default:
			case EDIT_MODE:
				result = initCategory;
				result.setDescription(description.getText());
				result.setName(name.getText());
				result.setParent((Category) parentCat.getSelectedItem());
				break;
			case INSERT_MODE:
				result = new Category((Category) parentCat.getSelectedItem(),
						name.getText(), description.getText());
				break;
			}
			
			this.dispose();
		}
	}

	/** @return A result category if the dialog was accepted */
	public Category getResult() {
		return result;
	}

	private static int getParentCategoryIndex(Category cat, Category[] cats) {
		if (cat == null)
			return -1;
		int id = cat.getParentId();
		int length = cats.length;
		if (id == 0)
			return length;
		for (int i = 0; i < length; i++) {
			if (cats[i].getId() == id)
				return i;
		}
		return -1;
	}

	private final JPanel contentPanel = new JPanel();
	private JTextField name;
	private JTextArea description;
	private Category result;
	private JComboBox<Category> parentCat;
	private Category initCategory;
	private int mode;
	/**
	 * 
	 */
	private static final int EDIT_MODE = 1;
	private static final int INSERT_MODE = 2;
	private static final long serialVersionUID = 4221500466232825177L;
}
