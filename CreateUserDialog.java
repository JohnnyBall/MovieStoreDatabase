import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class CreateUserDialog extends JDialog
                                 implements ActionListener
{
   private JTable            table;
   private JScrollPane       scroller;
   public  JButton           editButton;
   private JPanel            topPanel;
   private JPanel            buttonPanel;
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

   private DBHandler         dbhandler;
   private ResultSet         resultSet;
   private ResultSetMetaData metaData;
   private PreparedStatement pstmt;
   private Statement         statement;
   private Connection        connection;


   public CreateUserDialog(Connection newConnection, boolean isAdmin)
   {
      topPanel      = new JPanel();
      buttonPanel   = new JPanel();
      dbhandler     = new DBHandler();
      connection    = newConnection;
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
      editButton.setActionCommand("EDIT");
      editButton.addActionListener(this);
      buttonPanel.add(editButton);
      add(topPanel,BorderLayout.NORTH);      
      add(buttonPanel,BorderLayout.CENTER);

      getRootPane().setDefaultButton(editButton);
      this.setupMainFrame();
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

    if( !(userNameField.getText().trim().equals("")) && !(addressField.getText().trim().equals("")) && 
        !(cityField.getText().trim().equals("")) && !(stateField.getText().trim().equals("")) && !(quotaField.getText().trim().equals("")) && 
        !(zipField.getText().trim().equals(""))  && !(phoneField.getText().trim().equals("")) && !(emailField.getText().trim().equals("")) && 
        !(new String(pwdField.getPassword()).trim().equals("")))
    {
      try
      {
        Integer.parseInt(zipField.getText().trim());
        Integer.parseInt(quotaField.getText().trim());
        createUserQueryExecuter();
      }
      catch (NumberFormatException nfe)
      {
        JOptionPane.showMessageDialog(this,"Please make sure data in either the zip field or the quotaField is an integer!","RIP.",JOptionPane.WARNING_MESSAGE);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(this,"Please make sure data is entered in every Field.","RIP.",JOptionPane.WARNING_MESSAGE);
    }
  }
}//end of action performed
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void createUserQueryExecuter()
{
  int maxPid = 0;
  try 
  {
//-------------------------------------------------------------------------------------------------------------------------------
    // Retrieves the max pid from the Person table, then sets our max pid to it + 1 for use later on. 
    statement = connection.createStatement();
    resultSet = statement.executeQuery("SELECT MAX(PID) FROM Person");
    System.out.println("statement: " +statement.toString());
    if(!resultSet.next()) 
    {
      JOptionPane.showMessageDialog(null,"No records found!");
      return;
    }
    else
    {
     maxPid = resultSet.getInt(1) + 1;
    }
    statement.close();

//-------------------------------------------------------------------------------------------------------------------------------
    // Inserts the Newly Created person into the persons table with a new pid and the username provided
    pstmt       = connection.prepareStatement(" INSERT INTO Person(pid, pname) VALUES (?, ?);");
    pstmt.setInt(1,maxPid);
    pstmt.setString(2, userNameField.getText().trim());//pname
    System.out.println("userNameField "+userNameField.getText().trim());
    System.out.println("pstmt: " +pstmt.toString());
    System.out.println("About to Execute");
    pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    // Inserts the Newly Created user into the persons table
    pstmt.clearParameters();
    pstmt = connection.prepareStatement(" INSERT INTO User(pid, email, is_admin, rental_quota, user_password) VALUES (?, ?, 0, ?, ?);");
    pstmt.setInt(1,maxPid);
    pstmt.setString(2, emailField.getText().trim());//email
    pstmt.setInt(3, Integer.parseInt(quotaField.getText().trim()));//rental_quota
    pstmt.setString(4, new String(pwdField.getPassword()));//user_password
    System.out.println("pstmt: " +pstmt.toString());
    System.out.println("About to Execute");
    pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    // And here's where id keep my Addresses, IF I HAD ANYYY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Have some addresses now, here they are, they will make it so that users rentals can be shipped to them, isnt that nice?!
    pstmt.clearParameters();
    pstmt = connection.prepareStatement("INSERT address(street, zip, phone, state, city) VALUES(?, ?, ?, ?, ?);");
    pstmt.setString(1, addressField.getText().trim());//sets address
    pstmt.setInt(2, Integer.parseInt(zipField.getText().trim()));// sets zipcode int
    pstmt.setString(3, phoneField.getText().trim());// sets phone number
    pstmt.setString(4, stateField.getText().trim());// sets state
    pstmt.setString(5, cityField.getText().trim());//sets city
    System.out.println("pstmt: " + pstmt.toString());
    System.out.println("About to Execute");
    pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    //Sets up the users has_address field so that we can know where to connect the addresses to the peoples then we can ship their rentals! 
    pstmt.clearParameters();
    pstmt = connection.prepareStatement("INSERT has_address(pid, street, zip) VALUES(?, ?, ?);");
    pstmt.setInt(1,maxPid);// Sets pid for new person value
    pstmt.setString(2, addressField.getText().trim());//sets address
    pstmt.setInt(3, Integer.parseInt(zipField.getText().trim()));// sets zipcode int
    System.out.println("pstmt: " + pstmt.toString());
    System.out.println("About to Execute");
    pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    pstmt.close();
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
}// END OF createUserQueryExecuter
}//END OF CLASS