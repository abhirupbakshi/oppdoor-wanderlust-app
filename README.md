# Wanderlust Travel Planner
An travel planning app where a user can add different destinations and keep track of the trips they went into, with all of the expenses.

# API Documentation
In the following docs, I will use ***HOST*** as a placeholder for the domain on which the application is running.

# Auth APIs
* Login route: POST ***HOST***/login (need to pass username and password for "Basic" authentication)
* Logout route: POST ***HOST***/login (need to pass the JWT token)

After authentication, the JWT is set in the response header with header name: "token". This token needs to be used for all of the successing requests.

## User APIs
* Register user: POST ***HOST***/users
* Get a user info: GET ***HOST***/users
* Delete a user: DELETE ***HOST***/users

## Destination APIs
* Create destination: POST ***HOST***/destinations (protected route)
* Get a destination: GET ***HOST***/destinations/{uuid} (protected route)
* Get all created destinations: GET ***HOST***/destinations?page=&limit= (protected route) (page and limit is optional)
* Delete a destination: DELETE ***HOST***/destinations/{uuid} (protected route)

## Itinerary APIs
* Create itinerary: POST ***HOST***/itineraries/{destinationId} (protected route)
* Get a itinerary: GET ***HOST***/itineraries/{uuid} (protected route)
* Get all created itineraries: GET ***HOST***/itineraries?page=&limit= (protected route) (page and limit is optional)
* Delete a itinerary: DELETE ***HOST***/itineraries/{uuid} (protected route)

## Expense APIs
* Create expense: POST ***HOST***/expenses/{itineraryId} (protected route)
* Get a expense: GET ***HOST***/expenses/{uuid} (protected route)
* Get all created expenses: GET ***HOST***/expenses?page=&limit=&low&high (protected route) (page and limit is optional) (low and high represents the "amount" value and can be used to filter expenses in a specific range. Both are optional)
* Get created expenses, filtered by category: GET ***HOST***/expenses/category/{category_name}?page=&limit= (protected route) (page and limit is optional)
* Delete a expense: DELETE ***HOST***/expenses/{uuid} (protected route)

# Set Up
* The need environment variabls can be found in the env.txt file.
* The app uses Java 17 as the base version.
* A postman collection file (JSON format) has also been included for self testing of the APIs.
