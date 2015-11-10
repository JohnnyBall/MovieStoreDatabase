// This program displays the results of a query on a database
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class UserInfoDialog extends JDialog
                                 implements ActionListener
{

   private Vector<Object> userInfo;
   public  JButton        refreshButton;
   private JPanel         topPanel;
   private JPanel         buttonPanel;
   private JTable         table;
   private JScrollPane    scroller;
   private Connection     connection;
   private JLabel         userInfoLabel;
   private JLabel         userNameLabel;
   private JLabel         emailLabel;
   private JLabel         quotaLabel;
   private JLabel         pwdLabel;
   private DBHandler      dbhandler;
   private JTextField     userNameField;
   private JTextField     emailField;
   private JTextField     quotaField;
   private JPasswordField pwdField;


   public UserInfoDialog(Connection newConnection,Vector<Object> userInfo)
   {
      topPanel      = new JPanel();
      buttonPanel   = new JPanel();
      dbhandler     = new DBHandler();
      this.userInfo = userInfo;
      connection    = newConnection;
      refreshButton = new JButton("REFRESH");
      userNameLabel = new JLabel("User's Name: ");
      userNameField = new JTextField();
      emailLabel    = new JLabel("Email:");
      emailField    = new JTextField();
      quotaLabel    = new JLabel("Quota Amount:");
      quotaField    = new JTextField();
      pwdLabel      = new JLabel("Change Password: ");
      pwdField      = new JPasswordField();
      refreshButton.setVisible(false);
      refreshButton.setActionCommand("REFRESH");
      refreshButton.addActionListener(this);

      topPanel.setLayout(new GridLayout(8,2,0,5));

      if(userInfo.size() != 0)
      {
        //userNameField.setText();
        emailField.setText(userInfo.elementAt(1).toString());
        quotaField.setText(userInfo.elementAt(3).toString());
        pwdField.setText(userInfo.elementAt(4).toString());
        refreshButton.setVisible(true);
      }

      topPanel.add(userNameLabel);
      topPanel.add(userNameField);
      topPanel.add(emailLabel);
      topPanel.add(emailField);
      topPanel.add(quotaLabel);
      topPanel.add(quotaField);
      topPanel.add(pwdLabel);
      topPanel.add(pwdField);

      buttonPanel.add(refreshButton);
      add(topPanel,BorderLayout.NORTH);      
      add(buttonPanel,BorderLayout.CENTER);

      getRootPane().setDefaultButton(refreshButton);
      this.setupMainFrame();
   }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(500,400);
    this.setMinimumSize(new Dimension(500,400));
    this.setLocation(d.width/4, d.height/4);
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
}//end of action performed
void doQuery(String querytodo,String searchFieldText,int count)
{
   ResultSet         doQueryresultSet;
   ResultSetMetaData doQuerymetaData;
   PreparedStatement pstmt;
  try 
  {
    pstmt = connection.prepareStatement(querytodo);
    System.out.println("querytodo: "+ querytodo);
    System.out.println("searchFieldText: "+searchFieldText);
    pstmt.clearParameters();
    pstmt.setString(1, searchFieldText);

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
         columnNames.addElement(doQuerymetaData.getColumnName(i));
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
}//END OF CLASS