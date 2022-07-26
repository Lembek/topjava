curl -X GET --location "http://localhost:8080/topjava/rest/meals" 

curl -X GET --location "http://localhost:8080/topjava/rest/meals/100003" 

curl -X DELETE --location "http://localhost:8080/topjava/rest/meals/100004" 

curl -X GET --location "http://localhost:8080/topjava/rest/meals/filter" 

curl -X GET --location "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=11:00:00&endTime=23:00:00"

curl -X POST --location "http://localhost:8080/topjava/rest/meals" -H "Content-Type: application/json; charset=ISO-8859-1"  -d "{\"dateTime\":[2020,2,1,18,0],\"description\":\"Созданный ужин\",\"calories\":300}"

curl -X PUT --location "http://localhost:8080/topjava/rest/meals/100005" -H "Content-Type: application/json; charset=ISO-8859-1" -d "{\"dateTime\":[2020,2,1,10,0],\"description\":\"Созданный ужин\",\"calories\":300}"