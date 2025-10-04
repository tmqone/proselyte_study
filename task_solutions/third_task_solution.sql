select * from employee e
where e.salary > (select ee.salary from employee ee where id = e.managerid)
