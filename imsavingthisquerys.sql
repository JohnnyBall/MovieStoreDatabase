



CREATE OR REPLACE view  search as 
SELECT DISTINCT mgr.rid  
FROM  rentals mgr, belongs_to_genre mgb, movie mg  
WHERE mgb.gName like '%drama%' AND mgb.rid = mg.rid AND mg.rid = mgr.rid;

SELECT DISTINCT * FROM
(
SELECT r.rid, r.title as RentalTitle , r.releaseDate as ReleaseDate, r.num_availible_copys as AmmountAvaliable
FROM rentals r, search srs
where r.rid = srs.rid
) as Rental1
JOIN
(SELECT GROUP_CONCAT(persmcast.pname) as CastMembers
FROM    person persmcast, mcast mc, was_in wasin, movie movmc, rentals rnt, search scm
WHERE   mc.pid = persmcast.pid and wasin.pid = mc.pid and wasin.rid = movmc.rid and rnt.rid =  movmc.rid and rnt.rid = scm.rid) as CastMember
JOIN
(SELECT GROUP_CONCAT(persdirect.pname) as Director
FROM    person persdirect,  movie md, rentals rntd, director dir,search sd
WHERE   dir.pid = md.pid and persdirect.pid = dir.pid and rntd.rid =  md.rid and rntd.rid = sd.rid) as Director
JOIN
(SELECT GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen,search sg
WHERE   rgen.rid = gen.rid and rgen.rid = sg.rid) as Genr
JOIN
(SELECT GROUP_CONCAT(hwaa.aTitle) as AwardTitles
FROM    has_won_award hwaa, rentals rawd, search sat
WHERE   hwaa.rid = rawd.rid and rawd.rid = sat.rid) as AwardTi ;






CREATE OR REPLACE view  search as 
SELECT DISTINCT mgr.rid  
FROM  rentals mgr, belongs_to_genre mgb, game mg  
WHERE mgb.gName like '%first person shooter%' AND mgb.rid = mg.rid AND mg.rid = mgr.rid;

SELECT DISTINCT * FROM
(
SELECT DISTINCT  r.rid, r.title as RentalTitle , r.releaseDate as ReleaseDate, r.num_availible_copys as AmmountAvaliable
FROM rentals r,search sr
where r.rid  = sr.rid
) as Rental1
JOIN
(
SELECT DISTINCT GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen,search sg
WHERE   rgen.rid = gen.rid  and gen.rid = sg.rid
) as Genr  
JOIN
(
SELECT DISTINCT GROUP_CONCAT(plat.platName) as Platforms
FROM    plays_on_platform plat, rentals rplat,search sp
WHERE   plat.rid = rplat.rid and rplat.rid = sp.rid
) 
as plat;




