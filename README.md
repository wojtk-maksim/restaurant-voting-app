Restaurant Voting App
===============================
------------------------------- 
Final project on TopJava internship!
-------------------------------

Initial task:
-------------------------------
Design and implement a REST API using Spring-Boot/Spring Data JPA **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and its lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to GitHub repository. It should contain the code, README.md with API documentation and couple curl commands to test it (**better - link to Swagger**).

-----------------------------
P.S.: Make sure everything works with latest version that is on GitHub :)  
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

-----------------------------


My extended realization:
-----------------------------
* JWT based security
* 3 types of users: USER, ADMIN, SUPER_ADMIN
* Soft delete and hard delete functionality
* Error response corresponding to RFC 7807 specification
* Lightweight documentation for problems (realization of 'type' in RFC 7807)
* Localization of problems' descriptions and exceptions' messages
* Ample opportunities for entity management (update, enable/disable, soft delete/hard delete)
* Possibility to see all users who have voted for lunch 
* Batch input of lunches
* Deadline is at 12:00 Moscow time!

### Swagger UI is available at '/swagger' 