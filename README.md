# dockerspring
Run the app locally 

POST -> localhost:8080/transaction
example:
{
	"amount":"500" 
}

//change amount as many times you want and keep posting 

GET -> localhost:8080/transaction/all

//will return all transactions done within 60 seconds , cos cache will be cleared

GET -> localhost:8080/statistics

//will fetch statistics for the transaction done in 60seconds

Swagger UI 

http://localhost:8080/swagger-ui.html#/
