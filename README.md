Requirements
------------------------------------------------------------
* You have Registration service (found in  project) and all its dependencies setup and running on your machine

Steps
------------------------------------------------------------
1. load project into your ide by loading build.gradle file
2. Set following env variables in your run configurations
    1. DB_USER=vUser (or whatever user you created while creating registration service)
    2. DB_PASSWORD=test123$ (or whatever password you set for above user)
    3. DB=webstore (or whatever database you have created)
    4. DB_HOST=localhost (Your mysql server ip)
    5. DB_PORT=3306 (Your mysql port)
    6. SERVICE_PORT=9002 (This is port on which your service will run)
    7. REGISTRATION_SERVICE=http://localhost:9001/registration (This is link to your registration service)
5. If you are running project first time change 'spring.jpa.hibernate.ddl-auto' property in application.properties to 'create' (currently it is 'validate'), this will run migration and create required schema in database
6. Run Application class

Endpoints
------------------------------------------------------------
1. /product/all
2. /{id}/buy
3. /create
4. /{id}/sold
5. /{id}/stock