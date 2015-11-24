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
   private ResultSet         doQueryresultSet;
   private ResultSetMetaData doQuerymetaData;
   private PreparedStatement pstmt;

   
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
   private JTextField        sequelField;
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
      sequelField              = new JTextField();
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
      topPanel.add(sequelField);

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
      /*if(!( !(titleField.getText().trim().equals("")))
      {
          editInfo.addElement(titleField.getText().trim());
      }
      
      if(!( !(amountField.getText().trim().equals("")))
      {
          editInfo.addElement(amountField.getText().trim());
      }
      
      if(!( !(genreField.getText().trim().equals("")))
      {
          editInfo.addElement(genreField.getText().trim());
      }
      
      if(!( !(releaseDateField.getText().trim().equals("")))
      {
          editInfo.addElement(releaseDateField.getText().trim());
      }
      
      if(!( !(platFormField.getText().trim().equals("")))
      {
          editInfo.addElement(platFormField.getText().trim());
      }
      
      if(!( !(directorField.getText().trim().equals("")))
      {
          editInfo.addElement(directorField.getText().trim());
      }
      
      if(!( !(sequelField.getText().trim().equals("")))
      {
          editInfo.addElement(sequelField.getText().trim());
      }*/
      //Values in editInfo should be ready to send in a transaction to update userInfo
  }
}//end of action performed

void fieldUpdater()
{
    if(moviesButton.isSelected())
    {
      castMemberField.setEnabled(true);
      directorField.setEnabled(true);
      sequelField.setEnabled(true);
      awardsField.setEnabled(true);
      platFormField.setEnabled(false);
    }
    else if (gamesButton.isSelected())
    {
      castMemberField.setEnabled(false);
      directorField.setEnabled(false);
      sequelField.setEnabled(false);
      awardsField.setEnabled(false);
      platFormField.setEnabled(true);
    }
}     

/*public static void main(String args[])
{ 
   CreateRentalDialog db = new CreateRentalDialog();
} */
}//END OF CLASS