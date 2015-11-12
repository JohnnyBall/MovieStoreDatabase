import java.sql.*;

public class DBHandler
{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/moviestore";
    static final String USER = "root";
    static final String PASS = "141305";  //Adust this according to local host login

    public final String acquireResults;
    public final String directorSearch;
    public final String castSearch;
    public final String movieGenreSearch;
    public final String gameGenreSearch;
    public final String genreSearch;
    public final String platformSearch;
    public final String awardWinnerSearch;
    public final String haveRentedSearch;
    public final String notRentedSearch;
    public final String accountDetails;
    public final String sequelSearch;
    public final String titleGamesSearch;
    public final String titleMoviesSearch;
    public final String keywordMovieSearch;
    public final String keywordGameSearch;
    public final String personNameSearch;
    public final String addressSearch;
    public final String acquireResults2ElectricBugalooMovies;
    public final String acquireResults2ElectricBugalooGames;

    public DBHandler()
    {

        acquireResults = "SELECT DISTINCT * "+//"SELECT DISTINCT ar.title as RentalTitle, ar.rid as RENTALID " +
                         "FROM rentals ar " +
                         "WHERE ar.rid IN (";

//EXAMPLE OF CREATE VIEW FOR WHEN ITS NEEDED
/*CREATE OR REPLACE view  search as 
SELECT DISTINCT mgr.rid  
FROM  rentals mgr, belongs_to_genre mgb, movie mg  
WHERE mgb.gName like '%drama%' AND mgb.rid = mg.rid AND mg.rid = mgr.rid;*/

        acquireResults2ElectricBugalooMovies = "SELECT DISTINCT Rental1.rid as Rental_Id , Rental1.title as RentalTitle, Rental1.releaseDate as ReleaseDate, Rental1.num_availible_copys as AmountAvaliable, Genr.Genres,CastMember.CastMembers, Director.Director"+/*,AwardTi.AwardTitles*/ " FROM "+
                                               "(SELECT r.rid, r.title , r.releaseDate, r.num_availible_copys "+
                                               "FROM rentals r, search srs  "+
                                               "where r.rid = srs.rid) as Rental1 "+
                                               "JOIN "+
                                               "(SELECT rnt.rid, GROUP_CONCAT(persmcast.pname) as CastMembers "+
                                               "FROM    person persmcast, mcast mc, was_in wasin, movie movmc, rentals rnt, search scm "+
                                               "WHERE   mc.pid = persmcast.pid and wasin.pid = mc.pid and wasin.rid = movmc.rid and rnt.rid =  movmc.rid and rnt.rid = scm.rid "+
                                               "group by movmc.rid) as CastMember, "+
                                               "(SELECT rntd.rid, GROUP_CONCAT(persdirect.pname) as Director "+
                                               "FROM    person persdirect,  movie md, rentals rntd, director dir,search sd "+
                                               "WHERE   dir.pid = md.pid and persdirect.pid = dir.pid and rntd.rid =  md.rid and rntd.rid = sd.rid "+
                                               "group by rntd.rid) as Director, "+
                                               "(SELECT rgen.rid, GROUP_CONCAT(gen.gname) as Genres "+
                                               "FROM    belongs_to_Genre gen, movie mg, rentals rgen,search sg "+
                                               "WHERE   mg.rid = rgen.rid and rgen.rid  = gen.rid and mg.rid = sg.rid "+
                                               "group by rgen.rid) as Genr "+
                                               //"(SELECT rawd.rid, GROUP_CONCAT(hwaa.aTitle) as AwardTitles "+
                                               //"FROM    has_won_award hwaa, rentals rawd, search sat "+
                                               //"//WHERE   hwaa.rid = rawd.rid and rawd.rid = sat.rid "+
                                               //"group by rawd.rid) as AwardTi "+
                                               "where Rental1.rid "+/*= AwardTi.rid and AwardTi.rid */"= Genr.rid  and Genr.rid = Director.rid and  Director.rid  = CastMember.rid; ";


//EXAMPLE OF CREATE VIEW FOR WHEN ITS NEEDED
/*CREATE OR REPLACE view  search as 
SELECT DISTINCT mgr.rid  
FROM  rentals mgr, belongs_to_genre mgb, game mg  
WHERE mgb.gName like '%first person shooter%' AND mgb.rid = mg.rid AND mg.rid = mgr.rid;*/

        acquireResults2ElectricBugalooGames = "SELECT DISTINCT Rental1.rid as RentalId , Rental1.title as RentalTitle, Rental1.releaseDate as ReleaseDate, Rental1.num_availible_copys as AmmountAvaliable, Genr.Genres,plat.Platforms "+  
                                              "FROM( "+
                                              "SELECT DISTINCT  r.rid, r.title , r.releaseDate , r.num_availible_copys " +
                                              "FROM rentals r,search sr "+
                                              "where r.rid  = sr.rid "+
                                              "group by r.rid) as Rental1 , "+
                                              "(SELECT DISTINCT gen.rid as genrid,  GROUP_CONCAT(gen.gname) as Genres "+
                                              "FROM    belongs_to_Genre gen,  rentals rgen, search sg "+
                                              "WHERE  rgen.rid = gen.rid  and gen.rid  = sg.rid "+
                                              "group by gen.rid) as Genr, "+
                                              "(SELECT DISTINCT plat.rid,GROUP_CONCAT(DISTINCT plat.platName) as Platforms "+
                                              "FROM    plays_on_platform plat, rentals rplat, game g, search sg "+
                                              "WHERE   plat.rid = g.rid and g.rid = rplat.rid and rplat.rid = sg.rid "+
                                              "group by g.rid) as plat "+
                                              "where plat.rid = Genr.genrid and Genr.genrid = Rental1.rid;";


        addressSearch = "Select a.street, a.city, a.state, a.zip, a.phone "+
                        "From has_address ha, address a, user u, person p "+
                        "WHERE u.pid = ? AND u.pid = p.pid AND p.pid = ha.pid AND ha.zip = a.zip AND ha.street = a.street;";

        directorSearch = "SELECT DISTINCT r.rid " +
                         "FROM rentals r, person p, director d, movie m " +
                         "WHERE p.pname like ? AND p.pid = d.pid AND m.pid = d.pid AND m.rid = r.rid ";

        personNameSearch = "SELECT DISTINCT p.pname "+
                            "FROM person p "+
                            "WHERE p.pid = ? ";

        castSearch = "SELECT DISTINCT r.rid " +
                     "FROM rentals r, person p, mcast c, movie m, was_in w " +
                     "WHERE p.pname like ? AND p.pid = c.pid AND w.pid = c.pid AND w.rid = m.rid AND m.rid = r.rid";

        movieGenreSearch = "SELECT DISTINCT mgr.rid " +
                           "FROM  rentals mgr, belongs_to_genre mgb, movie mg " +
                           "WHERE mgb.gName like ? AND mgb.rid = mg.rid AND mg.rid = mgr.rid";

        gameGenreSearch = "SELECT DISTINCT gr.rid " +
                          "FROM  rentals gr, belongs_to_genre gb, game gg " +
                          "WHERE gb.gName like ? AND gb.rid = gg.rid AND gg.rid = gr.rid";

        genreSearch = "SELECT DISTINCT bgmr.rid " +
                      "FROM  rentals bgmr, belongs_to_genre bgm, movie bgmm, game bgmg " +
                      "WHERE (bgm.gName like ? AND ((bgmb.rid = bgmm.rid AND bgmm.rid = bgmr.rid) OR (bgm.rid = bgmg.rid AND bgmg.rid = bgmr.rid)))";

        platformSearch = "SELECT DISTINCT pr.rid " +
                         "FROM  rentals pr, game pg, plays_on_platform pop " +
                         "WHERE pop.platName like ? AND pop.rid = pg.rid AND pg.rid = pr.rid";

        awardWinnerSearch = "SELECT DISTINCT awr.rid " +
                            "FROM rentals awr, movie awm, has_won_award a " +
                            "WHERE awm.rid = awr.rid AND awr.rid = a.rid";

        titleGamesSearch =  "SELECT DISTINCT tgr.rid "+
                            "FROM  rentals tgr, game tgg "+
                            "WHERE tgr.title like ? AND tgr.rid = tgg.rid AND tgg.rid = tgr.rid";

        titleMoviesSearch = "SELECT DISTINCT tmr.rid "+
                            "FROM  rentals tmr, movie tm "+
                            "WHERE tmr.title like ? AND tmr.rid = tm.rid AND tm.rid = tmr.rid";

        haveRentedSearch = "SELECT DISTINCT r.rid, r.title,u.pid " +
                           "FROM rentals_record_rents rrr, rentals r, user u " +
                           "WHERE u.pid = ? AND rrr.rid = r.rid AND u.pid = rrr.pid";

        notRentedSearch = "SELECT DISTINCT nr.rid " +
                          "FROM rentals nr " +
                          "where nr.rid not in ( " +
                                                "SELECT DISTINCT rl.rid " +
                                                "FROM rentals_record_rents rrrl, rentals rl, user ul " +
                                                "WHERE ul.pid = ? AND rrrl.rid = rl.rid AND ul.pid = rrrl.pid)";

        accountDetails = "SELECT DISTINCT r.rid, r.title,r.releaseDate,rrr.Trackingnum,rrr.from_date " +
                         "FROM rentals_record_rents rrr, rentals r, user u " +
                         "WHERE u.pid = ?  AND rrr.rid = r.rid AND u.pid = rrr.pid";

        sequelSearch   = "SELECT DISTINCT r1.rid, r1.title " +
                         "FROM  rentals r,rentals r1, movie m, movie sm " +
                         "WHERE r.rid = ? AND m.rid_of_prequel = r.rid AND m.rid = r1.rid";
 
        keywordMovieSearch = " " + movieGenreSearch +" UNION " + castSearch +" UNION "+ directorSearch+" UNION " +titleMoviesSearch;

        keywordGameSearch = " " + gameGenreSearch +" UNION " + platformSearch +" UNION " +titleGamesSearch;

    }

    public Connection establishConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DATABASE_URL, USER, PASS);
    }

}
