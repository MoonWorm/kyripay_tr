# How to run (service + postgres + pgadmin):
* gradle bootJar
* docker-compose up --build

# Useful links & flow of consciousness

* [Lombok onX](https://projectlombok.org/features/experimental/onX)
* [Basic CRUD JPA sample](https://projectlombok.org/features/experimental/onX)
* [Cabling H2 database](https://www.baeldung.com/spring-boot-h2-database)
* [Pagination - JPA](https://www.baeldung.com/jpa-pagination)
* [Spring Data JPA @Query](https://www.baeldung.com/spring-data-jpa-query)
* [DB - initial data loading](https://www.baeldung.com/spring-boot-data-sql-and-schema-sql)
* [DB Init](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html)


**@RestController vs @RepositoryRestResource vs @RepositoryRestController**
@RepositoryRestResource - to annotate repository - "to customize export mapping and rels."
@RepositoryRestResource is used to set options on the public Repository interface - 
it will automatically create endpoints as appropriate based on the type of Repository that is being extended 
(i.e. CrudRepository/PagingAndSortingRepository/etc).

@BasePathAwareController and @RepositoryRestController are used when you want to manually create endpoints, 
but want to use the Spring Data REST configurations that you have set up.

@RestController, you will create a parallel set of endpoints with different configuration options - i.e. 
a different message converter, different error handlers, etc - but they will happily coexist (and probably cause confusion).

[@RestController vs @RepositoryRestResource](https://stackoverflow.com/questions/22824840/when-to-use-restcontroller-vs-repositoryrestresource/23183168)
[Projections](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#projections-excerpts)
[Projections](https://www.baeldung.com/spring-data-rest-projections-excerpts)
[Wow! You can make SDR repository finders return projections by default:](https://stackoverflow.com/questions/33288486/using-a-spring-data-rest-projection-as-a-representation-for-a-resource-in-a-cus)

[Damn!In Spring data rest is possible to add nested @OneToMany object to parent over POST /parent/{parentId}/nested?](https://stackoverflow.com/questions/37278662/in-spring-data-rest-is-possible-to-add-nested-onetomany-object-to-parent-over-p)
No.
[ "The association resource" ](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#repository-resources.association-resource)
The POST method is supported only for collection associations. It adds a new element to the collection.



Spring Data REST finds projection definitions as follows:
    Any @Projection interface found in the same package as your entity definitions (or one of its sub-packages) is registered.
    You can manually register a projection by using RepositoryRestConfiguration.getProjectionConfiguration().addProjection(…).

[Last mod date for cache headers](https://docs.spring.io/spring-data/rest/docs/current/reference/html/#conditional.if-modified-since)
@LastModifiedDate
Consider implementing optimistic locking via versioning and using Version as ETag

[Documenting a Spring Data REST API with Springfox and Swagger](https://reflectoring.io/documenting-spring-data-rest-api-with-springfox/)
    
    
[JPA One-to-many-1](https://www.baeldung.com/hibernate-one-to-many)    
[JPA One-to-many-2](https://hellokoding.com/jpa-one-to-many-relationship-mapping-example-with-spring-boot-maven-and-mysql/)
[JPA One-to-many-3](https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/)
[JPA map Map :)](https://reviewdb.io/questions/1505120348934/how-to-store-map-string-string-using-jpa)     

[Manual HATEOAS responses - simple sample](https://stackoverflow.com/questions/25858698/spring-hateoas-embedded-resource-support)


BUG? app start - get traces collection - I get one of:
-Pagination OK + excerpt projection OK + NO links to headers (NOK)
-No pagination, no projection, but links are present
Resolved - there were 2 repositories for the same entity type. Shame on me.

Faced this shit: [DATAREST-713 Use Entity ResourceProcessor when Serializing Projection](https://jira.spring.io/browse/DATAREST-713)
[Workaround](https://stackoverflow.com/questions/47048099/spring-data-rest-resourceprocessor-not-applied-on-projections)

Configuring additional message converters
[1](https://stackoverflow.com/questions/34172163/spring-boot-how-to-custom-httpmessageconverter)
[2 Official](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-message-converters)

[DAMN! Springfox-data-rest doesn't support Spring boot 2](https://github.com/springfox/springfox/issues/2298)
...and this will be fixed in 3.X. The latest version is 2.9.2. 3.0 is not released yet. 3.0.0-SNAPSHOT doesn't work as well...

[asciidoc shortcuts](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#working-with-asciidoctor-including-snippets-operation)
operation::index[snippets='curl-request,http-request,http-response']

[Spring boot testing](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
[String boot testing - 2](https://spring.io/guides/gs/accessing-data-rest/)
[@DirtiesContext can resolve db cleanup issue in tests](https://stackoverflow.com/questions/34617152/how-to-re-create-database-before-each-test-in-spring)

[RestDocs - Tune headers / replace patterns / etc](https://github.com/spring-projects/spring-restdocs/blob/master/docs/src/docs/asciidoc/customizing-requests-and-responses.adoc#customizing-requests-and-responses-preprocessors)
It is to document only headers that are important in the operation context; to replace (probably) `localhost:{random_port}` to some predefined value  

[Swagger: @Api description - deprecated](https://github.com/swagger-api/swagger-core/issues/1476)
[Swagger anno 1.5.x](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X#swaggerdefinition)

[Validation 1](https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/)
[Validation 2](https://github.com/swagger-api/swagger-codegen/issues/5688)
[Validation 3 Official - it doesn't work this way...](https://www.baeldung.com/spring-boot-bean-validation)

  
 
[Soring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)
[@EnableJpaAuditing](https://springbootdev.com/2018/03/13/spring-data-jpa-auditing-with-createdby-createddate-lastmodifiedby-and-lastmodifieddate/)       
 
 
[Instaling Minikube inside VirtualBox](https://medium.com/@vovaprivalov/setup-minikube-on-virtualbox-7cba363ca3bc) 

[Just released minikube 1.2.0](https://github.com/kubernetes/minikube/releases/tag/v1.2.0)

[Kuber apiVersions explained](https://matthewpalmer.net/kubernetes-app-developer/articles/kubernetes-apiversion-definition-guide.html)

[Kuber concepts, offdocs](https://kubernetes.io/docs/concepts/configuration/overview/)

[Kuber uninstall](https://gist.github.com/robinkraft/a0987b50de8b45e4bdc907d841db8f23)

[Kuber uninstall 2](https://stackoverflow.com/questions/49364619/removing-docker-kubernetes-k8s-images-from-local-machine)

[client / server version discrepancy issues](https://stackoverflow.com/questions/51180147/determine-what-resource-was-not-found-from-error-from-server-notfound-the-se)

[kubectl install](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

[dashboard on none: unexpected response code: 503](https://github.com/kubernetes/minikube/issues/4352)

[Postgres in Kubernetes](https://severalnines.com/blog/using-kubernetes-deploy-postgresql)

Cyber-dojo

ContainerDatabaseDriver

AssertJ

@SpringBootTest(classes=...)

In test context:
@TestConfiguration - ADD to context; @Configuration - override the entire context

@DataJpaTest

@TestEntityManager

@RestClientTest (MockRestServiceServer)

WireMockRule
MockLab (dump req/resp)

SpringMessageTest
Spring messaging

Spring cloud contracts (+ Pact?)

k8s+Helm
k8s: service mash / istio (~ LB by header...)
CircleCI, teamcity


