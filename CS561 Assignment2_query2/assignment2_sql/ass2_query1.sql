With THE_AVG as(
    select cust,prod,floor(avg(quant)) as avg
        from sales
            group by cust,prod
),
OTHER_PROD_AVG as(
    select A.cust as cust,A.prod as prod,floor(avg(B.quant)) as avg
        from sales as A, sales as B
            where A.cust = B.cust and A.prod != B.prod
                group by A.cust,A.prod
),
OTHER_CUST_AVG as(
    select C.cust as cust,C.prod as prod,floor(avg(D.quant)) as avg
        from sales as C,sales as D
            where C.cust != D.cust and C.prod = D.prod
                group by C.cust,C.prod
)
select THE_AVG.cust,THE_AVG.prod,THE_AVG.avg,OTHER_PROD_AVG.avg as other_prod_avg,OTHER_CUST_AVG.avg as other_cust_avg
    from (THE_AVG full outer join OTHER_PROD_AVG on THE_AVG.cust = OTHER_PROD_AVG.cust and THE_AVG.prod = OTHER_PROD_AVG.prod) full outer join OTHER_CUST_AVG on THE_AVG.cust = OTHER_CUST_AVG.cust and THE_AVG.prod = OTHER_CUST_AVG.prod
        order by cust,prod