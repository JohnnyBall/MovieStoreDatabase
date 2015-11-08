// This program displays the results of a query on a database
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AdminDialog extends JDialog
                                 implements ActionListener
{
   private JButton        toptenbutton;
   private JButton        hoursButton;
   private JPanel         topPanel;
   private JTable         table;
   private JScrollPane    scroller;
   private Connection     connection;

   public AdminDialog(Connection newConnection)
   {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      connection   = newConnection;
      topPanel     = new JPanel();
      hoursButton  = new JButton("Rentals Last 24 hours");
      toptenbutton = new JButton("TopTenLastMonth");
      toptenbutton.setActionCommand("TOPTEN");
      toptenbutton.addActionListener(this);
      hoursButton.setActionCommand("HOURS");
      hoursButton.addActionListener(this);

      topPanel.add(toptenbutton);
      topPanel.add(hoursButton);
      add(topPanel,BorderLayout.CENTER);

      getRootPane().setDefaultButton(toptenbutton);
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
    setTitle("AdminOptionsMenu");
    setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
{  
   String tmp;
   if(e.getActionCommand().equals("TOPTEN"))
   {  
      System.out.println("ATTEMPTING TOPTEN");
       tmp = " SELECT rrr.rid,r.title FROM rentals_record_rents rrr, rentals r" 
               +" WHERE (rrr.from_date between (CURDATE() - INTERVAL 30 DAY) AND CURDATE())"
               +" AND rrr.rid = r.rid GROUP BY rrr.rid ORDER BY count(*) desc LIMIT 10;";
       this.doQuery(tmp);
   }//END OF SEARCH ELSE
   else if(e.getActionCommand().equals("HOURS"))
   {
       System.out.println("ATTEMPTING HOURS");
       tmp = " SELECT DISTINCT r.rid, r.title, rrr.trackingnum, a.street, a.city, a.state, a.zip,a.phone, rrr.from_date"
       +" FROM rentals_record_rents rrr, rentals r, user u, person p, address a, has_address ha "
       +"WHERE rrr.from_date between (now() - INTERVAL 1 DAY) AND NOW() " 
       +"AND rrr.rid = r.rid AND rrr.pid = u.pid AND u.pid = p.pid AND p.pid = ha.pid AND ha.zip = a.zip AND ha.street = a.street;";
       this.doQuery(tmp);
   }//END OF SEARCH ELSE
}//end of action performed
void doQuery(String query)
{
  Statement         statement;
  ResultSet         resultSet;
  ResultSetMetaData metaData;
  try 
  {
    statement = connection.createStatement();
    resultSet = statement.executeQuery(query);
    //If there are no records, display a message
    if(!resultSet.next()) 
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
      metaData = resultSet.getMetaData();
      for(int i = 1; i <= metaData.getColumnCount(); ++i)
         columnNames.addElement(metaData.getColumnName(i));
      // get row data
      do 
      {
         Vector<Object> currentRow = new Vector<Object>();
         for(int i = 1; i <= metaData.getColumnCount(); ++i)
            currentRow.addElement(resultSet.getObject(i));
         rows.addElement(currentRow);
      } 
      while(resultSet.next()); //moves cursor to next record
            
      if(scroller!=null)
         getContentPane().remove(scroller);

      // display table with ResultSet contents
      table = new JTable(rows, columnNames);
      table.setPreferredScrollableViewportSize(new Dimension(this.getWidth(), 10*table.getRowHeight()));
      scroller = new JScrollPane(table);
      getContentPane().add(scroller,BorderLayout.SOUTH);
      validate();
    }
    statement.close();
  }//end of try
  catch(SQLException ex) 
  {
   JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
}// END OF DO QUERY 
}//END OF CLASS