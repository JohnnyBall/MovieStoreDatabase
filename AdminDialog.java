import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AdminDialog extends JDialog
                                 implements ActionListener
{
   private JButton             createButton;
   private JButton             deleteButton;
   private JButton             displayButton;
   private JButton             returnButton;
   
   private JRadioButton        castButton;
   private JRadioButton        directorButton;
   private JRadioButton        userButton;
   private JRadioButton        rentalButton;
   private JRadioButton        rentalOutButton;
   private JRadioButton        topTenButton;
   private JRadioButton        last24hrsButton;

   private ButtonGroup         adminButtonGroup;

   private JPanel              topPanel;
   private JPanel              buttonPanel;
   private JPanel              radiobuttonSelectionPanel;
   private JPanel              buttonSelectionPanel;
   private JTable              table;
   private JScrollPane         scroller;
   private CreateUserDialog    createuserdialog;   
   private CreateRentalDialog  createrentaldialog;

   private ResultSet           resultSet;
   private ResultSetMetaData   metaData;
   private PreparedStatement   pstmt;
   private Statement           statement;
   private Connection          connection;

   public AdminDialog(Connection newConnection)
   {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      connection                = newConnection;
      topPanel                  = new JPanel();
      radiobuttonSelectionPanel = new JPanel();
      buttonSelectionPanel      = new JPanel();
      buttonPanel               = new JPanel();
      buttonPanel.setLayout(new GridLayout(2,2,0,5));

      displayButton    = new JButton("Display");
      displayButton.setActionCommand("DISPLAY");
      displayButton.addActionListener(this);
      buttonSelectionPanel.add(displayButton);
      
      createButton     = new JButton("CREATE");
      createButton.setActionCommand("CREATE");
      createButton.addActionListener(this);
      buttonSelectionPanel.add(createButton);
      
      deleteButton     = new JButton("DELETE");
      deleteButton.setActionCommand("DELETE");
      deleteButton.addActionListener(this);
      buttonSelectionPanel.add(deleteButton);
      
      
      returnButton     = new JButton("Return");
      returnButton.setActionCommand("RETURN");
      returnButton.addActionListener(this);
      buttonSelectionPanel.add(returnButton);
      
      castButton       = new JRadioButton("Cast");
      castButton.setActionCommand("CAST");
      castButton.addActionListener(this);
      directorButton   = new JRadioButton("Director");
      directorButton.setActionCommand("DIRECTOR");
      directorButton.addActionListener(this);
      userButton       = new JRadioButton("Members");
      userButton.setActionCommand("USER");
      userButton.addActionListener(this);
      rentalButton     = new JRadioButton("Rentals");
      rentalButton.setActionCommand("RENTALS");
      rentalButton.addActionListener(this);
      rentalOutButton  = new JRadioButton("Rented Out");
      rentalOutButton.setActionCommand("RENTEDOUT");
      rentalOutButton.addActionListener(this);
      topTenButton     = new JRadioButton("Top Ten Last Month");
      topTenButton.setActionCommand("TOPTEN");
      topTenButton.addActionListener(this);
      last24hrsButton  = new JRadioButton("Rentals Last 24 hours",true);
      last24hrsButton.setActionCommand("LAST24");
      last24hrsButton.addActionListener(this);
      adminButtonGroup = new ButtonGroup();

      adminButtonGroup.add(castButton);
      adminButtonGroup.add(directorButton);
      adminButtonGroup.add(userButton);
      adminButtonGroup.add(rentalButton);
      adminButtonGroup.add(rentalOutButton);
      adminButtonGroup.add(topTenButton);
      adminButtonGroup.add(last24hrsButton);

      radiobuttonSelectionPanel.add(castButton);
      radiobuttonSelectionPanel.add(directorButton);
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
    this.setSize(750,320);
    this.setMinimumSize(new Dimension(750,320));
    this.setLocation(d.width/4, d.height/4);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setTitle("AdminOptionsMenu");
    setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
{  
   String tmp;
   if((e.getActionCommand().equals("USER"))||(e.getActionCommand().equals("RENTALS"))||(e.getActionCommand().equals("RENTEDOUT"))||(e.getActionCommand().equals("TOPTEN"))||(e.getActionCommand().equals("LAST24"))||(e.getActionCommand().equals("CAST"))||(e.getActionCommand().equals("DIRECTOR")))
   {
      buttonUpdater();
   }
   else if(e.getActionCommand().equals("DISPLAY") && userButton.isSelected())
   {
      tmp = "SELECT * FROM moviestore.person NATURAL JOIN moviestore.user;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && castButton.isSelected())
   {
      tmp = "SELECT * FROM moviestore.person NATURAL JOIN moviestore.mcast;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && directorButton.isSelected())
   {
      tmp = "SELECT * FROM moviestore.person NATURAL JOIN moviestore.director;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && rentalButton.isSelected())
   {
      tmp = "SELECT * FROM moviestore.rentals;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && rentalOutButton.isSelected())
   {
      tmp = "SELECT * FROM rentals_record_rents rrr Where rrr.return_Date is null ;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && topTenButton.isSelected())
   {
      tmp = " SELECT rrr.rid,r.title FROM rentals_record_rents rrr, rentals r" 
          + " WHERE (rrr.from_date between (CURDATE() - INTERVAL 30 DAY) AND CURDATE())"
          + " AND rrr.rid = r.rid GROUP BY rrr.rid ORDER BY count(*) desc LIMIT 10;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("DISPLAY") && last24hrsButton.isSelected())
   {
      tmp = " SELECT DISTINCT r.rid, r.title, rrr.trackingnum, a.street, a.city, a.state, a.zip,a.phone, rrr.from_date"
          + " FROM rentals_record_rents rrr, rentals r, user u, person p, address a, has_address ha "
          + " WHERE rrr.from_date between (now() - INTERVAL 1 DAY) AND NOW() " 
          + " AND rrr.rid = r.rid AND rrr.pid = u.pid AND u.pid = p.pid AND p.pid = ha.pid AND ha.zip = a.zip AND ha.street = a.street;";
      this.doQuery(tmp);
   }
   else if(e.getActionCommand().equals("CREATE") && userButton.isSelected())
   {
      createuserdialog = new CreateUserDialog(connection,true);
   }      
   else if(e.getActionCommand().equals("CREATE") && rentalButton.isSelected())
   {
      createrentaldialog = new CreateRentalDialog(connection);
   }
   else if(e.getActionCommand().equals("DELETE"))
   {
      if(table.getSelectedRow() == -1)
       JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
      else
        deleteQueryExecuter((int)table.getValueAt(table.getSelectedRow(), 0));
   }
   else if(e.getActionCommand().equals("RETURN"))
   {
      if(table.getSelectedRow() == -1)
       JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
      else
        returnQueryExecuter((int)table.getValueAt(table.getSelectedRow(), 0));
   }          
}//end of action performed
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void buttonUpdater()
{
    createButton.setEnabled(false);
    deleteButton.setEnabled(false);
    returnButton.setEnabled(false);

    if(userButton.isSelected()||rentalButton.isSelected())
      createButton.setEnabled(true);
}        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void doQuery(String query)
{
  try 
  {
    statement = connection.createStatement();
    resultSet = statement.executeQuery(query);
    if(!resultSet.next()) 
    {
      JOptionPane.showMessageDialog(null,"No records found!");
      return;
    }
    else 
    {
      Vector<Object> columnNames = new Vector<Object>(); 
      Vector<Object> rows = new Vector<Object>();
      metaData = resultSet.getMetaData();
      for(int i = 1; i <= metaData.getColumnCount(); ++i)
         columnNames.addElement(metaData.getColumnLabel(i));
      do 
      {
         Vector<Object> currentRow = new Vector<Object>();
         for(int i = 1; i <= metaData.getColumnCount(); ++i)
            currentRow.addElement(resultSet.getObject(i));
         rows.addElement(currentRow);
      } 
      while(resultSet.next());
      if(scroller!=null)
         getContentPane().remove(scroller);

      table = new JTable(rows, columnNames);
      table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
      {
          public void valueChanged(ListSelectionEvent event)
          {
              if(userButton.isSelected()||rentalButton.isSelected())
              {
                  createButton.setEnabled(true);
                  deleteButton.setEnabled(true);
              }
              else if(rentalOutButton.isSelected())
              {
                  returnButton.setEnabled(true);
              }
          }
      });
      table.setPreferredScrollableViewportSize(new Dimension(this.getWidth()-35, 10*table.getRowHeight()));
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
void deleteQueryExecuter(int idToDelete)
{
  try 
  {
    if(userButton.isSelected())
      pstmt = connection.prepareStatement(" DELETE FROM moviestore.user WHERE pid = ?");
    else if(rentalButton.isSelected())
      pstmt = connection.prepareStatement(" DELETE FROM moviestore.Rentals WHERE rid = ?");

    pstmt.setInt(1,idToDelete);
    System.out.println("pstmt: " + pstmt.toString());

    System.out.println("About to Execute DELETION FROM MOVIESTORE");
    pstmt.execute();

    System.out.println("hey kid it looks like you deleted the thing, thats great...");
    JOptionPane.showMessageDialog(null, "The item you selected has been DELETED from the database!", "Well thats pretty neat!", JOptionPane.INFORMATION_MESSAGE);
    displayButton.doClick();
    pstmt.close();
    System.out.println("DONE!");
  }//end of try
  catch(SQLException ex) 
  {
    System.out.println(ex.getMessage());
    JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
}// END OF DeleteQueryExecuter
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void returnQueryExecuter(int trackingNumber)
{
  try 
  {
      pstmt = connection.prepareStatement("UPDATE rentals_record_rents SET return_Date = CURDATE() WHERE Trackingnum = ?;");

    pstmt.setInt(1,trackingNumber);
    System.out.println("pstmt: " + pstmt.toString());
    System.out.println("About to Execute UPDATE on rentals_record_rents");
    pstmt.execute();
    JOptionPane.showMessageDialog(null, "The rental you selected has been returned!", "Well thats pretty neat!", JOptionPane.INFORMATION_MESSAGE);
    displayButton.doClick();
    pstmt.close();
    System.out.println("DONE!");
  }//end of try
  catch(SQLException ex) 
  {
    System.out.println(ex.getMessage());
    JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
}// END OF DeleteQueryExecuter
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}//END OF CLASS