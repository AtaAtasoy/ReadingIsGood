# ReadingIsGood

## Run
`docker-compose up --build`

## Implementation Details & Discussions

### Tech Stack
Java 17, Spring Boot 3.0.0, H2 Database.

### Architecture
Controller <-> Service <-> Repository

### DB Tables
OneToMany relation between Customers - Orders.

ManyToMany relation between Orders - Books. 

In order to be able to add `price` and `quantity` columns to the Orders-Books relation, created a `OrderDetail` table with PK(`order_id`,`book_id`) and `price`, `quantity` table. It almost served as an order-log-store. The table was necessary to be able to calculate the result required on the statistics endpoint. 

Query below calculates the desired monthly statistics result.

```SQL
SELECT rand() as id, COUNT(DISTINCT order_id) AS total_order_count,SUM(quantity) AS total_book_count,
MONTHNAME(created_at) as month_name,SUM(price*quantity) AS total_purchased_amount
FROM (SELECT od.order_id, o.created_at, od.quantity, od.price FROM order_detail od
INNER JOIN ORDERS o ON o.order_id = od.order_id
WHERE o.customer_id = :customerId)
GROUP BY MONTHNAME(created_at);
``` 
`rand() as id` was required to be able to map the result of this query to the `Statistics` entity.

## Use Cases

- [X] Registering new customer
- [X] Placing new order
- [X] Tracking stock of books
- [X] List all orders of the customer w/pagination
- [X] Viewing the order details in between dates.
- [X] Query Monthly Statistics

## Base Requirements
- No tests due to time constraints... Not the best practice to leave testing to the end. In my defense, I was testing the program manually...
- Containerized with Dockerfile and DockerCompose.
- Restful endpoints 
- Clean Code (hopefully..)

## Controllers

### Customer Controller
- [X] Will persist new customers.
- [X] Will query all orders of the customer with pagination.

### Book Controller
- [X] Will persist new book.
- [X] Will update book's stock.

### Order Controller
- [X] Has 3 status IN_PROGRESS, CANCELED, COMPLETED with endpoints to update the status.
- [X] Will update stock records. 
- [X] Will query order by Id.
- [X] Lists orders by date interval.

### Statistics Controller
- [X] Serves the `total_order_count`, `total_book_count`, `total_purchased_amount` per month.

## Validations
- [X] Validators implemented on service layer to ensure fields are not blank, negative or duplicates of one other.

## Authentications - DNE.. 
- For this scope a user with admin priveledges would've made sense. Could've been handled with JWT.

## Reponses
- Success messages return the entity models defined in the `models` package. Error responses for each controller is defined in `controlleradvice` package.

## Postman
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/c175f1b8a4a4831c53e7?action=collection%2Fimport)

Above might be deprecated. Also try [this](https://lunar-shadow-62871.postman.co/workspace/44f416f9-2386-4589-8e1f-f957da89ef9d/collection/11764954-87cd1e07-68cd-432b-821f-fd5c3d76724d?action=share&creator=11764954)
