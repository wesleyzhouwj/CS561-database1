with SALES_BASE as(
    select cust,prod,quant,month,
case
when month in(1,2,3) then 'Q1'
when month in(4,5,6) then 'Q2'
when month in(7,8,9) then 'Q3'
when month in(10,11,12) then 'Q4'
end as Quarter1
from sales
), 
Q3 as(
    select cust,prod,quarter1,avg(quant) as avg,min(quant) as min
    	from SALES_BASE
    		group by cust,prod,quarter1
),
BEFORE_TOT as(
    select Q3.cust as cust,Q3.prod as prod,Q3.quarter1 as quarter1,count(*) as count
    	from SALES_BASE,Q3
    		where Q3.cust = SALES_BASE.cust and Q3.prod = SALES_BASE.prod and to_number(substring(SALES_BASE.quarter1 from 2 for 2),'999999999') = to_number(substring(Q3.quarter1 from 2 for 2),'999999999')-1 and SALES_BASE.quant between Q3.min and Q3.avg
    			group by Q3.cust,Q3.prod,Q3.quarter1
),
AFTER_TOT as(
    select Q3.cust as cust,Q3.prod as prod,Q3.quarter1 as quarter1,count(*) as count
    	from SALES_BASE,Q3
    		where Q3.cust = SALES_BASE.cust and Q3.prod = SALES_BASE.prod and to_number(substring(SALES_BASE.quarter1 from 2 for 2),'999999999') = to_number(substring(Q3.quarter1 from 2 for 2),'99999999')+1 and SALES_BASE.quant between Q3.min and Q3.avg
    			group by Q3.cust,Q3.prod,Q3.quarter1
),
CUST_PROD_QUARTER as (
    select cust,prod,quarter1
    	from SALES_BASE
    		group by cust,prod,quarter1
)

select CUST_PROD_QUARTER.cust as CUSTOMER,CUST_PROD_QUARTER.prod as PRODUCT,CUST_PROD_QUARTER.quarter1 as QUARTER,BEFORE_TOT.count as BEFORE_TOT,AFTER_TOT.count as AFTER_TOT
	from (CUST_PROD_QUARTER full outer join BEFORE_TOT on CUST_PROD_QUARTER.cust = BEFORE_TOT.cust and CUST_PROD_QUARTER.prod = BEFORE_TOT.prod and CUST_PROD_QUARTER.quarter1 = BEFORE_TOT.quarter1) full outer join AFTER_TOT on CUST_PROD_QUARTER.cust = AFTER_TOT.cust and CUST_PROD_QUARTER.prod = AFTER_TOT.prod and CUST_PROD_QUARTER.quarter1 = AFTER_TOT.quarter1
		where BEFORE_TOT is not null or AFTER_TOT is not null





