




SELECT DISTINCT * FROM
(
SELECT r.rid, r.title as RentalTitle , r.releaseDate as ReleaseDate, r.num_availible_copys as AmmountAvaliable
FROM rentals r
where r.rid in (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, movie m  WHERE gb.gName like '%action%' AND gb.rid = m.rid AND m.rid = gr.rid)
) as Rental1
JOIN
(SELECT GROUP_CONCAT(persmcast.pname) as CastMembers
FROM    person persmcast, mcast mc, was_in wasin, movie movmc, rentals rnt
WHERE   mc.pid = persmcast.pid and wasin.pid = mc.pid and wasin.rid = movmc.rid and rnt.rid =  movmc.rid and rnt.rid in  (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, movie m  WHERE gb.gName like '%action%' AND gb.rid = m.rid AND m.rid = gr.rid)) as CastMember
JOIN
(SELECT GROUP_CONCAT(persdirect.pname) as Director
FROM    person persdirect,  movie md, rentals rntd, director dir
WHERE   dir.pid = md.pid and persdirect.pid = dir.pid and rntd.rid =  md.rid and rntd.rid in  (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, movie m  WHERE gb.gName like '%action%' AND gb.rid = m.rid AND m.rid = gr.rid)) as Director
JOIN
(SELECT GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen
WHERE   rgen.rid = gen.rid and rgen.rid in  (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, movie m  WHERE gb.gName like '%action%' AND gb.rid = m.rid AND m.rid = gr.rid)) as Genr
JOIN
(SELECT GROUP_CONCAT(hwaa.aTitle) as AwardTitles
FROM    has_won_award hwaa, rentals rawd
WHERE   hwaa.rid = rawd.rid and rawd.rid in  (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, movie m  WHERE gb.gName like '%action%' AND gb.rid = m.rid AND m.rid = gr.rid)) as AwardTi ;



SELECT DISTINCT * FROM
(
SELECT DISTINCT  r.rid, r.title as RentalTitle , r.releaseDate as ReleaseDate, r.num_availible_copys as AmmountAvaliable
FROM rentals r
where r.rid in (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, game gg  WHERE gb.gName like '%moba%' AND gb.rid = gg.rid AND gg.rid = gr.rid)
) as Rental1
JOIN
(
SELECT DISTINCT GROUP_CONCAT(gen.gname) as Genres
FROM    belongs_to_Genre gen, rentals rgen
WHERE   rgen.rid = gen.rid  and gen.rid in (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, game gg  WHERE gb.gName like '%moba%' AND gb.rid = gg.rid AND gg.rid = gr.rid)
) as Genr  
JOIN
(SELECT DISTINCT GROUP_CONCAT(plat.platName) as Platforms
FROM    plays_on_platform plat, rentals rplat
WHERE   plat.rid = rplat.rid and rplat.rid in (SELECT DISTINCT gr.rid FROM  rentals gr, belongs_to_genre gb, game gg  WHERE gb.gName like '%moba%' AND gb.rid = gg.rid AND gg.rid = gr.rid)) 
as plat;

