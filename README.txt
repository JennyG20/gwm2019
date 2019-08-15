To try the app, a local MySQL server has to be running.
In order to set up the server, please follow the instructions below.
If you already have MySQL installed on your computer, skip steps 1-2.
The publicly distributed software would connect to a remote server 
without any manual configuration but unfortunately I cannot set up a 
server for this at the moment.

1) download and install MySQL Installer from 

"https://dev.mysql.com/downloads/installer/"

2) install MySQL Server and MySQL Workbench
3) configure MySQL Server to be locally accessible and set the password for 
root or a custom user with SELECT, UPDATE and INSERT access for

"h8J2Kws345ON3"

4) create a schema called

"main"

5) open SampleData.sql in MySQL Workbench and run it in the "main" schema