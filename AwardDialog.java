// This program displays the results of a query on a database
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AwardDialog extends JDialog
{
   private JPanel         topPanel;
   private JLabel         headerLabel;
   private JLabel         awardsLabel;
   private Connection     connection;
   private String         movieTitle;

   public AwardDialog(Connection newConnection, String movieTitle, int rid)
   {
      Statement stmt;
      ResultSet rs;
      Vector<Object> awards = new Vector<Object>();
      String query;
      String awardsWon;
      this.movieTitle = movieTitle;
      setLayout(new FlowLayout(FlowLayout.CENTER));
      connection   = newConnection;
      topPanel     = new JPanel();
      headerLabel  = new JLabel("Awards Won:");
      awardsLabel  = new JLabel();
      
      query = "SELECT a.aTitle FROM has_won_award a WHERE a.rid = " + rid +';';
      try
      {
        stmt = connection.createStatement();
        rs   = stmt.executeQuery(query);
        
        if(!rs.next()) 
        {
            JOptionPane.showMessageDialog(null,"No records found!");
            this.dispose();
            return;
        }
        else 
        {
            do
            {
                awards.addElement(rs.getObject(1));
            }
            while(rs.next());
        }
        
        awardsWon = (String)awards.elementAt(0);
        for(int i = 1; i < awards.size(); ++i)
            awardsWon = awardsWon + ", " + (String)awards.elementAt(i);
        
        awardsLabel.setText(awardsWon);
        
      }
      
      catch(SQLException ex)
      {
          JOptionPane.showMessageDialog(null, ex.getMessage(), "Query error!", JOptionPane.ERROR_MESSAGE);
          this.dispose();
          return;
      }

      topPanel.add(headerLabel);
      topPanel.add(awardsLabel);
      add(topPanel,BorderLayout.CENTER);

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
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setTitle("Awards Won by " + movieTitle);
    setVisible(true);
  }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
