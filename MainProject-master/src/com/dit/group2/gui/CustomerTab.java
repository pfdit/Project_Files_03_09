package com.dit.group2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import com.dit.group2.order.Order;
import com.dit.group2.person.Person;
import com.dit.group2.retailSystem.RetailSystemDriver;
import com.dit.group2.stock.StockItem;

@SuppressWarnings("serial")
public class CustomerTab extends PersonTab implements MouseListener{

	private RetailSystemDriver driver;
	private JButton newCustomerButton;
	private JComboBox<String> orderComboBox;
	private DefaultComboBoxModel<String> orderComboboxModel;
	private Vector<String> orderComboboxItems;
	private JLabel orderLabel;
	private boolean automaticItemSelection;
	
	
	public CustomerTab(RetailSystemDriver driver) {
		super(driver);
		this.driver = driver;
		automaticItemSelection = true;
		newCustomerButton = new JButton();
		newCustomerButton.addActionListener(this);
		comboboxLabel.setText("Customer");
		orderComboboxItems = new Vector<String>();
		orderComboboxModel = new DefaultComboBoxModel<String>(orderComboboxItems);
		orderComboBox = new JComboBox<String>(orderComboboxModel);
		orderLabel = new JLabel("Orders");

		titleLabel.setText("<html><div style=\"text-align: center;\"> Customer Form </html>");
		orderLabel.setBounds(59, 160, 94, 14);
		orderComboBox.setBounds(200, 160, 265, 23);
		orderComboBox.addItemListener(this);
		orderComboBox.addMouseListener(this);
		orderComboBox.setToolTipText("<html>Click on the text to see details of the currently selected order<br/>or click the down arrow to select another order.</html>");
		ToolTipManager.sharedInstance().setInitialDelay(0);
		mainPanel.add(orderComboBox);
		mainPanel.add(orderLabel);
		
		setTextField(0,driver.getPersonDB().getCustomerList());
		setFieldEditable(false);
		addAllElements();	
	}
	
	
	public void addItemsToOrderCombobox(){
		orderComboboxItems.clear();
		String item = "";
		
		for(Order order : driver.getOrderDB().getCustomerOrderList()){
			if(order.getPerson()==person){
				item ="ID:\t"+ order.getId()+" \t "+"Date: "+order.getDate()+"";
				orderComboboxItems.add(item);
			}
		}
		orderComboBox.setSelectedIndex(orderComboBox.getItemCount()-1);
	
		revalidate();
		repaint();
	}	

	public void setTextField(int index, ArrayList<Person> list){
		
		super.setTextField(index,list);
	
		addItemsToCombobox(driver.getPersonDB().getCustomerList());
		automaticItemSelection = true;
		comboBox.setSelectedIndex(index);
		addItemsToOrderCombobox();
		automaticItemSelection = false;
		revalidate();
		repaint();
	} 
	
	private void showOrderDetails(Order order){
		String orderDetailsMessage="ORDER DATE : "+order.getDate();
		
		orderDetailsMessage+="\nItems in this order: ";
		for(StockItem stockItem : order.getOrderEntryList()){
			orderDetailsMessage+="\n     "+stockItem.getQuantity()+" \t x \t "+stockItem.getProduct().getProductName()+" \t\t\t    Subtotal: \t\u20ac"+(stockItem.getProduct().getRetailPrice()*stockItem.getQuantity());
		}
		
		orderDetailsMessage+="\n\nCUSTOMER ID: "+order.getPerson().getId()+"\nPersonal Details: ";
		orderDetailsMessage+="\n     Name: \t"+order.getPerson().getName();
		orderDetailsMessage+="\n     Phone: \t"+order.getPerson().getContactNumber();
		orderDetailsMessage+="\n     Address: \t"+order.getPerson().getAddress();
		orderDetailsMessage+="\n\nThe total order value is \t\u20ac"+order.getGrandTotalOfOrder()+"\n";
		
		JOptionPane.showMessageDialog(null, orderDetailsMessage,  "ORDER ID: "+ (order.getId())+"    STAFF ID: "+order.getCurrentlyLoggedInStaff().getId()  , JOptionPane.PLAIN_MESSAGE);
	}

	public int getIndex(ArrayList<Person> list){
		int index = super.getIndex(list);
		return index;
	}
	
	public JButton getNewCustomerButton(){
		return newCustomerButton;
	}
	
	public void setAutomaticItemSelection(boolean automaticItemSelection){
		this.automaticItemSelection = automaticItemSelection;
	}
	
