package goodsbase.ui;

import goodsbase.model.DataLoadException;
import goodsbase.model.Product;
import goodsbase.model.Supply;
import goodsbase.model.Unit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

/**
 * @author Daria
 * 
 */
public class AddSupplyDialog extends JDialog implements ActionListener {

	/** Writes supply to database */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			this.dispose();
		} else if (e.getActionCommand().equals("OK")) {
			try {
				double sPrice = Double.valueOf(price.getText());
				if (sPrice <= 0) {
					JOptionPane.showMessageDialog(this, "Wrong price value");
					return;
				}
				double sQuantity = Double.valueOf(quantity.getText());
				if (sQuantity <= 0) {
					JOptionPane.showMessageDialog(this, "Wrong quantity value");
					return;
				}
				Unit sUnit = (Unit) units.getSelectedItem();
				Supply s = new Supply(prod, sQuantity, sUnit, sPrice,
						Supply.Type.ARRIVAL);
				String message;
				if (Supply.updateSupplies(s)) {
					message = "Supply added";
				} else {
					message = "Cannot add supply";
				}
				JOptionPane.showMessageDialog(this, message);
				this.dispose();
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "Check the entered data");
			} catch (DataLoadException e2) {
				log.log(Level.WARNING, "Failed to update supplies", e2);
				JOptionPane
						.showMessageDialog(this, "Failed to update supplies");
			}
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddSupplyDialog(Product prod, Frame owner) {
		super(owner);
		this.prod = prod;
		setTitle("Add supply on " + this.prod.getName() + " TM: "+ prod.getTradeMark()
				+" BY: "+prod.getManufacturer());
		setModal(true);
		setLocation((int)owner.getLocation().getX()+owner.getWidth()/4, (int)owner.getLocation().getY()+owner.getHeight()/4);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddSupplyDialog.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 354, 247);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		units = new JComboBox<Unit>();
		units.setModel(new DefaultComboBoxModel<Unit>(Unit.values()));
		units.setSelectedIndex(0);
		
		JLabel lblUnits = new JLabel("Units:");
		
		JLabel lblPrice = new JLabel("Price:");
		
		price = new JTextField();
		price.setColumns(10);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		
		quantity = new JTextField();
		quantity.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(49)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUnits)
						.addComponent(lblQuantity)
						.addComponent(lblPrice))
					.addGap(74)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(units, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(quantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(price, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(83))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(17)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(49)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPrice)
								.addComponent(price, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblUnits)
							.addComponent(units, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(32)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblQuantity)
						.addComponent(quantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(41, Short.MAX_VALUE))
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
	
	JComboBox<Unit> units;
	private final JPanel contentPanel = new JPanel();
	private JTextField price;
	private JTextField quantity;
	private Product prod;
	
	private static final Logger log = Logger.getLogger(AddSupplyDialog.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = 392636407386674723L;


}
