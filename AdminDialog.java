import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AdminDialog extends JDialog
                                 implements ActionListener
{
/*public static void main(String args[])
{ 
   AdminDialog db = new AdminDialog();
} */

   private JButton            createButton;
   private JButton            deleteButton;
   private JButton            displayButton;
   
   private JRadioButton       userButton;
   private JRadioButton       rentalButton;
   private JRadioButton       rentalOutButton;
   private JRadioButton       topTenButton;
   private JRadioButton       last24hrsButton;

   private ButtonGroup        adminButtonGroup;

   private JPanel             topPanel;
   private JPanel             buttonPanel;
   private JPanel             radiobuttonSelectionPanel;
   private JPanel             buttonSelectionPanel;
   private JTable             table;
   private JScrollPane        scroller;
   private Connection         connection;
   private CreateUserDialog   createuserdialog;   
   private CreateRentalDialog createrentaldialog;

   public AdminDialog(Connection newConnection)
   {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      connection              = newConnection;
      topPanel                  = new JPanel();
      radiobuttonSelectionPanel = new JPanel();
      buttonSelectionPanel      = new JPanel();
      buttonPanel               = new JPanel();
      buttonPanel.setLayout(new GridLayout(2,2,0,5));

      displayButton   = new JButton("Display");
      displayButton.setActionCommand("DISPLAY");
      displayButton.addActionListener(this);
      buttonSelectionPanel.add(displayButton);

      createButton   = new JButton("CREATE");
      createButton.setActionCommand("CREATE");
      createButton.addActionListener(this);
      buttonSelectionPanel.add(createButton);

      deleteButton   = new JButton("DELETE");
      deleteButton.setActionCommand("DELETE");
      deleteButton.addActionListener(this);
      buttonSelectionPanel.add(deleteButton);
     
      userButton       = new JRadioButton("Members");
      userButton.setActionCommand("USER");
      userButton.addActionListener(this);
      rentalButton     = new JRadioButton("Rentals",true);
      rentalButton.setActionCommand("RENTALS");
      rentalButton.addActionListener(this);
      rentalOutButton  = new JRadioButton("Rented Out");
      rentalOutButton.setActionCommand("RENTEDOUT");
      rentalOutButton.addActionListener(this);
      topTenButton     = new JRadioButton("Top Ten Last Month");
      topTenButton.setActionCommand("TOPTEN");
      topTenButton.addActionListener(this);
      last24hrsButton  = new JRadioButton("Rentals Last 24 hours");
      last24hrsButton.setActionCommand("LAST24");
      last24hrsButton.addActionListener(this);
      adminButtonGroup = new ButtonGroup();

      adminButtonGroup.add(userButton);
      adminButtonGroup.add(rentalButton);
      adminButtonGroup.add(rentalOutButton);
      adminButtonGroup.add(topTenButton);
      adminButtonGroup.add(last24hrsButton);

      radiobuttonSelectionPanel.add(userButton);
      radiobuttonSelectionPanel.add(rentalButton);
      radiobuttonSelectionPanel.add(rentalOutButton);
      radiobuttonSelectionPanel.add(topTenButton);
      radiobuttonSelectionPanel.add(last24hrsButton);

      buttonPanel.add(radiobuttonSelectionPanel);
      buttonPanel.add(buttonSelectionPanel);


      add(topPanel,BorderLayout.CENTER);
      add(buttonPanel,BorderLayout.NORTH);
      this.setupMainFrame();
      buttonUpdater();
   }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(700,300);
    this.setMinimumSize(new Dimension(700,300));
    this.setLocation(d.width/4, d.height/4);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setTitle("AdminOptionsMenu");
    setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
{  
   String tmp;
   if((e.getActionCommand().equals("USER"))||(e.getActionCommand().equals("RENTALS"))||(e.getActionCommand().equals("RENTEDOUT"))||(e.getActionCommand().equals("TOPTEN"))||(e.getActionCommand().equals("LAST24")))
   {
      System.out.println("UPDATING");
      buttonUpdater();
   }
   else if(e.getActionCommand().equals("DISPLAY") && userButton.isSelected())
   {
      System.out.println("Display Users");
   }
   else if(e.getActionCommand().equals("DISPLAY") && rentalButton.isSelected())
   {
      System.out.println("DisplayRentals rentalButton");
   }
   else if(e.getActionCommand().equals("DISPLAY") && rentalOutButton.isSelected())
   {
      System.out.println("Display rentalOutButton");
   }
   else if(e.getActionCommand().equals("DISPLAY") && topTenButton.isSelected())
   {
          System.out.println("ATTEMPTING TOPTEN");
       tmp = " SELECT rrr.rid,r.title FROM rentals_record_rents rrr, rentals r" 
            +" WHERE (rrr.from_date between (CURDATE() - INTERVAL 30 DAY) AND CURDATE())"
            +" AND rrr.rid = r.rid GROUP BY rrr.rid ORDER BY count(*) desc LIMIT 10;";
       this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && last24hrsButton.isSelected())
   {
           System.out.println("ATTEMPTING HOURS");
       tmp = " SELECT DISTINCT r.rid, r.title, rrr.trackingnum, a.street, a.city, a.state, a.zip,a.phone, rrr.from_date"
            +" FROM rentals_record_rents rrr, rentals r, user u, person p, address a, has_address ha "
            +" WHERE rrr.from_date between (now() - INTERVAL 1 DAY) AND NOW() " 
            +" AND rrr.rid = r.rid AND rrr.pid = u.pid AND u.pid = p.pid AND p.pid = ha.pid AND ha.zip = a.zip AND ha.street = a.street;";
       this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("CREATE") && userButton.isSelected())
   {
      createuserdialog = new CreateUserDialog(connection,true);
      System.out.println("CreateUserDialog");
   }      
   else if(e.getActionCommand().equals("CREATE") && rentalButton.isSelected())
   {
      createrentaldialog = new CreateRentalDialog(connection);
      System.out.println("CreateRentalDialog");
   }
   else if(e.getActionCommand().equals("DELETE") && userButton.isSelected())
   {
      System.out.println("DELETEUser");
   }      
   else if(e.getActionCommand().equals("DELETE") && rentalButton.isSelected())
   {
      System.out.println("DELETERental");
   }   
}//end of action performed
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void buttonUpdater()
{
    createButton.setEnabled(false);
    deleteButton.setEnabled(false);
    if(userButton.isSelected()||rentalButton.isSelected())
    {
      createButton.setEnabled(true);
      deleteButton.setEnabled(true);
    }
}        

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
         columnNames.addElement(metaData.getColumnLabel(i));
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
}//END OF CLASS