package goodsbase.ui;

import goodsbase.model.Category;
import goodsbase.model.DataLoadException;
import goodsbase.util.Loaders;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class EditCategoryDialog extends JDialog implements ActionListener {
	
	public static final int EDIT_MODE = 1;
	public static final int INSERT_MODE = 2;

	/**
	 * Launch the application.
	 */
	/*	public static void main(String[] args) {
		try {
			AddCategoryDialog dialog = new AddCategoryDialog(null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 * @throws DataLoadException in EDIT_MODE only
	 */
	public EditCategoryDialog(Frame container, Category category, int mode) throws DataLoadException {
		super(container);
		this.mode = mode;
		this.category = category;
		setModal(true);
		
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(EditCategoryDialog.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setBounds(100, 100, 450, 300);
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
		
		switch(mode) {
			default:
			case EDIT_MODE:
				//TODO: optimize
				setTitle("Edit category");
				Category[] cats = Loaders.getSortedByNameCategories();
				JComboBox<Category> catBox = new JComboBox<Category>(cats);
				catBox.addItem(null);
				catBox.setSelectedIndex(
						getParentCategoryIndex(category, cats));
				parentCat = catBox;
				name.setText(category.getName());
				description.setText(category.getDescription());
				break;
			case INSERT_MODE:
				setTitle("Add a new category");
				parentCat = new JLabel("");
				//display parent if only it exists
				if(category == null){
					lblParentCategory.setVisible(false);
					parentCat.setVisible(false);
				} else {
					((JLabel)parentCat).setText(category.toString());
				}
				break;			
		}
		
	
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblParentCategory)
						.addComponent(lblName)
						.addComponent(lblDescription))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(parentCat, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(name)
						.addComponent(description, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))
					.addContainerGap(84, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblParentCategory)
						.addComponent(parentCat))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDescription)
						.addComponent(description, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(39, Short.MAX_VALUE))
		);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel")){
			this.dispose();
		} else if(e.getActionCommand().equals("OK")) {
			//TODO: validate values
			switch(mode){
				default:
				case EDIT_MODE:
					result = category;	
					result.setName(name.getText());
					result.setDescription(description.getText());
					result.setParent((Category)((JComboBox<Category>)parentCat)
										.getSelectedItem());
					break;
				case INSERT_MODE:
					result = new Category(category, name.getText(), description.getText());	
					break;
			}
			this.dispose();
			
		}
		
	}
	
	public Category getResult(){
		return result;
	}
	
	/*TODO: get rid of this..*/
	private static int getParentCategoryIndex(Category cat, Category[] cats) {
		int id = cat.getParentId();
		int length = cats.length;
		if(id == 0)
			return length;		
		for(int i = 0; i < length; i++){
			if(cats[i].getId() == id)
				return i;
		}
		/*to select null element*/
		return length;
	}
	
	private final JPanel contentPanel = new JPanel();
	private JTextField name;
	private JTextArea description;
	/*parent cat. in insert mode or current in edit mode*/
	private Category category;
	private Category result;	
	private Component parentCat;
	private final int mode;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4221500466232825177L;
}
