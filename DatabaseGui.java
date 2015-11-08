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
   private JRadioButton   genreButton;
   private JRadioButton   awardButton;
   private JRadioButton   platformButton;

   private JPanel         loginPanel;
   private JPanel         queryPanel;
   private JPanel         radioButtonPanel;

   private JTable         table;
   private JScrollPane    scroller;

   private Connection connection;

   public DatabaseGui()
   {
      setLayout(new FlowLayout());

      loginPanel     = new JPanel();
      idLabel        = new JLabel("User Id");
      pwdLabel       = new JLabel("Password");
      idField        = new JTextField(10);
      pwdField       = new JPasswordField(10);
      
      castButton     = new JRadioButton("cast");
      genreButton    = new JRadioButton("genre");
      awardButton    = new JRadioButton("award");
      platformButton = new JRadioButton("platform");
      
      loginButton    = new JButton("Login");
      loginPanel.setLayout(new GridLayout(2,2,0,5));
      loginPanel.add(idLabel);
      loginPanel.add(idField);
      loginPanel.add(pwdLabel);
      loginPanel.add(pwdField);
      add(loginPanel);
      add(loginButton);
      getRootPane().setDefaultButton(loginButton);
      loginButton.setActionCommand("LOGIN");
      loginButton.addActionListener(this);

      queryPanel   = new JPanel();
      searchField  = new JTextField(30);
      searchButton = new JButton("Submit");
      searchButton.setActionCommand("SEARCH");
      searchButton.setEnabled(false);
      searchField.setEnabled(false);
      queryPanel.setLayout(new BoxLayout(queryPanel,BoxLayout.X_AXIS));
      queryPanel.setBorder(BorderFactory.createTitledBorder("Search"));
      queryPanel.add(searchField);
      queryPanel.add(searchButton);

      add(queryPanel);
      radioButtonPanel = new JPanel();
      radioButtonPanel.add(castButton);
      radioButtonPanel.add(genreButton);
      radioButtonPanel.add(platformButton);
      radioButtonPanel.add(awardButton);
      add(radioButtonPanel);

      this.setupMainFrame();

      searchButton.addActionListener(this);
      
      WindowHandler window  = new WindowHandler();
      this.addWindowListener(window);
   }

  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(400, 500);
    this.setMinimumSize(new Dimension(500, 500));
    this.setLocation(d.width/4, d.height/4);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("MOVIE'S R US");
    this.setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void actionPerformed(ActionEvent e)
{
   if(e.getActionCommand().equals("LOGIN"))
   {
      String id     = idField.getText();
      char[] p      = pwdField.getPassword();
      String pwd    = new String(p);
      String url    = "jdbc:mysql://localhost:3306/moviestore";
      try 
      {
         Class.forName( "com.mysql.jdbc.Driver" );
         connection = DriverManager.getConnection(url, "root", "141305" );
         loginButton.setEnabled(false);
         idLabel.setEnabled(false);
         pwdLabel.setEnabled(false);
         idField.setEnabled(false);
         pwdField.setEnabled(false);
         searchButton.setEnabled(true);
         searchField.setEnabled(true);
         getRootPane().setDefaultButton(searchButton);
      }
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
      String query = searchField.getText();
      Statement statement;
      ResultSet resultSet;
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
            ResultSetMetaData metaData = resultSet.getMetaData();
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
            //table.doLayout();
            scroller = new JScrollPane(table);
            getContentPane().add(scroller);
            validate();
         }
         statement.close();
      }
      catch(SQLException ex) 
      {
       JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
      }
   }//END OF SEARCH ELSE
}//END OF ACTION PERFORMED        
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

public static void main(String args[])
{ 
   DatabaseGui db = new DatabaseGui();
}
}//END OF CLASS