with SALES_BASE as(
    select cust,prod,quant,month,
case
when month in(1,2,3) then 'Q1'
when month in(4,5,6) then 'Q2'
when month in(7,8,9) then 'Q3'
when month in(10,11,12) then 'Q4'
end as Quarter
from sales
),
quarter as(
    select distinct quarter as quarter from SALES_BASE
),
cust_prod as(
    select cust,prod
    	from SALES_BASE
    		group by cust,prod
),
base as(
    select * from cust_prod,quarter
),
base1 as(
    select cust, prod, quarter, avg(quant) as avg_q
from SALES_BASE
group by cust, prod, quarter
),
base_avg as (
    select a.cust, a.prod, a.quarter,avg_q,
	case
	when a.quarter= 'Q1' then 1
	when a.quarter= 'Q2'  then 2
	when a.quarter= 'Q3'  then 3
	when a.quarter= 'Q4'  then 4
	end as q_n
from base as a left outer join base1 as b on a.cust= b.cust and a.prod=b.prod and a.quarter=b.quarter
),
before_avg as(
    select a.cust,a.prod,a.quarter,b.avg_q as before_a
    	from base_avg as a,base_avg as b
    		where a.cust = b.cust and a.prod = b.prod and a.q_n-1 = b.q_n
),
after_avg as(
    select a.cust,a.prod,a.quarter,b.avg_q as after_a
    	from base_avg as a,base_avg as b
    		where a.cust = b.cust and a.prod = b.prod and a.q_n+1 = b.q_n
)
select base_avg.cust as customer,base_avg.prod as product,base_avg.quarter as quarter,round(before_a) as before_avg,round(after_a) as after_avg
	from (base_avg full outer join before_avg on base_avg.cust = before_avg.cust and base_avg.prod = before_avg.prod and base_avg.quarter = before_avg.quarter) full outer join after_avg on base_avg.cust = after_avg.cust and base_avg.prod = after_avg.prod and base_avg.quarter = after_avg.quarter
		where before_a is not null or after_a is not null
        	order by customer,product

