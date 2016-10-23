run:
	sbt run
zipkin:
	docker run -d -p 127.0.0.1:9411:9411 openzipkin/zipkin
