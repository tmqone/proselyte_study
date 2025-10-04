select p.firstname as Имя, p.lastname as Фамилия, 
a.city as Город, a.state as Штат 
from person p
left join address a on a.personid = p.personid
