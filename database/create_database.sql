DROP DATABASE IF EXISTS final_task;

CREATE DATABASE IF NOT EXISTS final_task;

USE final_task;

CREATE TABLE IF NOT EXISTS users
(
  id INT AUTO_INCREMENT UNIQUE NOT NULL,
  login VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS profiles
(
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL,
  rating INT NOT NULL,
  user_id INT UNIQUE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
  id INT NOT NULL AUTO_INCREMENT,
  thread VARCHAR(255) NOT NULL,
  parent_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adverts
(
  id INT NOT NULL AUTO_INCREMENT,
  heading VARCHAR(200) NOT NULL,
  publication_date TIMESTAMP NOT NULL,
  price INT NOT NULL,
  active TINYINT NOT NULL,
  paid TINYINT NOT NULL,
  end_paid_date TIMESTAMP,
  description VARCHAR(1000),
  category_id INT NOT NULL,
  profile_id INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
id INT NOT NULL AUTO_INCREMENT,
value VARCHAR(700) NOT NULL,
publication_date TIMESTAMP,
profile_id INT NOT NULL,
advert_id INT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS chats
(
id INT NOT NULL AUTO_INCREMENT,
sender_profile_id INT NOT NULL,
advert_id INT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS messages
(
id INT NOT NULL AUTO_INCREMENT,
value VARCHAR(300) NOT NULL,
send_date TIMESTAMP NOT NULL,
chat_id INT NOT NULL,
profile_id INT NOT NULL,
PRIMARY KEY (id)
);

ALTER TABLE profiles
	  ADD CONSTRAINT fk_profiles_user_id
      FOREIGN KEY (user_id) 
      REFERENCES users (id);

ALTER TABLE chats
	  ADD CONSTRAINT fk_chats_advert_id
      FOREIGN KEY (advert_id) 
      REFERENCES adverts (id),
      ADD CONSTRAINT fk_chats_sender_profile_id
      FOREIGN KEY (sender_profile_id) 
      REFERENCES profiles (id);
      
ALTER TABLE messages
	  ADD CONSTRAINT fk_messages_profile_id
      FOREIGN KEY (profile_id) 
      REFERENCES profiles (id),
	  ADD CONSTRAINT fk_messages_chat_id
      FOREIGN KEY (chat_id) 
      REFERENCES chats (id);

ALTER TABLE categories
      ADD CONSTRAINT fk_categories_parent_id  
      FOREIGN KEY (parent_id) 
      REFERENCES categories (id);
	
ALTER TABLE adverts
      ADD CONSTRAINT fk_adverts_user_id  
      FOREIGN KEY (profile_id) 
      REFERENCES profiles (id),
	  ADD CONSTRAINT fk_adverts_catalog_id  
      FOREIGN KEY (category_id) 
      REFERENCES categories (id);
      
ALTER TABLE comments
      ADD CONSTRAINT fk_comments_user_id  
      FOREIGN KEY (profile_id) 
      REFERENCES profiles (id),
      ADD CONSTRAINT fk_comments_advert_id  
      FOREIGN KEY (advert_id) 
      REFERENCES adverts (id);

COMMIT;