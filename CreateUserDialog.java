import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class CreateUserDialog extends JDialog
                                 implements ActionListener
{

   private Vector<Object>    userInfo;
   private Vector<Object>    addressInfo;
   private Vector<Object>    editInfo;
   public  JButton           editButton;
   private JPanel            topPanel;
   private JPanel            buttonPanel;
   private JTable            table;
   private JScrollPane       scroller;
   private Connection        connection;
   private JLabel            userInfoLabel;
   private JLabel            userNameLabel;
   private JLabel            emailLabel;
   private JLabel            quotaLabel;
   private JLabel            pwdLabel;
   private JLabel            addressLabel;
   private JLabel            stateLabel;
   private JLabel            cityLabel;
   private JLabel            zipLabel;
   private JLabel            phoneLabel;
   private DBHandler         dbhandler;
   private JTextField        userNameField;
   private JTextField        addressField;
   private JTextField        stateField;
   private JTextField        cityField;
   private JTextField        zipField;
   private JTextField        phoneField;
   private JTextField        emailField;
   private JTextField        quotaField;
   private JPasswordField    pwdField;
   private ResultSet         doQueryresultSet;
   private ResultSetMetaData doQuerymetaData;
   private PreparedStatement pstmt;


   public CreateUserDialog(Connection newConnection, boolean isAdmin)
   {
      topPanel      = new JPanel();
      buttonPanel   = new JPanel();
      dbhandler     = new DBHandler();
      this.userInfo = userInfo;
      connection    = newConnection;
      addressInfo   = new Vector<Object>();
      editButton    = new JButton("Submit Detail Edits");
      addressLabel  = new JLabel("Address: ");
      addressField  = new JTextField();
      stateLabel    = new JLabel("State: ");
      stateField    = new JTextField();
      cityLabel     = new JLabel("City: ");
      cityField     = new JTextField();
      zipLabel      = new JLabel("Zip: ");
      zipField      = new JTextField();
      phoneLabel    = new JLabel("Phone: ");
      phoneField    = new JTextField();
      userNameLabel = new JLabel("User's Name: ");
      userNameField = new JTextField();
      emailLabel    = new JLabel("Email:");
      emailField    = new JTextField();
      if(!isAdmin)
      {
        quotaLabel    = new JLabel("Quota Amount:");
        quotaField    = new JTextField("3");
        quotaField.setEnabled(false);
      }
      else
      {
        quotaLabel    = new JLabel("Quota Amount:");
        quotaField    = new JTextField();
      }
      pwdLabel      = new JLabel("Change Password: ");
      pwdField      = new JPasswordField();

      topPanel.setLayout(new GridLayout(18,2,0,5));
      
      topPanel.add(userNameLabel);
      topPanel.add(userNameField);
      topPanel.add(addressLabel);
      topPanel.add(addressField);
      topPanel.add(cityLabel);
      topPanel.add(cityField);
      topPanel.add(stateLabel);
      topPanel.add(stateField);
      topPanel.add(zipLabel);
      topPanel.add(zipField);
      topPanel.add(phoneLabel);
      topPanel.add(phoneField);
      topPanel.add(emailLabel);
      topPanel.add(emailField);
      topPanel.add(quotaLabel);
      topPanel.add(quotaField);
      topPanel.add(pwdLabel);
      topPanel.add(pwdField);
      buttonPanel.add(editButton);
      add(topPanel,BorderLayout.NORTH);      
      add(buttonPanel,BorderLayout.CENTER);

      getRootPane().setDefaultButton(editButton);
      this.setupMainFrame();
      editInfo = new Vector<Object>();
      
      editInfo.addElement(userNameField.getText());
      editInfo.addElement(addressField.getText());
      editInfo.addElement(cityField.getText());
      editInfo.addElement(stateField.getText());
      editInfo.addElement(zipField.getText());
      editInfo.addElement(phoneField.getText());
      editInfo.addElement(emailField.getText());
      editInfo.addElement(quotaField.getText());
      editInfo.addElement(new String(pwdField.getPassword()));
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(500,750);
    this.setMinimumSize(new Dimension(500,750));
    this.setLocation(d.width/4, d.height/8);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setTitle("Add User");
    setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
{  

  if(e.getActionCommand().equals("EDIT"))
  {
      if(!(editInfo.elementAt(0).toString().equals(userNameField.getText().trim())) && !(userNameField.getText().trim().equals("")))
      {
          editInfo.addElement(userNameField.getText().trim());
      }
      
      if(!(editInfo.elementAt(1).toString().equals(addressField.getText().trim())) && !(addressField.getText().trim().equals("")))
      {
          editInfo.addElement(addressField.getText().trim());
      }
      
      if(!(editInfo.elementAt(2).toString().equals(cityField.getText().trim())) && !(cityField.getText().trim().equals("")))
      {
          editInfo.addElement(cityField.getText().trim());
      }
      
      if(!(editInfo.elementAt(3).toString().equals(stateField.getText().trim())) && !(stateField.getText().trim().equals("")))
      {
          editInfo.addElement(stateField.getText().trim());
      }
      
      if(!(editInfo.elementAt(4).toString().equals(zipField.getText().trim())) && !(zipField.getText().trim().equals("")))
      {
          editInfo.addElement(zipField.getText().trim());
      }
      
      if(!(editInfo.elementAt(5).toString().equals(phoneField.getText().trim())) && !(phoneField.getText().trim().equals("")))
      {
          editInfo.addElement(phoneField.getText().trim());
      }
      
      if(!(editInfo.elementAt(6).toString().equals(emailField.getText().trim())) && !(emailField.getText().trim().equals("")))
      {
          editInfo.addElement(emailField.getText().trim());
      }
      
      if(!(editInfo.elementAt(8).toString().equals(new String(pwdField.getPassword()).trim())) && !(new String(pwdField.getPassword()).trim().equals("")))
      {
          editInfo.addElement(new String(pwdField.getPassword()).trim());
      }
      
      //Values in editInfo should be ready to send in a transaction to update userInfo
  }
}//end of action performed
void doQuery(String querytodo,String queryString,int count)
{
  try 
  {
    pstmt = connection.prepareStatement(querytodo);
    System.out.println("querytodo: "+ querytodo);
    pstmt.clearParameters();
    pstmt.setString(1, queryString);

    doQueryresultSet = pstmt.executeQuery();
    System.out.println("About to Execute");
    //If there are no records, display a message
    if(!doQueryresultSet.next()) 
    {
      JOptionPane.showMessageDialog(null,"No records found!");
      return;
    }
    else 
    {
      // columnNames holds the column names of the query result      
      Vector<Object> columnNames = new Vector<Object>(); 
      // rows is a vector of vectors, each vector is a vector of
      // values representing a certain row of the query result
      Vector<Object> rows = new Vector<Object>();
      // get column headers
      doQuerymetaData = doQueryresultSet.getMetaData();
      for(int i = 1; i <= doQuerymetaData.getColumnCount(); ++i)
         columnNames.addElement(doQuerymetaData.getColumnLabel(i));
      // get row data
      do 
      {
         Vector<Object> currentRow = new Vector<Object>();
         for(int i = 1; i <= doQuerymetaData.getColumnCount(); ++i)
            currentRow.addElement(doQueryresultSet.getObject(i));
         rows.addElement(currentRow);
      } 
      while(doQueryresultSet.next()); //moves cursor to next record
            
      if(scroller!=null)
         getContentPane().remove(scroller);

      // display table with ResultSet contents
      table = new JTable(rows, columnNames);
      table.setPreferredScrollableViewportSize(new Dimension(this.getWidth(), 10*table.getRowHeight()));
      scroller = new JScrollPane(table);
      getContentPane().add(scroller,BorderLayout.SOUTH);
      validate();
    }
    pstmt.close();
  }//end of try
  catch(SQLException ex) 
  {
   JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
}// END OF DO QUERY
/*public static void main(String args[])
{ 
   CreateUserDialog db = new CreateUserDialog();
} */
}//END OF CLASS