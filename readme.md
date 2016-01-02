### Java Lucene REST

A REST project exemplifying finding "similar" objects using Apache Lucene (Java library used by stand-alone server Solr).

#### Implementation
This project defines a "customer" as being composed of the fields: 'First Name', 'Last Name', 'Email', 'Billing Email', and 'Phone Number'

The core technologies used in this project are:

* Java 8
* Maven - for project management
* (XML-less!) Spring MVC - for web services
* HSQLDB - as an embedded data store
* Lucene - for searching and similarity matching

#### Operations Implemented
After downloading or cloning the project, the web services can be run by the command `mvn tomcat7:run` (or by building and deploying another app server).  When running, the following endpoints are accessible:

* `POST /customer` - creates a new customer - returns the new customer instance

example: 

```bash
curl -i -X POST -d '{"firstName":"dylan", "lastName":"moore", "email":"dzmoore@gmail.com", "billingEmail":"billing@example.com", "phoneNumber":"(999) 123-3456"}'   -H "Content-Type: application/json"   http://localhost:8080/customer

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 19 Sep 2015 11:02:52 GMT

{"pk":1,"firstName":"dylan","lastName":"moore","email":"dzmoore@gmail.com","billingEmail":"billing@example.com","phoneNumber":"(999) 123-4567"}
```


* `GET /customer/{customer-pk}` - retrieves a customer instance by a specified pk

example:
```bash
curl -i -X GET http://localhost:8080/customer/1

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 19 Sep 2015 11:03:40 GMT

{"pk":1,"firstName":"dylan","lastName":"moore","email":"dzmoore@gmail.com","billingEmail":"billing@example.com","phoneNumber":"(999) 123-4567"}
```

* `PUT /customer` - updates an existing customer instance with new values - returns a message with whether or not the operation was successful

example:

```bash
curl -i -X PUT -d '{"pk":1,"firstName":"drylan", "lastName":"monore", "email":"email@email", "billingEmail":"billing@email", "phoneNumber":"1234112"}'   -H "Content-Type: application/json"   http://localhost:8080/customer

HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 19 Sep 2015 11:05:48 GMT

{"result":"Update successful.","error":false}

```

* `DELETE /customer/{customer-pk}` - deletes a customer instance by the specified pk - returns a message with whether the operation was successful

example:

```bash
curl -i -X DELETE http://localhost:8080/customer/1
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 19 Sep 2015 11:06:48 GMT

{"result":"Update successful.","error":false}

```

* `GET /index/customers` - updates the index of customer fields

example:

```bash
curl -i -X GET http://localhost:8080/index/customers
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 19 Sep 2015 11:12:28 GMT

{"result":"Customer index started.","error":false}
```

* `GET /customer/similar-to/{customer-pk}/{max-number-of-similar-customers}`

```bash
curl -i -X GET http://localhost:8080/similarity/customer/3/10
HTTP/1.1 200 OK
Server: Apache-Coyote/1.1
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 19 Sep 2015 11:34:53 GMT

[{"pk":5,"firstName":"Harry","lastName":"Richardson","email":"richardson@gmail.com","billingEmail":"richardson@example.com","phoneNumber":"(123) 123-1365"},{"pk":4,"firstName":"Rary","lastName":"Gichardson","email":"richardson@gmail.com","billingEmail":"richardson@example.com","phoneNumber":"(123) 123-1365"}]

```

#### Additional
I initially considered implementing a Levenshtein Distance algorithm to determine the similarity of objects/fields, but then decided that the approach to use a more sophisticated indexing system (Lucene/Solr) would scale much better (and optimizing Levenshtein might require some form of 'indexing' regardless).  The current implementation requires that a re-index command be executed if new customer entries are to be considered in the 'similar' list, but a production version of this project would most likely include periodic re-indexing from some type of cron.  

Additionally, the project could be tweaked to improve the similarity results.  The current implementation uses Levenshtein distance (fuzzy logic with the Lucene queries preceeded by a tilde `~`) with the default threshold (0.5) and all non-null keywords `OR`'d together.  Obviously, this might return similar profiles when customer's names are similar, but this would be an easy 'fix' to define something like first and last names being treated as a single unit, or perhaps even given a lower priority in the search by themselves.
