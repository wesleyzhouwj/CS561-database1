 with
January_max as(
    select B.cust,B.prod,B.quant as Jan_Max,B.day as Jan_day,B.month as Jan_month,B.year as Jan_year from
    (select cust,prod,max(quant) as JanMax
    	from sales
    		where month = '1' and year > '2000' and year < '2005' /*between 2001 and 2004  is ok to get the same result*/
    		group by cust,prod) as A
    natural join 
    (select * from sales) as B where B.quant = A.JanMax 
),
February_min as(
    select D.cust,D.prod,D.quant as Feb_Min,D.day as Feb_day,D.month as Feb_month,D.year as Feb_year from
   ( select cust,prod,min(quant) as FebMin
   		from sales
   			where month = '2'
   				group by cust,prod) as C
    natural join
    	(select * from sales) as D where D.quant = C.FebMin 
),
March_min as(
    select F.cust,F.prod,F.quant as Mar_Min,F.day as Mar_day,F.month as Mar_month,F.year as Mar_year from
    (select cust,prod,min(quant) as MarMin
    	from sales
    		where month = '3'
    			group by cust,prod) as E
    natural join
    	(select * from sales) as F where F.quant = E.MarMin 
),
Jan_Feb as(
		select *
			from January_max  natural full outer join February_min  
)

select * from Jan_Feb natural full join March_min order by cust,prod

