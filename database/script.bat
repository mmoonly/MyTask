@echo off
mysql -u root -ppassword -s < create_database.sql
mysql -u root -ppassword -s < insert_values.sql
pause