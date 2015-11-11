




SELECT DISTINCT * FROM
(
SELECT r.rid, r.title as RentalTitle , r.releaseDate as ReleaseDate, r.num_availible_copys as AmmountAvaliable
FROM rentals r
where r.rid = '208'
) as Rental1
JOIN
(SELECT GROUP_CONCAT(persmcast.pname) as CastMembers
FROM    person persmcast, mcast mc, was_in wasin, movie movmc, rentals rnt
WHERE   mc.pid = persmcast.pid and wasin.pid = mc.pid and wasin.rid = movmc.rid and rnt.rid =  movmc.rid and rnt.rid = '208') as CastMember
JOIN
(SELECT GROUP_CONCAT(persdirect.pname) as Director
FROM    person persdirect,  movie md, rentals rntd, director dir
WHERE   dir.pid = md.pid and persdirect.pid = dir.pid and rntd.rid =  md.rid and rntd.rid = '208') as Director
JOIN
(SELECT GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen
WHERE   rgen.rid = gen.rid and rgen.rid = '208') as Genr
JOIN
(SELECT GROUP_CONCAT(hwaa.aTitle) as AwardTitles
FROM    has_won_award hwaa, rentals rawd
WHERE   hwaa.rid = rawd.rid and rawd.rid = '208') as AwardTi ;



SELECT DISTINCT * FROM
(
SELECT DISTINCT  r.rid, r.title as RentalTitle , r.releaseDate as ReleaseDate, r.num_availible_copys as AmmountAvaliable
FROM rentals r
where r.rid  = '209'
) as Rental1
JOIN
(
SELECT DISTINCT GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen
WHERE   rgen.rid = gen.rid  and gen.rid ='209'
) as Genr  
JOIN
(SELECT DISTINCT GROUP_CONCAT(plat.platName) as Platforms
FROM    plays_on_platform plat, rentals rplat
WHERE   plat.rid = rplat.rid and rplat.rid ='209' ) 
as plat;

