# 🏠 Accommodation Booking Service 🏠
## Description
This API allows your customers to register, login, check available accommodations, book the accommodation, pay for accommodations. Admin will receive telegram notifications about new bookings and payments; every day at 12pm API will check all expired bookings and send a notification to telegram about them.

## Features
- JWT token to authenticate a user;
- Using roles of users to provide access to different API functions;
- Telegram bot to send notifications about new bookings, payments and expired bookings;
- Stripe API to send payments;
- Book an accommodation;
- Update user's role;
- Get payments by user id;

## Implemented technologies
- Maven
- MySQL
- Lombok
- Java 17
- Mapstruct
- Liquibase
- Stripe API
- Spring boot
- Telegram API
- Spring security
- Test containers
- Jackson web token

## ENDPOINTS
## Available for all users:
- POST: /api/register - Allows users to register a new account.
- POST: /api/login - Grants JWT tokens to authenticated users.
## Available for registered users
- GET: /api/users/me - Retrieves the profile information for the currently logged-in user.
- PATCH: /api/users/me - Allows users to update their profile information.
- DELETE: /api/users/me - Allows users to delete their profile.
- GET: /api/accommodations - Provides a list of available accommodations.
- GET: /api/accommodations/{accommodationId} - Retrieves detailed information about a specific accommodation.
- POST: /api/bookings - Permits the creation of new accommodation bookings.
- GET: /api/bookings/my - Retrieves user bookings
- GET: /api/bookings/{id} - Provides information about a specific booking.
- PATCH: /api/bookings/{id} - Allows users to update their booking details.
- DELETE: /api/bookings/{id} - Enables the cancellation of bookings.
- GET: /api/payments/success - Handles successful payment processing through Stripe redirection. 
- GET: /api/payments/cancel - Manages payment cancellation and returns payment paused messages during Stripe redirection.
## Available for admin users
- PUT: /api/users/{id}/role - Allows to update their roles, providing role-based access.
- DELETE: /api/users/delete/{userId} - Allows to delete user's profile by his/her ID.
- POST: /api/accommodations - Permits the addition of new accommodations.
- PATCH: /api/accommodations/{accommodationId} - Allows updates to accommodation details, including inventory management.
- DELETE: /api/accommodations/{accommodationId} - Enables the removal of accommodations.
- GET: /api/bookings/?user_id=...&status=... - Retrieves bookings based on user ID and their status.
- GET: /api/payments/{userId} - Retrieves payment information by user ID.

## Important notice
Please note that endpoints with POST, PUT and PATCH methods require JSON body as an argument.
## API using steps
1. Upload this API to your server.
2. Add accommodations using admin user. This API implements liquibase so all needed tables will be created in the DB automatically after launching the API. Also, admin user will be added. Login: admin@user.com, password: 12345.
3. Add the following keys to .env file:
    - telegram bot token;
    - telegram chat id;
    - Stripe secret key.
4. Done! Now new users can register to your Accommodation Booking Service and book the accommodations.


