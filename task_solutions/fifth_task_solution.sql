select c.* from customers c
full outer join orders o on o.customerid = c.id
where (select count(*) from orders where customerid = c.id) < 1
