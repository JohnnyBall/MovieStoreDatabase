import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class CreateRentalDialog extends JDialog
                                 implements ActionListener
{
   private JRadioButton      gamesButton;
   private JRadioButton      moviesButton;
   private JButton           submitButton;

   private ButtonGroup       gamesOrMoviesButtonGroup;

   private JPanel            topPanel;
   private JPanel            buttonPanel;
   private JTable            table;
   private Connection        connection;
   private DBHandler         dbhandler;
   private ResultSet         resultSet;
   private ResultSetMetaData metaData;
   private PreparedStatement pstmt;
   private Statement         statement;

   
   private JLabel            titleLabel;
   private JLabel            amountLabel;
   private JLabel            releaseDateLabel;
   private JLabel            genreLabel;
   private JLabel            platformLabel;
   private JLabel            castMemberLabel;
   private JLabel            directorLabel;
   private JLabel            sequelLabel;
   private JLabel            awardsLabel;


   private JTextField        titleField;
   private JTextField        amountField;
   private JTextField        releaseDateField;
   private JTextField        genreField;
   private JTextField        platFormField;
   private JTextField        directorField;
   private JTextField        castMemberField;
   private JTextField        prequelField;
   private JTextField        awardsField;


   public CreateRentalDialog(Connection newConnection)
   {
      connection               = newConnection;
      topPanel                 = new JPanel();
      buttonPanel              = new JPanel();
      dbhandler                = new DBHandler();
      
      gamesButton              = new JRadioButton("games");
      moviesButton             = new JRadioButton("movies",true);
      
      gamesOrMoviesButtonGroup = new ButtonGroup();
      gamesOrMoviesButtonGroup.add(moviesButton);
      gamesOrMoviesButtonGroup.add(gamesButton);

      submitButton             = new JButton("Submit");

      submitButton.setActionCommand("SUBMIT");
      submitButton.addActionListener(this);


      buttonPanel.add(submitButton);
      buttonPanel.add(gamesButton);
      buttonPanel.add(moviesButton);
      buttonPanel.setBorder(BorderFactory.createTitledBorder("Rental type:"));

      moviesButton.setActionCommand("MOVIES");
      gamesButton.setActionCommand("GAMES");
      moviesButton.addActionListener(this);
      gamesButton.addActionListener(this);

      

      getRootPane().setDefaultButton(submitButton);
      
      
      titleLabel               = new JLabel("Title:");
      amountLabel              = new JLabel("Number of Available Copy's:");
      releaseDateLabel         = new JLabel("ReleaseDate: ");
      genreLabel               = new JLabel("Genre: ");
      platformLabel            = new JLabel("Platform: ");
      castMemberLabel          = new JLabel("CastMember(s): ");
      directorLabel            = new JLabel("Director: ");
      sequelLabel              = new JLabel("Sequal: ");
      awardsLabel              = new JLabel("Awards Won: ");
      
      
      titleField               = new JTextField();
      amountField              = new JTextField();
      releaseDateField         = new JTextField();
      genreField               = new JTextField();
      platFormField            = new JTextField();
      directorField            = new JTextField();
      castMemberField          = new JTextField();
      prequelField              = new JTextField();
      awardsField              = new JTextField();

      topPanel.setLayout(new GridLayout(18,2,0,5));
      
      topPanel.add(titleLabel);
      topPanel.add(titleField);
      
      topPanel.add(amountLabel);
      topPanel.add(amountField);
      
      topPanel.add(releaseDateLabel);
      topPanel.add(releaseDateField);

      topPanel.add(genreLabel);
      topPanel.add(genreField);

      topPanel.add(platformLabel);
      topPanel.add(platFormField);
      
      topPanel.add(directorLabel);
      topPanel.add(directorField);
      
      topPanel.add(castMemberLabel);
      topPanel.add(castMemberField);

      topPanel.add(sequelLabel);
      topPanel.add(prequelField);

      topPanel.add(awardsLabel);
      topPanel.add(awardsField);

      add(topPanel,BorderLayout.CENTER);      
      
      add(buttonPanel,BorderLayout.NORTH);

      getRootPane().setDefaultButton(submitButton);
      this.setupMainFrame();
      fieldUpdater();
      
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
    setTitle("Add Rental");
    setVisible(true);
  }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public void actionPerformed(ActionEvent e)
{  

  if((e.getActionCommand().equals("MOVIES"))||(e.getActionCommand().equals("GAMES")))
  {
    fieldUpdater();
  }
  else if(e.getActionCommand().equals("SUBMIT"))
  {
    if (gamesButton.isSelected() && !(titleField.getText().trim().equals("")) && !(amountField.getText().trim().equals("")) && !(releaseDateField.getText().trim().equals("")) && !(genreField.getText().trim().equals("")) && !(platFormField.getText().trim().equals("")))
    {
      try
      {
        Integer.parseInt(amountField.getText().trim());
        createRentalQueryExecuter();
      }
      catch (NumberFormatException nfe)
      {
        JOptionPane.showMessageDialog(this,"Please make sure data in either the zip field or the quotaField is an integer!","RIP.",JOptionPane.WARNING_MESSAGE);
      }
    }
    else if(moviesButton.isSelected() && !(titleField.getText().trim().equals("")) && !(amountField.getText().trim().equals("")) && !(releaseDateField.getText().trim().equals("")) && !(genreField.getText().trim().equals("")) && !(directorField.getText().trim().equals("")))
    {
      try
      {
        Integer.parseInt(amountField.getText().trim());
        createRentalQueryExecuter();
      }
      catch (NumberFormatException nfe)
      {
        JOptionPane.showMessageDialog(this,"Please make sure data in either the zip field or the quotaField is an integer!","RIP.",JOptionPane.WARNING_MESSAGE);
      }
    }
    else
    {
      JOptionPane.showMessageDialog(this,"Please make sure data is entered in every Title, Amount, releaseDate.","RIP.",JOptionPane.WARNING_MESSAGE);
    }
      //Values in editInfo should be ready to send in a transaction to update userInfo
  }
}//end of action performed

void fieldUpdater()
{
    if(moviesButton.isSelected())
    {
      castMemberField.setEnabled(true);
      directorField.setEnabled(true);
      prequelField.setEnabled(true);
      awardsField.setEnabled(true);
      platFormField.setEnabled(false);
    }
    else if (gamesButton.isSelected())
    {
      castMemberField.setEnabled(false);
      directorField.setEnabled(false);
      prequelField.setEnabled(false);
      awardsField.setEnabled(false);
      platFormField.setEnabled(true);
    }
}     
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
void createRentalQueryExecuter()
{
  int maxRID = 0;
  try 
  {
//-------------------------------------------------------------------------------------------------------------------------------
    // Retrieves the max pid from the Person table, then sets our max pid to it + 1 for use later on. 
    statement = connection.createStatement();
    resultSet = statement.executeQuery("SELECT MAX(RID) FROM Rentals");
    System.out.println("statement: " + statement.toString());
    if(!resultSet.next()) 
    {
      JOptionPane.showMessageDialog(null,"No records found!");
      return;
    }
    else
    {
     maxRID = resultSet.getInt(1) + 1;
    }
    statement.close();

//-------------------------------------------------------------------------------------------------------------------------------
    //
    //pstmt = connection.prepareStatement("INSERT rentals (rid,title,releaseDate,num_availible_copys) VALUES (?, ?, ?, ?);");////////////////////////////////////////FIX 
    pstmt = connection.prepareStatement("INSERT rentals (rid, title, releaseDate, num_availible_copys) VALUES (?, ?,CURDATE(), ?);");////////////////////////////////////////FIX
    pstmt.setInt(1,maxRID);
    pstmt.setString(2, titleField.getText().trim());//pname
    pstmt.setInt(3, Integer.parseInt(amountField.getText().trim()));// sets amountField  of available copys int
    //pstmt.setDate(3, new Date(releaseDateField.getText().trim()))///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    System.out.println("titleField "+ titleField.getText().trim());
    System.out.println("pstmt: " + pstmt.toString());
    System.out.println("About to Execute RENTAL INSERT");
    pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    // 
    pstmt.clearParameters();
    pstmt = connection.prepareStatement("INSERT belongs_to_genre(gName, rid)  VALUES(?, ?);");
    pstmt.setString(1, genreField.getText().trim());//Sets the Genre name from the text field
    pstmt.setInt(2,maxRID);
    System.out.println("pstmt: " + pstmt.toString());
    System.out.println("About to Execute GENRE INSERT");
    pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    if(moviesButton.isSelected())
    {
//-------------------------------------------------------------------------------------------------------------------------------
       // 
        pstmt.clearParameters();
        pstmt = connection.prepareStatement("INSERT movie (rid, pid, rid_of_prequel) VALUES (?, ?, ?);");
        pstmt.setInt(1,maxRID);
        pstmt.setInt(2, Integer.parseInt(directorField.getText().trim()));//SETS DIRECTOR FROM DIRECTORS PID, maybe NEED TO FIX TO MAKE IT ACTUALLY LOOK UP THE DIRECTOR NAME?????????

        if(!prequelField.getText().trim().equals(""))//checks for data in the prequel field
          pstmt.setInt(3,Integer.parseInt(prequelField.getText().trim()));//SETS PREQUEL FROM prequelField RID,  maybe NEED TO FIX TO MAKE IT ACTUALLY LOOK UP THE MOVIE?????????
        else
          pstmt.setNull(3,Types.INTEGER);//SETS PREQUEL FROM prequelField RID to null if left blank!

        System.out.println("pstmt: " + pstmt.toString());
        System.out.println("About to Execute INSERT movie");
        pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
       // AND HERES WHERE  I WOULD PUT MY CAST MEMBERS, IF I HAD ANY!!!!!!!!!!!!!!!!!!!!!! 
        if(!castMemberField.getText().trim().equals(""))// checks to see if castMemberField is blank
        {
          pstmt.clearParameters();
          pstmt = connection.prepareStatement("INSERT was_in(pid,rid) VALUES (?, ?);");
          pstmt.setInt(1,maxRID);
          pstmt.setInt(2, Integer.parseInt(castMemberField.getText().trim()));///////////////////////////////////////MAY NEED TO PARSE AND STRING/loop and choop
          System.out.println("pstmt: " + pstmt.toString());
          System.out.println("About to ExecuteINSERT movie");
          pstmt.execute();
       }
//-------------------------------------------------------------------------------------------------------------------------------
        //
        if(!awardsField.getText().trim().equals(""))// checks to see if award textfield is blank
        {
          pstmt.clearParameters();
          pstmt = connection.prepareStatement("INSERT has_won_award(rid, aTitle) VALUES (?, ?);");
          pstmt.setInt(1, maxRID);// Sets pid for new person value
          pstmt.setString(2, awardsField.getText().trim());//sets award/////////////////////////////////////MAY NEED TO PARSE AND STRING/loop and choop
          System.out.println("pstmt: " + pstmt.toString());
          System.out.println("About to Execute INSERT has_won_award");
          pstmt.execute();
        }
//-------------------------------------------------------------------------------------------------------------------------------
    }
    else if(gamesButton.isSelected())
    {    
//-------------------------------------------------------------------------------------------------------------------------------
       // 
        pstmt.clearParameters();
        pstmt = connection.prepareStatement("INSERT game (rid) VALUE (?);");
        pstmt.setInt(1,maxRID);
        System.out.println("pstmt: " + pstmt.toString());
        System.out.println("About to Execute INSERT movie");
        pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
      // 
        pstmt.clearParameters();
        pstmt = connection.prepareStatement("INSERT plays_on_platform (rid, platName) VALUE (?, ?);");
        pstmt.setInt(1,maxRID);
        pstmt.setString(2, platFormField.getText().trim());//sets platform/////////////////////////////////////MAY NEED TO PARSE AND STRING/loop and choop
        System.out.println("pstmt: " + pstmt.toString());
        System.out.println("About to Execute INSERT movie");
        pstmt.execute();
//-------------------------------------------------------------------------------------------------------------------------------
    }
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
}// END OF createRentalQueryExecuter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}//END OF CLASS