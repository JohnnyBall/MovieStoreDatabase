CREATE OR REPLACE view  search as 
SELECT DISTINCT mgr.rid  
FROM  rentals mgr, belongs_to_genre mgb, movie mg  
WHERE mgb.gName like '%drama%' AND mgb.rid = mg.rid AND mg.rid = mgr.rid;

SELECT DISTINCT Rental1.rid as RentalId , Rental1.title as RentalTitle, Rental1.releaseDate as ReleaseDate, Rental1.num_availible_copys as AmmountAvaliable, Genr.Genres,CastMember.CastMembers, Director.Director,AwardTi.AwardTitles  FROM
(
SELECT r.rid, r.title , r.releaseDate, r.num_availible_copys
FROM rentals r, search srs
where r.rid = srs.rid
) as Rental1
JOIN
(
SELECT rnt.rid, GROUP_CONCAT(persmcast.pname) as CastMembers
FROM    person persmcast, mcast mc, was_in wasin, movie movmc, rentals rnt, search scm
WHERE   mc.pid = persmcast.pid and wasin.pid = mc.pid and wasin.rid = movmc.rid and rnt.rid =  movmc.rid and rnt.rid = scm.rid
group by movmc.rid
) as CastMember,

(
SELECT rntd.rid, GROUP_CONCAT(persdirect.pname) as Director
FROM    person persdirect,  movie md, rentals rntd, director dir,search sd
WHERE   dir.pid = md.pid and persdirect.pid = dir.pid and rntd.rid =  md.rid and rntd.rid = sd.rid
group by rntd.rid
) as Director,
(
SELECT rgen.rid, GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen,search sg
WHERE   rgen.rid = gen.rid and rgen.rid = sg.rid
group by rgen.rid
) as Genr,
(
SELECT rawd.rid, GROUP_CONCAT(hwaa.aTitle) as AwardTitles
FROM    has_won_award hwaa, rentals rawd, search sat
WHERE   hwaa.rid = rawd.rid and rawd.rid = sat.rid
group by rawd.rid
) as AwardTi 
where Rental1.rid = AwardTi.rid and AwardTi.rid = Genr.rid  and Genr.rid = Director.rid and  Director.rid  = CastMember.rid  ;




CREATE OR REPLACE view  search as 
SELECT DISTINCT mgr.rid  
FROM  rentals mgr, belongs_to_genre mgb, game mg  
WHERE mgb.gName like '%first person shooter%' AND mgb.rid = mg.rid AND mg.rid = mgr.rid;

SELECT DISTINCT Rental1.rid as RentalId , Rental1.title as RentalTitle, Rental1.releaseDate as ReleaseDate, Rental1.num_availible_copys as AmmountAvaliable, Genr.Genres,plat.Platforms  FROM
(
SELECT DISTINCT  r.rid, r.title , r.releaseDate , r.num_availible_copys
FROM rentals r,search sr
where r.rid  = sr.rid
group by r.rid
) as Rental1 ,
(
SELECT DISTINCT gen.rid as genrid,  GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen,  rentals rgen, search sg
WHERE  rgen.rid = gen.rid  and gen.rid  = sg.rid
group by gen.rid
) as Genr,
(
SELECT DISTINCT plat.rid,GROUP_CONCAT(DISTINCT plat.platName) as Platforms
FROM    plays_on_platform plat, rentals rplat, game g, search sg
WHERE   plat.rid = g.rid and g.rid = rplat.rid and rplat.rid = sg.rid
group by g.rid
) as plat
where plat.rid = Genr.genrid and Genr.genrid = Rental1.rid;





