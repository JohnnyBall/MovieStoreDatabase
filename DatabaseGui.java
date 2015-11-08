// This program displays the results of a query on a database
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DatabaseGui extends JFrame
                                 implements ActionListener
{
   private JLabel         idLabel;
   private JLabel         pwdLabel;
   private JTextField     idField;
   private JPasswordField pwdField;
   private JTextField     searchField;

   private JButton        loginButton;
   private JButton        searchButton;

   private JRadioButton   castButton;
   private JRadioButton   directorButton;
   private JRadioButton   genreButton;
   private JRadioButton   awardButton;
   private JRadioButton   platformButton;
   private JRadioButton   gamesButton;
   private JRadioButton   moviesButton;

   private ButtonGroup  gamesOrMoviesButtonGroup;
   private ButtonGroup  moviesSelectionButtonGroup;
   private ButtonGroup  gamesSelectionButtonGroup;

   private JPanel         topPanel;
   private JPanel         loginPanel;
   private JPanel         queryPanel;
   private JPanel         radioButtonPanel;
   private JPanel         gamesOrMoviesPanel;

   private JTable         table;
   private JScrollPane    scroller;

   private Connection     connection;
   private AdminDialog    adminDialog;
   
   private DBHandler      dbHandler;

   Vector<Object>         userData;
   public DatabaseGui()
   {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      topPanel       = new JPanel();
      loginPanel     = new JPanel();
      idLabel        = new JLabel("User Id");
      pwdLabel       = new JLabel("Password");
      idField        = new JTextField(10);
      pwdField       = new JPasswordField(10);
      
      castButton     = new JRadioButton("cast");
      directorButton = new JRadioButton("director");
      genreButton    = new JRadioButton("genre");
      awardButton    = new JRadioButton("award");
      platformButton = new JRadioButton("platform");
      gamesButton    = new JRadioButton("games");
      moviesButton   = new JRadioButton("movies");

      gamesOrMoviesButtonGroup   = new ButtonGroup();

      castButton.setEnabled(true);
      directorButton.setEnabled(true);
      awardButton.setEnabled(true);
      platformButton.setEnabled(false);

      
      loginButton    = new JButton("Login");
      loginPanel.setLayout(new GridLayout(2,2,0,5));
      loginPanel.add(idLabel);
      loginPanel.add(idField);
      loginPanel.add(pwdLabel);
      loginPanel.add(pwdField);
      topPanel.add(loginPanel);
      topPanel.add(loginButton);
      add(topPanel,BorderLayout.NORTH);
      getRootPane().setDefaultButton(loginButton);
      loginButton.setActionCommand("LOGIN");
      loginButton.addActionListener(this);

      radioButtonPanel   = new JPanel();
      gamesOrMoviesPanel = new JPanel();

      radioButtonPanel.add(castButton);
      radioButtonPanel.add(directorButton);
      radioButtonPanel.add(genreButton);
      radioButtonPanel.add(platformButton);
      radioButtonPanel.add(awardButton);
      radioButtonPanel.setBorder(BorderFactory.createTitledBorder("Selection Options"));

      gamesOrMoviesButtonGroup.add(moviesButton);
      gamesOrMoviesButtonGroup.add(gamesButton);
      gamesOrMoviesPanel.add(gamesButton);
      gamesOrMoviesPanel.add(moviesButton);
      gamesOrMoviesPanel.setBorder(BorderFactory.createTitledBorder("Rental Options"));
      radioButtonPanel.add(gamesOrMoviesPanel);

      moviesButton.setActionCommand("MOVIES");
      gamesButton.setActionCommand("GAMES");
      moviesButton.addActionListener(this);
      gamesButton.addActionListener(this);

      queryPanel   = new JPanel();
      searchField  = new JTextField(30);
      searchButton = new JButton("Submit");

      searchButton.setActionCommand("SEARCH");
      searchButton.addActionListener(this);
      searchButton.setEnabled(false);
      searchField.setEnabled(false);
      queryPanel.setLayout(new BorderLayout());
      queryPanel.setBorder(BorderFactory.createTitledBorder("Search"));
      queryPanel.add(searchField,BorderLayout.CENTER);
      queryPanel.add(searchButton,BorderLayout.EAST);
      queryPanel.add(radioButtonPanel,BorderLayout.SOUTH);
      add(queryPanel,BorderLayout.CENTER);
      
      dbHandler = new DBHandler();


      this.setupMainFrame();

      searchButton.addActionListener(this);
      
      WindowHandler window  = new WindowHandler();
      this.addWindowListener(window);
   }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(500, 500);
    this.setMinimumSize(new Dimension(400, 500));
    this.setLocation(d.width/4, d.height/4);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("MOVIE'S R US");
    this.setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void actionPerformed(ActionEvent e)
{
   Statement         statement;
   ResultSet         resultSet;
   ResultSetMetaData metaData;
   PreparedStatement pstmt;

   if((e.getActionCommand().equals("MOVIES"))||(e.getActionCommand().equals("GAMES")))
   {
        buttonUpdater();
   }
   else if(e.getActionCommand().equals("LOGIN"))
   {
      String id     = idField.getText();
      char[] p      = pwdField.getPassword();
      String pwd    = new String(p);
      try 
      {
         connection = dbHandler.establishConnection();
         statement = connection.createStatement();
         resultSet = statement.executeQuery("Select * From User u where u.email = '" + id + "' AND user_password = '" + pwd+"';");
         if(!resultSet.next()) 
         {
            JOptionPane.showMessageDialog(null,"No records found!");
            return;
         }
         else
         {
            userData = new Vector<Object>();
            metaData = resultSet.getMetaData();
            for(int i = 1; i <= metaData.getColumnCount(); ++i)
            {
               userData.addElement(resultSet.getObject(i));
               System.out.println(metaData.getColumnName(i)+": "+resultSet.getObject(i));
            }
            if(userData.elementAt(2).toString().equals("1"))
               adminDialog = new AdminDialog(connection);
            loginButton.setEnabled(false);
            idLabel.setEnabled(false);
            pwdLabel.setEnabled(false);
            idField.setEnabled(false);
            pwdField.setEnabled(false);
            searchButton.setEnabled(true);
            searchField.setEnabled(true);
            getRootPane().setDefaultButton(searchButton);
         }//END of else 
      }// end of try
      catch(ClassNotFoundException ex) 
      {
         JOptionPane.showMessageDialog(null, "Failed to load JDBC driver!");
         System.exit(1);
      }
      catch(SQLException ex) 
      {
         JOptionPane.showMessageDialog(null, "Access denied!");
         return;
      }
   }//END IF LOGIN IF
   else if(e.getActionCommand().equals("SEARCH"))
   {
      
      String query;
      String queryInsert = searchField.getText();
      if(directorButton.isSelected())
      {
          query = dbHandler.acquireResults + dbHandler.directorSearch + ')';
          doQuery(query,queryInsert);
      }
      else if(castButton.isSelected())
      {
          query = dbHandler.acquireResults + dbHandler.castSearch + ')';
          doQuery(query,queryInsert);
      }
      else if(genreButton.isSelected())
      {
          query = dbHandler.acquireResults + dbHandler.movieGenreSearch + ')';
          doQuery(query,queryInsert);
      }
      else if(platformButton.isSelected())
      {
          query = dbHandler.acquireResults + dbHandler.platformSearch + ')';
          doQuery(query,queryInsert);
      }
      else if(awardButton.isSelected())
      {
          query = dbHandler.acquireResults + dbHandler.awardWinnerSearch + ')';
          doQuery(query,"");
      }
   }//END OF SEARCH ELSE
}//END OF ACTION PERFORMED
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void doQuery(String querytodo,String searchFieldText)
{
   ResultSet         doQueryresultSet;
   ResultSetMetaData doQuerymetaData;
   PreparedStatement pstmt;
  try 
  {
    pstmt            = connection.prepareStatement(querytodo);
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
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////            
void buttonUpdater()
{
    castButton.setSelected(false);
    directorButton.setSelected(false);
    awardButton.setSelected(false);
    platformButton.setSelected(false);
    genreButton.setSelected(false);
    System.out.println("Buttons are being updated.");
    if(moviesButton.isSelected())
    {
      castButton.setEnabled(true);
      directorButton.setEnabled(true);
      awardButton.setEnabled(true);
      platformButton.setEnabled(false);
    }
    else if (gamesButton.isSelected())
    {
      castButton.setEnabled(false);
      directorButton.setEnabled(false);
      awardButton.setEnabled(false);
      platformButton.setEnabled(true);   
    }
}        
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// inner class for handling window event
private class WindowHandler extends WindowAdapter
{
   public void windowClosing(WindowEvent e)
   {
      try 
      {
         if(connection!=null)
            connection.close();
      }
      catch(SQLException ex) 
      {
         JOptionPane.showMessageDialog(null, "Unable to disconnect!");
      }
      System.exit(0);
   }
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void main(String args[])
{ 
   DatabaseGui db = new DatabaseGui();
}
}//END OF CLASS