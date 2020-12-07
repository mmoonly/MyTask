USE final_task;

INSERT categories (thread,parent_id)
VALUES
('Electronic',null),
('Phone',1),
('Computer',1);

INSERT profiles (name,surname,rating)
VALUES
('Victor','Hrenin',1),
('Petya','Vasiliev',3),
('Dima','Lukash',0);

INSERT adverts (heading,publication_date,price,active,paid,end_paid_date,description,category_id,profile_id)
VALUES
('Sell Iphone 12','2020-11-23',300,1,0,null,'Sell iphone 12 best of the best',2,1),
('Sell Razor','2020-11-23',500,1,1,'2020-11-30','Sell computer',3,1),
('Sell Samsung galaxy','2020-11-23',420,1,0,null,'Sell Samsung galaxy best of the best',2,2);

COMMIT;