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
   private JButton        refreshButton;
   private JPanel         topPanel;
   private JTable         table;
   private JScrollPane    scroller;
   private Connection     connection;
   private JLabel         userInfoLabel;
   private DBHandler      dbhandler;

   public UserInfoDialog(Connection newConnection,Vector<Object> userInfo)
   {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      dbhandler     = new DBHandler();
      this.userInfo = userInfo;
      connection    = newConnection;
      topPanel      = new JPanel();
      refreshButton = new JButton("REFRESH");
      userInfoLabel = new JLabel("USERINFO: PID:"+userInfo.elementAt(0)+" EMAIL:"+userInfo.elementAt(1)+" RentalQuota:"+userInfo.elementAt(3));
      this.add(userInfoLabel,BorderLayout.SOUTH);
      refreshButton.setActionCommand("REFRESH");
      refreshButton.addActionListener(this);

      topPanel.add(refreshButton);

      add(topPanel,BorderLayout.CENTER);

      getRootPane().setDefaultButton(refreshButton);
      this.setupMainFrame();
   }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(500,300);
    this.setMinimumSize(new Dimension(500,300));
    this.setLocation(d.width/4, d.height/4);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setTitle("UserInfoDialog");
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