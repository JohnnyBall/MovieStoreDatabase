// This program displays the results of a query on a database
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DatabaseGui extends JFrame
                                 implements ActionListener
{
   private JLabel           idLabel;
   private JLabel           pwdLabel;
   private JTextField       idField;
   private JPasswordField   pwdField;
   private JTextField       searchField;

   private JButton          loginButton;
   private JButton          searchButton;
   private JButton          userInfoButton;
   private JButton          sequelButton;
   private JButton          displayAwardsButton;
   private JButton          adminButton;
   private JButton          rentButton;
   private JButton          logoutButton;

   private JRadioButton     castButton;
   private JRadioButton     directorButton;
   private JRadioButton     genreButton;
   private JRadioButton     awardButton;
   private JRadioButton     platformButton;
   private JRadioButton     gamesButton;
   private JRadioButton     moviesButton;
   private JRadioButton     keywordsButton;
   private JRadioButton     dontShowRentedBeforeButton;

   private ButtonGroup      gamesOrMoviesButtonGroup;
   private ButtonGroup      moviesSelectionButtonGroup;
   private ButtonGroup      gamesSelectionButtonGroup;

   private JPanel           topPanel;
   private JPanel           loginPanel;
   private JPanel           queryPanel;
   private JPanel           radioButtonPanel;
   private JPanel           gamesOrMoviesPanel;

   private JTable           table;
   private JScrollPane      scroller;

   private Connection       connection;
   private AdminDialog      adminDialog;
   private UserInfoDialog   userdialog;
   private CreateUserDialog createuserdialog;
   
   private DBHandler        dbHandler;

   Vector<Object>           userData;
   public DatabaseGui()
   {
      setLayout(new FlowLayout(FlowLayout.CENTER));
      topPanel                   = new JPanel();
      loginPanel                 = new JPanel();
      idLabel                    = new JLabel("User Id");
      pwdLabel                   = new JLabel("Password");
      idField                    = new JTextField(10);
      pwdField                   = new JPasswordField(10);
      
      castButton                 = new JRadioButton("cast");
      directorButton             = new JRadioButton("director");
      genreButton                = new JRadioButton("genre");
      awardButton                = new JRadioButton("award");
      platformButton             = new JRadioButton("platform");
      gamesButton                = new JRadioButton("games");
      moviesButton               = new JRadioButton("movies",true);
      keywordsButton             = new JRadioButton("keywords");
      dontShowRentedBeforeButton = new JRadioButton("no previous rentals");
      
      gamesOrMoviesButtonGroup   = new ButtonGroup();
      gamesSelectionButtonGroup  = new ButtonGroup();
      moviesSelectionButtonGroup = new ButtonGroup();
      
      userInfoButton             = new JButton("Create User");
      rentButton                 = new JButton("Rent");
      loginButton                = new JButton("Login");
      sequelButton               = new JButton("Display Sequels");
      displayAwardsButton        = new JButton("Display Awards");
      adminButton                = new JButton("Admin Info");
      logoutButton               = new JButton("Logout");
      buttonUpdater();
      sequelButton.setEnabled(false);
      rentButton.setEnabled(false);   
      displayAwardsButton.setEnabled(false);  
      userInfoButton.setEnabled(false);
      loginPanel.setLayout(new GridLayout(2,2,0,5));
      loginPanel.add(idLabel);
      loginPanel.add(idField);
      loginPanel.add(pwdLabel);
      loginPanel.add(pwdField);
      topPanel.add(loginPanel);
      topPanel.add(loginButton);
      topPanel.add(logoutButton);
      topPanel.add(userInfoButton);
      topPanel.add(sequelButton);
      topPanel.add(displayAwardsButton);
      topPanel.add(rentButton);
      topPanel.add(adminButton);
      add(topPanel,BorderLayout.NORTH);
      getRootPane().setDefaultButton(loginButton);
      loginButton.setActionCommand("LOGIN");
      loginButton.addActionListener(this);
      userInfoButton.setActionCommand("USERINFO");
      userInfoButton.addActionListener(this);
      sequelButton.setActionCommand("SEQUEL");
      sequelButton.addActionListener(this);
      displayAwardsButton.setActionCommand("AWARDS");
      displayAwardsButton.addActionListener(this);
      adminButton.setActionCommand("ADMIN");
      adminButton.addActionListener(this);
      rentButton.setActionCommand("RENT");
      rentButton.addActionListener(this);
      logoutButton.setActionCommand("LOGOUT");
      logoutButton.addActionListener(this);
      logoutButton.setEnabled(false);

      adminButton.setVisible(false);

      radioButtonPanel   = new JPanel();
      gamesOrMoviesPanel = new JPanel();

      gamesSelectionButtonGroup.add(platformButton);

      moviesSelectionButtonGroup.add(genreButton);
      moviesSelectionButtonGroup.add(keywordsButton);
      moviesSelectionButtonGroup.add(castButton);
      moviesSelectionButtonGroup.add(directorButton);
      moviesSelectionButtonGroup.add(awardButton);

      radioButtonPanel.add(castButton);
      radioButtonPanel.add(directorButton);
      radioButtonPanel.add(genreButton);
      radioButtonPanel.add(platformButton);
      radioButtonPanel.add(awardButton);
      radioButtonPanel.add(keywordsButton);
      radioButtonPanel.setBorder(BorderFactory.createTitledBorder("Selection Options"));

      gamesOrMoviesButtonGroup.add(moviesButton);
      gamesOrMoviesButtonGroup.add(gamesButton);

      gamesOrMoviesPanel.add(gamesButton);
      gamesOrMoviesPanel.add(moviesButton);
      gamesOrMoviesPanel.add(dontShowRentedBeforeButton);
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
      
      this.setupMainFrame();
      dbHandler            = new DBHandler();
      WindowHandler window = new WindowHandler();
      this.addWindowListener(window);
   }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public void setupMainFrame()
  {
    Toolkit   tk = Toolkit.getDefaultToolkit();
    Dimension d  = tk.getScreenSize();
    this.setSize(900, 500);
    this.setMinimumSize(new Dimension(900, 500));
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

   if((e.getActionCommand().equals("MOVIES"))||(e.getActionCommand().equals("GAMES")))
   {
        buttonUpdater();
   }
   else if((e.getActionCommand().equals("ADMIN")))
   {
        adminDialog.setVisible(true);
   }
   else if((e.getActionCommand().equals("LOGOUT")))
   {
      userInfoButton.setText("Create User");
      loginButton.setEnabled(true);
      idLabel.setEnabled(true);
      pwdLabel.setEnabled(true);
      idField.setEnabled(true);
      pwdField.setEnabled(true);
      searchButton.setEnabled(false);
      searchField.setEnabled(false);
      userInfoButton.setEnabled(false);
      logoutButton.setEnabled(false);
      rentButton.setEnabled(false);  
      getRootPane().setDefaultButton(loginButton);
      adminButton.setVisible(false);
      userData = new Vector<Object>();
      userdialog = new UserInfoDialog(connection,userData);
      userdialog.setVisible(false);
   }
   else if(e.getActionCommand().equals("LOGIN"))
   {
      String id     = idField.getText();
      char[] p      = pwdField.getPassword();
      String pwd    = new String(p);
      try 
      {
         connection = dbHandler.establishConnection();
         statement  = connection.createStatement();
         resultSet  = statement.executeQuery("Select * From User u where u.email = '" + id + "' AND user_password = '" + pwd+"';");
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
               userData.add((i-1),resultSet.getObject(i));
               System.out.println(metaData.getColumnLabel(i)+": "+resultSet.getObject(i));
            }
            if(userData.elementAt(2).toString().equals("1"))
            {
               adminDialog = new AdminDialog(connection);
               adminDialog.setVisible(false);
               adminButton.setVisible(true);
            }

            userdialog = new UserInfoDialog(connection,userData);
            userdialog.setVisible(false);
            loginButton.setEnabled(false);
            idLabel.setEnabled(false);
            pwdLabel.setEnabled(false);
            idField.setEnabled(false);
            pwdField.setEnabled(false);
            searchButton.setEnabled(true);
            searchField.setEnabled(true);
            userInfoButton.setEnabled(true);
            userInfoButton.setText("User Info");
            logoutButton.setEnabled(true);
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
   else if(e.getActionCommand().equals("USERINFO"))
   { 
        userdialog.setVisible(true);
        userdialog.refreshButton.doClick();
   }
   else if(e.getActionCommand().equals("SEARCH"))
   {   
      String query;
      if(moviesButton.isSelected())
      {
        if(directorButton.isSelected())
        {
            if(dontShowRentedBeforeButton.isSelected())
            {
                query = "CREATE OR REPLACE view  search as "+"select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.directorSearch+") as r2 on r2.rid = rip.rid" + ';';
                myexecuteQuery(query,searchField.getText(),2);
                query = dbHandler.acquireResults2ElectricBugalooMovies;
                doQuery(query);
            }
            else
            {
               query =  "CREATE OR REPLACE view  search as "+dbHandler.directorSearch + ';';
               myexecuteQuery(query,searchField.getText(),1);
               query = dbHandler.acquireResults2ElectricBugalooMovies;
               doQuery(query);
            }
        }
        else if(castButton.isSelected())
        {   
            if(dontShowRentedBeforeButton.isSelected())
            {
                query = "CREATE OR REPLACE view  search as  "+"select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.castSearch+") as r2 on r2.rid = rip.rid" + ';';
                myexecuteQuery(query,searchField.getText(),2);
                query = dbHandler.acquireResults2ElectricBugalooMovies;
                doQuery(query);
            }
            else
            {
              query = "CREATE OR REPLACE view  search as  "+ dbHandler.castSearch + ';';
              myexecuteQuery(query,searchField.getText(),1);
              query = dbHandler.acquireResults2ElectricBugalooMovies;
              doQuery(query);
            }
        }
        else if(genreButton.isSelected())
        {   if(dontShowRentedBeforeButton.isSelected())
            {
                query = "CREATE OR REPLACE view  search as  "+ "select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.movieGenreSearch+") as r2 on r2.rid = rip.rid" + ';';
                myexecuteQuery(query,searchField.getText(),2);
                query = dbHandler.acquireResults2ElectricBugalooMovies;
                doQuery(query);
            }   
            else
            {
                query = "CREATE OR REPLACE view  search as  "+ dbHandler.movieGenreSearch + ';';
                myexecuteQuery(query,searchField.getText(),1);
                query = dbHandler.acquireResults2ElectricBugalooMovies;
                doQuery(query);

            }
        }
        else if(awardButton.isSelected())
        {            
            if(dontShowRentedBeforeButton.isSelected())
            {
               query = "CREATE OR REPLACE view  search as  "+ "select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.awardWinnerSearch+") as r2 on r2.rid = rip.rid" + ';';
               myexecuteQuery(query,searchField.getText(),1);
               query = dbHandler.acquireResults2ElectricBugalooMovies;
               doQuery(query);
            }
            else
            {
              query = "CREATE OR REPLACE view  search as  "+ dbHandler.awardWinnerSearch + ';';
              myexecuteQuery(query);
              query = dbHandler.acquireResults2ElectricBugalooMovies;
              doQuery(query);
            }
        }
        else if(keywordsButton.isSelected())
        {            
            if(dontShowRentedBeforeButton.isSelected())
            {
                query = "CREATE OR REPLACE view  search as  "+ "select rip.rid from (("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.keywordMovieSearch+") as r2 on r2.rid = rip.rid)" + ';';
                myexecuteQuery(query,searchField.getText(),5);
                query = dbHandler.acquireResults2ElectricBugalooMovies;
                doQuery(query);
            }
            else
            {
              query = "CREATE OR REPLACE view  search as  "+  dbHandler.keywordMovieSearch + ';';
              myexecuteQuery(query,searchField.getText(),4);
              query = dbHandler.acquireResults2ElectricBugalooMovies;
              doQuery(query);
            }
        }
      }// end moviesButton.isSelected()
      else if(gamesButton.isSelected())
      {
        if(platformButton.isSelected())
        {            
            if(dontShowRentedBeforeButton.isSelected())
            {  
                query = "CREATE OR REPLACE view  search as  "+ "select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.platformSearch+") as r2 on r2.rid = rip.rid" + ';';
                myexecuteQuery(query,searchField.getText(),2);
                query = dbHandler.acquireResults2ElectricBugalooGames;
                doQuery(query);
            }
            else
            {
              query = "CREATE OR REPLACE view  search as  "+dbHandler.platformSearch + ';';
              myexecuteQuery(query,searchField.getText(),1);
              query = dbHandler.acquireResults2ElectricBugalooGames;
              doQuery(query);
            }
        }
        else if(genreButton.isSelected())
        {            
            if(dontShowRentedBeforeButton.isSelected())
            {
                query = "CREATE OR REPLACE view  search as  "+"select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.gameGenreSearch+") as r2 on r2.rid = rip.rid" + ';';
                myexecuteQuery(query,searchField.getText(),2);
                query = dbHandler.acquireResults2ElectricBugalooGames;
                doQuery(query);
            }
            else
            {
              query = "CREATE OR REPLACE view  search as  "+ dbHandler.gameGenreSearch + ';';
              myexecuteQuery(query,searchField.getText(),1);
              query = dbHandler.acquireResults2ElectricBugalooGames;
              doQuery(query);
            }
        }
        else if(keywordsButton.isSelected())
        {            
            if(dontShowRentedBeforeButton.isSelected())
            {
                query = "CREATE OR REPLACE view  search as "+ "select rip.rid from ("+dbHandler.notRentedSearch +" ) as rip INNER JOIN (" + dbHandler.keywordGameSearch+") as r2 on r2.rid = rip.rid" + ';';
                myexecuteQuery(query,searchField.getText(),4);
                query = dbHandler.acquireResults2ElectricBugalooGames;
                doQuery(query);
            }
            else
            {
              query = "CREATE OR REPLACE view  search as "+ dbHandler.keywordGameSearch + ';';
              myexecuteQuery(query,searchField.getText(),3);
              query = dbHandler.acquireResults2ElectricBugalooGames;
              doQuery(query);
            }
        }
      }//end of gamesButton.isSelected()
   }//END OF SEARCH ELSE
   else if(e.getActionCommand().equals("SEQUEL"))
   {
       int               ridHolder;
       String            query;
       PreparedStatement pstmt;
       
       if(table.getSelectedRow() == -1)
       {
        JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
       }
       else
       {
       ridHolder = (int)table.getValueAt(table.getSelectedRow(), 0);
       query = dbHandler.sequelSearch;
       System.out.println(query + " " + ridHolder);
       try
       {
            pstmt = connection.prepareStatement(query);
            pstmt.clearParameters();
            pstmt.setInt(1, ridHolder);
       
            resultSet = pstmt.executeQuery();
            System.out.println("HERE");
            if(!resultSet.next()) 
            {
                JOptionPane.showMessageDialog(null,"No records found!");
                return;
            }
            else
            {
                ridHolder = (int)resultSet.getObject(1);
            }
            pstmt.close();
            
            query = dbHandler.acquireResults2ElectricBugalooSequels;
            pstmt = connection.prepareStatement(query);
            pstmt.clearParameters();
            for(int i = 1; i <= 4; ++i)
                pstmt.setInt(i, ridHolder);
            
            resultSet = pstmt.executeQuery();
            System.out.println("OR HERE " + ridHolder);
       
            if(!resultSet.next()) 
            {
                JOptionPane.showMessageDialog(null,"No records found!");
                return;
            }
            else
            {
                Vector<Object> columnNames = new Vector<Object>();
                Vector<Object> rows        = new Vector<Object>();
                metaData                   = resultSet.getMetaData();
                
                for(int i = 1; i <= metaData.getColumnCount(); ++i)
                    columnNames.addElement(metaData.getColumnLabel(i));
                
                do
                {
                    Vector<Object> currentRow = new Vector<Object>();
                    for(int i = 1; i <= metaData.getColumnCount(); ++i)
                    {
                        currentRow.addElement(resultSet.getObject(i));
                        
                        if (i == 1)
                            ridHolder = (int)resultSet.getObject(i);
                    }
                    rows.addElement(currentRow);
                    pstmt.close();
                    pstmt = connection.prepareStatement(dbHandler.sequelSearch);
                    pstmt.clearParameters();
                    System.out.println(ridHolder);
                    pstmt.setInt(1, ridHolder);
                    resultSet = pstmt.executeQuery();
                    
                    if(resultSet.next()) 
                        ridHolder = (int)resultSet.getObject(1);
                    else
                        ridHolder = 999;
                  
                    pstmt.close();
                    
                    pstmt = connection.prepareStatement(query);
                    pstmt.clearParameters();
                    for(int i = 1; i <= 4; ++i)
                        pstmt.setInt(i, ridHolder);
                    
                    resultSet = pstmt.executeQuery();
                }
                while(resultSet.next());
                
                if(scroller != null)
                    getContentPane().remove(scroller);
                
                sequelButton.setEnabled(false);
                displayAwardsButton.setEnabled(false);
                table = new JTable(rows, columnNames);
                table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
                {
                    public void valueChanged(ListSelectionEvent event)
                    {
                        if(!gamesButton.isSelected())
                        {
                            sequelButton.setEnabled(true);
                            displayAwardsButton.setEnabled(true);
                        }
                    }
                });
                table.setPreferredScrollableViewportSize(new Dimension(this.getWidth()-20, 10*table.getRowHeight()));
                scroller = new JScrollPane(table);
                getContentPane().add(scroller,BorderLayout.SOUTH);
                validate();
            }
            pstmt.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
        }
       }
   }
   else if(e.getActionCommand().equals("AWARDS"))
   {
       int ridHolder;
       String titleHolder;
       
       if(table.getSelectedRow() == -1)
       {
        JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
       }
       else
       {
       ridHolder = (int)table.getValueAt(table.getSelectedRow(), 0);
       titleHolder = (String)table.getValueAt(table.getSelectedRow(), 1);
       
       new AwardDialog(connection, titleHolder, ridHolder);
       }
   }
   else if((e.getActionCommand().equals("RENT")))
   {
       int               ridHolder;
       int               maxTrackingNumber;
       String            titleHolder;
       PreparedStatement pstmt;
       try 
       {
         if(table.getSelectedRow() == -1)
         {
            JOptionPane.showMessageDialog(null,"Nothing seems to be selected!");
         }
         else
         {
           statement = connection.createStatement();
           resultSet = statement.executeQuery("SELECT MAX(Trackingnum) FROM Rentals_Record_Rents");
           System.out.println("statement: " +statement.toString());
           if(!resultSet.next()) 
           {
             JOptionPane.showMessageDialog(null,"No records found!");
             return;
           }
           else
           {
             maxTrackingNumber = resultSet.getInt(1) + 1;
           }
           statement.close();
           //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
           ridHolder   = (int)table.getValueAt(table.getSelectedRow(), 0);
           titleHolder = (String)table.getValueAt(table.getSelectedRow(), 1);
           pstmt       = connection.prepareStatement("INSERT Rentals_Record_Rents(rid,pid,Trackingnum,from_Date) VALUES(?, ?, ?, CURDATE());");
           pstmt.setInt(1, ridHolder);
           pstmt.setInt(2, (int)userData.elementAt(0));
           pstmt.setInt(3, maxTrackingNumber);
           System.out.println("pstmt: " + pstmt.toString());
           System.out.println("About to UPDATE on rentals_record_rents RENTING UR THING");
           pstmt.execute();
           JOptionPane.showMessageDialog(null, "The rental you selected has been returned!", "Well thats pretty neat!", JOptionPane.INFORMATION_MESSAGE);
           pstmt.close();
           System.out.println("DONE!");
          }
        }
        catch(SQLException ex) 
        {
          System.out.println(ex.getMessage());
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
        }
   }
}//END OF ACTION PERFORMED
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void myexecuteQuery(String querytodo,String searchFieldText,int count)
{
   PreparedStatement pstmt;
   int j = 1;
  try 
  {

    pstmt = connection.prepareStatement(querytodo);
    System.out.println(" in myexecuteQuery!!!!!");
    System.out.println("querytodo: "+ querytodo);
    System.out.println("searchFieldText: "+searchFieldText);
    pstmt.clearParameters();

    if (dontShowRentedBeforeButton.isSelected())
    {
        pstmt.setString(1, userData.elementAt(0).toString());
        j++;
    }
    while(j <=count)
    {
        pstmt.setString(j, '%'+searchFieldText+'%');
        j++;
    }

    pstmt.executeUpdate();
    System.out.println("About to Execute");

    pstmt.close();

    System.out.println(" LEAVING myexecuteQuery!!!!!");
  }//end of try
  catch(SQLException ex) 
  {
   JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
}// END OF DO QUERY
void myexecuteQuery(String querytodo)
{
   PreparedStatement pstmt;

  try 
  {

    pstmt = connection.prepareStatement(querytodo);
    System.out.println(" in myexecuteQuery!!!!!");
    System.out.println("querytodo: "+ querytodo);
    pstmt.clearParameters();
    pstmt.executeUpdate();
    System.out.println("About to Execute");

    pstmt.close();

    System.out.println(" LEAVING myexecuteQuery!!!!!");
  }//end of try
  catch(SQLException ex) 
  {
   JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
  }
}// END OF DO QUERY  
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void doQuery(String querytodo,String searchFieldText,int count)
{
   ResultSet         doQueryresultSet;
   ResultSetMetaData doQuerymetaData;
   PreparedStatement pstmt;
   int j = 1;
  try 
  {

    pstmt = connection.prepareStatement(querytodo);
    System.out.println("querytodo: "+ querytodo);
    System.out.println("searchFieldText: "+searchFieldText);
    pstmt.clearParameters();

    if (dontShowRentedBeforeButton.isSelected())
    {
        pstmt.setString(1, userData.elementAt(0).toString());
        j++;
    }
    while(j <=count)
    {
        pstmt.setString(j, '%'+searchFieldText+'%');
        j++;
    }

    doQueryresultSet = pstmt.executeQuery();
    System.out.println("About to Execute");
    if(awardButton.isSelected())
    {
        pstmt.clearParameters();
    }
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

      sequelButton.setEnabled(false);
      displayAwardsButton.setEnabled(false);
      rentButton.setEnabled(false); 
      table = new JTable(rows, columnNames);
      table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
      {
          public void valueChanged(ListSelectionEvent event)
          {
             if(!gamesButton.isSelected())
              {
                sequelButton.setEnabled(true);
                displayAwardsButton.setEnabled(true); 
              }
              rentButton.setEnabled(true); 
          }
      });
      table.setPreferredScrollableViewportSize(new Dimension(this.getWidth()-20, 10*table.getRowHeight()));
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
void doQuery(String query)
{
  Statement         statement;
  ResultSet         resultSet;
  ResultSetMetaData metaData;
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
      sequelButton.setEnabled(false);
      displayAwardsButton.setEnabled(false);
      rentButton.setEnabled(false);  
      table = new JTable(rows, columnNames);
      table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
      {
          public void valueChanged(ListSelectionEvent event)
          {
              if(!gamesButton.isSelected())
              {
                sequelButton.setEnabled(true);
                displayAwardsButton.setEnabled(true); 
              }
              rentButton.setEnabled(true); 
          }
      });
      table.setPreferredScrollableViewportSize(new Dimension(this.getWidth()-20, 10*table.getRowHeight()));
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

void buttonUpdater()
{
    castButton.setSelected(false);
    directorButton.setSelected(false);
    awardButton.setSelected(false);
    platformButton.setSelected(false);
    genreButton.setSelected(false);
    keywordsButton.setSelected(true);
    if(moviesButton.isSelected())
    {
      castButton.setEnabled(true);
      directorButton.setEnabled(true);
      awardButton.setEnabled(true);
      platformButton.setEnabled(false);
      gamesSelectionButtonGroup.remove(genreButton);
      gamesSelectionButtonGroup.remove(keywordsButton);
      moviesSelectionButtonGroup.add(genreButton);
      moviesSelectionButtonGroup.add(keywordsButton);
    }
    else if (gamesButton.isSelected())
    {
      moviesSelectionButtonGroup.remove(genreButton);
      moviesSelectionButtonGroup.remove(keywordsButton);
      gamesSelectionButtonGroup.add(genreButton);
      gamesSelectionButtonGroup.add(keywordsButton);
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