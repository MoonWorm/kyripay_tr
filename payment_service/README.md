# **kyripay_tr** - Payment Service

## Development
1.) Run docker container for Postgres database:
<pre>
docker run --name postgres_payments_ms -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=payments_ms_user -e POSTGRES_DB=payments_ms -d -p 5432:5432 postgres:11.3
</pre>
2.) Execute Maven command under default profile:
<pre>
mvn clean install
</pre>
3.) Use **com.kyripay.payment.PaymentServiceApplication** main method as an entry point for common Spring Boot Application

## Integration tests
1.) Run docker container for Postgres database:
<pre>
docker run --name postgres_payments_ms -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=payments_ms_user -e POSTGRES_DB=payments_ms -d -p 5432:5432 postgres:11.3
</pre>
2.) Execute Maven command under **integration** profile:
<pre>
mvn clean install -Pintegration
</pre>

## Docker compose
1.) Run docker container for Postgres database:
<pre>
docker run --name postgres_payments_ms -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=payments_ms_user -e POSTGRES_DB=payments_ms -d -p 5432:5432 postgres:11.3
</pre>
2.) Execute Maven command under default profile:
<pre>
mvn clean install
</pre>
3.) Ensure that Docker container for Postgres database is stopped:
<pre>
docker container stop postgres_payments_ms
</pre>
4.) Execute the next command from the root project folder to run both API service and Postgres database using Docker:
<pre>
docker-compose up
</pre>
or (background mode)
<pre>
docker-compose up -b
</pre>
5.) To stop docker containers execute the next command from the root project folder:
<pre>
docker-compose down
</pre>

## Access database via psql CLI:

<pre>
psql postgresql://payments_ms_user:12345@localhost:5432/payments_ms
</pre>