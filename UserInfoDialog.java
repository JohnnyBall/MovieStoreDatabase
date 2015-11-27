// This program displays the results of a query on a database
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UserInfoDialog extends JDialog implements ActionListener
{
   private Vector<Object>    userInfo;
   private Vector<Object>    addressInfo;
   //private Vector<Object>    editInfo;

   public  JButton           refreshButton;
   public  JButton           editButton;

   private JPanel            topPanel;
   private JPanel            buttonPanel;
   private String            usersName;

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
   private DBHandler         dbhandler;
   private ResultSet         resultSet;
   private ResultSetMetaData metaData;
   private Statement         statement;

   public UserInfoDialog(Connection newConnection,Vector<Object> userInfo)
   {
      try
      {
      this.userInfo = userInfo;
      connection    = newConnection;
      topPanel      = new JPanel();
      buttonPanel   = new JPanel();
      dbhandler     = new DBHandler();
      addressInfo   = new Vector<Object>();
      refreshButton = new JButton("REFRESH");
      editButton    = new JButton("Submit Detail Edits");
      userNameLabel = new JLabel("User's Name: ");
      userNameField = new JTextField();
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
      emailLabel    = new JLabel("Email:");
      emailField    = new JTextField();
      quotaLabel    = new JLabel("Quota Amount:");
      quotaField    = new JTextField();
      pwdLabel      = new JLabel("Change Password: ");
      pwdField      = new JPasswordField();

      quotaField.setEditable(false);
      refreshButton.setVisible(false);
      refreshButton.setActionCommand("REFRESH");
      refreshButton.addActionListener(this);

      topPanel.setLayout(new GridLayout(18,2,0,5));


//THIS SHOULD BE A METHOD////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
      pstmt = connection.prepareStatement(dbhandler.personNameSearch);
      System.out.println("running this Query:"+dbhandler.personNameSearch);
      pstmt.clearParameters();
      pstmt.setString(1, userInfo.elementAt(0).toString());
      doQueryresultSet = pstmt.executeQuery();
      if(!doQueryresultSet.next()) 
      {
        JOptionPane.showMessageDialog(null,"No records found!");
        return;
      }
      else
      {
        usersName = doQueryresultSet.getObject(1).toString();
        userNameField.setText(usersName);
      }        
      pstmt.close();
      pstmt = connection.prepareStatement(dbhandler.addressSearch);
      pstmt.clearParameters();
      pstmt.setInt(1, (int)userInfo.elementAt(0));
      doQueryresultSet = pstmt.executeQuery();
      if(!doQueryresultSet.next())
      {
          JOptionPane.showMessageDialog(null,"No records found!");
          return;
      }
      else
      {
          for(int i = 1; i <= doQueryresultSet.getMetaData().getColumnCount(); ++i)
              addressInfo.addElement(doQueryresultSet.getObject(i));
      }
      pstmt.close();

      addressField.setText((String)addressInfo.elementAt(0));
      cityField.setText((String)addressInfo.elementAt(1));
      stateField.setText((String)addressInfo.elementAt(2));

      zipField.setText(addressInfo.elementAt(3).toString());
      phoneField.setText(addressInfo.elementAt(4).toString());
      emailField.setText(userInfo.elementAt(1).toString());
      quotaField.setText(userInfo.elementAt(3).toString());
      pwdField.setText(userInfo.elementAt(4).toString());

      refreshButton.setVisible(true);

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

      buttonPanel.add(refreshButton);
      buttonPanel.add(editButton);
      editButton.setActionCommand("EDIT");
      editButton.addActionListener(this);
      add(topPanel,BorderLayout.NORTH);      
      add(buttonPanel,BorderLayout.CENTER);

      getRootPane().setDefaultButton(refreshButton);
      this.setupMainFrame();      
      }
      catch(SQLException ex) 
      {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
      }
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
    setTitle("User Info");
    setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
{  
  if(e.getActionCommand().equals("REFRESH"))
   {              
    doQuery( dbhandler.accountDetails,userInfo.elementAt(0).toString(),1);
   }
  
  else if(e.getActionCommand().equals("EDIT"))
  {
     updateUserQueryExecuter();
  }
}//end of action performed
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void updateUserQueryExecuter()
{
  try 
  {
//-------------------------------------------------------------------------------------------------------------------------------
    // 
        usersName = userNameField.getText().trim();
        pstmt     = connection.prepareStatement("UPDATE  IGNORE Person SET pname = ? WHERE pid = ?;");
        pstmt.setString(1,usersName);
        pstmt.setInt(2, (int)userInfo.elementAt(0));
        System.out.println("userName " + usersName);
        System.out.println("pstmt: "   + pstmt.toString());
        System.out.println("About to Execute UPDATE PERSONS NAME");
        pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    //
        userInfo.set(1,emailField.getText().trim());
        userInfo.set(4,new String(pwdField.getPassword()).trim());

        pstmt     = connection.prepareStatement("UPDATE IGNORE User SET email = ? , user_password = ? WHERE pid = ?;");
        pstmt.setString(1,userInfo.elementAt(1).toString());
        pstmt.setString(2,userInfo.elementAt(4).toString());
        pstmt.setInt(3, (int)userInfo.elementAt(0));

        System.out.println("userName " + usersName);
        System.out.println("pstmt: "   + pstmt.toString());
        System.out.println("About to Execute UPDATE USERS INFO!!!!!!!!");
        pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    //
        pstmt = connection.prepareStatement(" UPDATE IGNORE address "
                                          + " SET street   = ? , zip = ? , state = ? , phone = ? , city = ?"
                                          + " WHERE street = (SELECT hs.street FROM has_address hs WHERE hs.pid = ?);");

        addressInfo.set(0,addressField.getText().trim());
        addressInfo.set(1,cityField.getText().trim());
        addressInfo.set(2,stateField.getText().trim());
        addressInfo.set(3,zipField.getText().trim());
        addressInfo.set(4,phoneField.getText().trim());

        pstmt.setString(1,addressInfo.elementAt(0).toString());
        pstmt.setInt(2, Integer.parseInt(addressInfo.elementAt(3).toString()));
        pstmt.setString(3,addressInfo.elementAt(2).toString());
        pstmt.setString(4,addressInfo.elementAt(4).toString());
        pstmt.setString(5,addressInfo.elementAt(1).toString());
        pstmt.setInt(6, (int)userInfo.elementAt(3));

        System.out.println("pstmt: "   + pstmt.toString());
        System.out.println("About to Execute UPDATE PERSONS ADDRESS!!!!!!");
        pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    pstmt.close();
    System.out.println("LEAVING");
    JOptionPane.showMessageDialog(null, "hey kid it looks like newly created user went through, thats great...", "Well thats pretty neat!", JOptionPane.INFORMATION_MESSAGE);
  }//end of try
  catch(SQLException ex) 
  {
    System.out.println(ex.getMessage());
    JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
  catch (NumberFormatException nfe)
  {
    JOptionPane.showMessageDialog(this,"Please make sure data in either the zip field or the quotaField is an integer!","RIP.",JOptionPane.WARNING_MESSAGE);
  }
}// END OF updateUserQueryExecuter 
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
      Vector<Object> columnNames = new Vector<Object>(); 
      Vector<Object> rows        = new Vector<Object>();
      doQuerymetaData            = doQueryresultSet.getMetaData();

      for(int i = 1; i <= doQuerymetaData.getColumnCount(); ++i)
         columnNames.addElement(doQuerymetaData.getColumnLabel(i));

      do 
      {
         Vector<Object> currentRow = new Vector<Object>();
         for(int i = 1; i <= doQuerymetaData.getColumnCount(); ++i)
            currentRow.addElement(doQueryresultSet.getObject(i));
         rows.addElement(currentRow);
      } 
      while(doQueryresultSet.next());
            
      if(scroller!=null)
         getContentPane().remove(scroller);

      table    = new JTable(rows, columnNames);
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}//END OF CLASS