	public void personDetailsForm(){	
		super.personDetailsForm();
		if(name!=null && address!=null && email!=null && contactNumber!=null){
		
			if(editMode){
				driver.getPersonDB().changePersonDetails(person, name, email, contactNumber, address, 0, null, null, null);
				setTextField(getIndex(driver.getPersonDB().getCustomerList()), driver.getPersonDB().getCustomerList());
				valid = true;
			}
			else{
				valid = true;
				driver.getPersonDB().createNewPerson(person,name, email, contactNumber, address,  0, null,null, null);
				setTextField(driver.getPersonDB().getCustomerList().size()-1,driver.getPersonDB().getCustomerList());
			}
			deletePersonButton.setEnabled(true);
			newPersonButton.setEnabled(true);
			newPersonButton.setVisible(true);
			editPersonButton.setEnabled(true);
			editPersonButton.setVisible(true);
			submitButton.setVisible(false);	
			cancelButton.setVisible(false);
			cancelEditButton.setVisible(false);
		}	
		else{
			JOptionPane.showMessageDialog(null, ""+errorMessage);
		}	
		revalidate();
		repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		super.actionPerformed(e);	
	
		if(e.getSource()==submitButton){
			if(submitButtonMode == 3){
				editMode = false;
				personDetailsForm();
				if(valid){
					JOptionPane.showMessageDialog(null, "hey");
					driver.getPersonDB().getCustomerList();
					driver.getGui().getTabbedPane().setSelectedComponent(driver.getGui().getCustomerOrderTabbedPane());
					driver.getGui().getCustomerOrderTab().fillUpCustomerComboBox();
					driver.getGui().getCustomerOrderTab().getCustomerComboBox().setSelectedIndex(driver.getPersonDB().getCustomerList().size());
				}
			}
			else if(submitButtonMode == 2){
				editMode = true;
				personDetailsForm();
			}
			else if(submitButtonMode == 1){
				editMode = false;
				personDetailsForm();
			}
			orderComboBox.setEnabled(true);
			comboBox.setEnabled(true);
		}
		if(valid){
			setFieldEditable(false);
			valid = false;
		}
		if(e.getSource()==newPersonButton){	
			clearTextFields(driver.getPersonDB().getCustomerList());
			orderComboBox.setEnabled(false);
			setFieldEditable(true);			
		}	
		if(e.getSource()==editPersonButton){
			orderComboBox.setEnabled(false);
			setFieldEditable(true);	
		}	
		if(e.getSource()==cancelButton){
			setFieldEditable(false);
			orderComboBox.setEnabled(true);
			setTextField(driver.getPersonDB().getCustomerList().size()-1,driver.getPersonDB().getCustomerList());	
			if(!(driver.getPersonDB().getCustomerList().size()>0))
				clearTextFields(driver.getPersonDB().getCustomerList());
		}
		if(e.getSource()==cancelEditButton){		
			setTextField(getIndex(driver.getPersonDB().getCustomerList()),driver.getPersonDB().getCustomerList());
			orderComboBox.setEnabled(true);
			setFieldEditable(false);
			if(!(driver.getPersonDB().getCustomerList().size()>0))
				clearTextFields(driver.getPersonDB().getCustomerList());
		}
		if(e.getSource()==deletePersonButton){
			deletePerson(person, driver.getPersonDB().getCustomerList());
			driver.getGui().getCustomerOrderTab().fillUpCustomerComboBox();
			validate();
		}
		
		if(e.getSource()==newCustomerButton){
			submitButtonMode = 3;
			newPersonButton.doClick();
			
		}
		revalidate();
  		repaint();
	}
	
	@Override
	public void itemStateChanged(ItemEvent event) {	
		if (event.getStateChange() == ItemEvent.SELECTED) {
			if (event.getItemSelectable().equals(comboBox)) {
				automaticItemSelection = true;	
				if(getIndex(driver.getPersonDB().getCustomerList())!=-1){
					setTextField(getIndex(driver.getPersonDB().getCustomerList()),driver.getPersonDB().getCustomerList());	
				}
	        }
			if (event.getItemSelectable().equals(orderComboBox)) {
				
				String[] values = ((String)orderComboBox.getSelectedItem()).split("\\t");
				
				if(values!=null&&!automaticItemSelection){
					showOrderDetails(driver.getOrderDB().getOrderById(getIndex(driver.getPersonDB().getCustomerList()), driver.getOrderDB().getCustomerOrderList()));
				}
			}
			revalidate();
	  		repaint();
       }	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(orderComboBox.isEnabled()){
			String[] values = ((String)orderComboBox.getSelectedItem()).split("\\t");
			if(values!=null){
				showOrderDetails(driver.getOrderDB().getOrderById(Integer.parseInt(values[1].trim()), driver.getOrderDB().getCustomerOrderList()));
			}	
		}
	}
}
