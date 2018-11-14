with
maxQ as(
select B.cust as customer, B.quant as max_quant,B.prod as products, B.day,B.month,B.year,B.state from
(select cust, max(quant) as max_quant from sales group by cust) as A
	left join 
		(select * from sales) as B 
        	on B.cust = A.cust and B.quant = A.max_quant ),
            
minQ as(
select D.cust as customer, D.quant as min_quant,D.prod as Products, D.day,D.month,D.year,D.state from
(select cust, min(quant) as min_quant from sales group by cust) as C
	left join 
		(select * from sales) as D 
        	on D.cust = C.cust and D.quant = C.min_quant), 
avgQ as(
    (select cust,round(avg(quant) ) as avgC from sales group by cust)
)

select maxQ.customer, maxQ.max_quant,maxQ.products,concat(maxQ.month,'/',maxQ.day,'/',maxQ.year) as max_date,maxQ.state,minQ.min_quant,minQ.products,concat(minQ.month,'/',minQ.day,'/',minQ.year)as min_date,minQ.state,avgQ.avgC
	from maxQ join minQ on maxQ.customer = minQ.customer join avgQ on avgQ.cust = maxQ.customer order by customer