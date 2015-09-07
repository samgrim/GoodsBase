package goodsbase.ui;

import goodsbase.model.DataLoadException;
import goodsbase.model.Supply;
import goodsbase.model.Unit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RemoveSupplyDialog extends JDialog implements ActionListener{

	/**
	 * Create the dialog.
	 */
	public RemoveSupplyDialog(Supply s) {
		this.supply = s;
		setTitle("Write off " + supply.getProduct().getName() + " TM: "+ supply.getProduct().getTradeMark()
				+" BY: "+supply.getProduct().getManufacturer());
		setResizable(false);
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(RemoveSupplyDialog.class.getResource("/Coin - Stacks (Silver)_24x24.gif")));
		setBounds(100, 100, 393, 136);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		
		int spacing = (int) (supply.getQuantity());
		{
			slider = new JSlider();
			slider.setMinorTickSpacing(1);
			slider.setMajorTickSpacing(spacing);
			
			slider.setMaximum((int)supply.getQuantity());
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setValue(spacing/2);
			slider.addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent e) {
					int val = slider.getValue();
					quantity.setText(String.valueOf(val));
				}
				
			});
		}
		
		quantity = new JTextField();
		quantity.setColumns(10);
		quantity.setText(String.valueOf(spacing/2));
		quantity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					if(supply.getUnit() == Unit.PIECE) {
						int dotPos = quantity.getText().indexOf('.');
						int val;
						if(dotPos > 0) {
							val = Integer.parseInt(quantity.getText().substring(0, dotPos));
							quantity.setText(String.valueOf(val));
						} else
							val = Integer.parseInt(quantity.getText());
						if(val > supply.getQuantity()){
							val = (int)supply.getQuantity();
							quantity.setText(String.valueOf(val));
						} else if(val <0) {
							val = 0;
							quantity.setText(String.valueOf(val));
						}
						slider.setValue(val);				
					} else if (supply.getUnit() == Unit.KG) {
						double val = Double.parseDouble(quantity.getText());
						if(val > supply.getQuantity()){
							val = supply.getQuantity();
							quantity.setText(String.valueOf(val));
						} else if(val <0) {
							val = 0;
							quantity.setText(String.valueOf(val));
						}
						slider.setValue((int)val);	
					}
				} catch (NumberFormatException e0) {
					quantity.setText(String.valueOf(0));
					slider.setValue(0);
				}				
			}			
		});
		
		
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGap(29)
					.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
					.addComponent(quantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(quantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(31, Short.MAX_VALUE))
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "OK":
			try {			
				supply.setQuantity(Double.parseDouble(quantity.getText())); 
				String message;
				if(Supply.updateSupplies(supply)) {
					message = "Supply written off";
				} else {
					message = "Cannot write supply off";
				}
				JOptionPane.showMessageDialog(this, message);
				this.dispose();
			} catch(IllegalArgumentException e1) {
				JOptionPane.showMessageDialog(this, "Check the entered data");
			} catch (DataLoadException e2) {
				log.log(Level.WARNING, "Failed to update supplies", e2);
				JOptionPane.showMessageDialog(this, "Failed to update supplies");
			}
			this.dispose();
			break;
		case "Cancel":
			this.dispose();
			break;
		}
		
	}
	
	private Supply supply;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1084623440508128402L;
	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JTextField quantity;

	private static final Logger log = Logger.getLogger(RemoveSupplyDialog.class.getName());
}
