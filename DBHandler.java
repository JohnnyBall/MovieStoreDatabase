import java.sql.*;

public class DBHandler 
{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/moviestore";
    static final String USER = "root";
    static final String PASS = "Prog1";  //Adust this according to local host login
    
    public final String acquireResults;
    public final String directorSearch;
    public final String castSearch;
    public final String movieGenreSearch;
    public final String gameGenreSearch;
    public final String genreSearch;
    public final String platformSearch;
    public final String awardWinnerSearch;
    public final String keywordSearch;
    public final String haveRentedSearch;
    public final String notRentedSearch;
    public final String accountDetails;
    public final String sequelSearch;
    
    public DBHandler()
    {
        
        acquireResults = "SELECT DISTINCT ar.title, ar.rid " +
                         "FROM rentals ar " +
                         "WHERE ar.rid IN(";
        
        directorSearch = "SELECT DISTINCT r.rid " +
                         "FROM rentals r, person p, director d, movie m " +
                         "WHERE p.pname = ? AND p.pid = d.pid AND m.pid = d.pid AND m.rid = r.rid ";
        
        castSearch = "SELECT DISTINCT r.rid" +
                     "FROM rentals r. person p, mcast c, movie m, was_in w" +
                     "WHERE p.pname = ? AND p.pid = c.pid AND w.pid = c.pid AND w.rid = m.rid AND m.rid = r.rid";
        
        movieGenreSearch = "SELECT DISTINCT r.rid" +
                           "FROM  rentals r, belongs_to_genre b, movie m" +
                           "WHERE b.gName = ? AND b.rid = m.rid AND m.rid = r.rid";
        
        gameGenreSearch = "SELECT DISTINCT r.rid" +
                          "FROM  rentals r, belongs_to_genre b, game g" +
                          "WHERE b.gName = ? AND b.rid = g.rid AND g.rid = r.rid";
        
        genreSearch = "SELECT DISTINCT r.rid" +
                      "FROM  rentals r, belongs_to_genre b, movie m, game g" +
                      "WHERE (b.gName = ? AND ((b.rid = m.rid AND m.rid = r.rid) OR (b.rid = g.rid AND g.rid = r.rid)))";
        
        platformSearch = "SELECT DISTINCT r.rid" +
                         "FROM  rentals r, game g, plays_on_platform pop" +
                         "WHERE pop.platName = ? AND pop.rid = g.rid AND g.rid = r.rid";
        
        awardWinnerSearch = "SELECT DISTINCT r.rid" +
                            "FROM rentals r, movie m, has_won_award a" +
                            "WHERE m.rid = r.rid AND r.rid = a.rid";
        
        keywordSearch = "PLACEHOLDER";//  Needs to be clarified for me before I just type it in
        
        haveRentedSearch = "SELECT DISTINCT r.rid, r.title,u.pid" +
                           "FROM rentals_record_rents rrr, rentals r, user u" +
                           "WHERE u.pid = ? AND rrr.rid = r.rid AND u.pid = rrr.pid";
        
        notRentedSearch = "SELECT DISTINCT r.rid, r.title" +
                          "FROM rentals r" +
                          "where r.rid not in ( " +
                                                "SELECT DISTINCT rl.rid" +
                                                "FROM rentals_record_rents rrrl, rentals rl, user ul" +
                                                "WHERE ul.pid = ? AND rrrl.rid = rl.rid AND ul.pid = rrrl.pid)";
        
        accountDetails = "SELECT DISTINCT r.rid, r.title,r.releaseDate,rrr.Trackingnum,rrr.from_date" +
                         "FROM rentals_record_rents rrr, rentals r, user u" +
                         "WHERE u.pid = ?  AND rrr.rid = r.rid AND u.pid = rrr.pid";
        
        sequelSearch = "SELECT DISTINCT r1.rid, r1.title" +
                       "FROM  rentals r,rentals r1, movie m, movie sm" +
                       "WHERE r.rid = 201 AND m.rid_of_prequel = r.rid AND m.rid = r1.rid";
        
    }
    
    public Connection establishConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DATABASE_URL, USER, PASS);
    }
    
}
