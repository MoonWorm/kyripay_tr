package com.kyripay.converter.repository;

import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.DocumentStatus;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@DataMongoTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = DocumentRepositoryTest.Initializer.class)
public class DocumentRepositoryTest
{
  @Autowired
  DocumentRepository repository;

  private final Document testDocument = new Document("1", "IDENTITY", DocumentStatus.READY, "test".getBytes());

  @ClassRule
  public static GenericContainer mongo = new GenericContainer<>("mongo:3.6").withExposedPorts(27017);

  public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
  {
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues values = TestPropertyValues.of(
          "spring.data.mongodb.host=" + mongo.getContainerIpAddress(),
          "spring.data.mongodb.port=" + mongo.getMappedPort(27017)
      );
      values.applyTo(configurableApplicationContext);
    }
  }

  @Rule
  public MongoDbRule embeddedMongoDbRule = new MongoDbRule(
      mongoDb().databaseName("converter").port(mongo.getMappedPort(27017)).build()
  );

  @Test
  public void nothingReturnedForUnknownDocument() {
    assertThat(repository.findById("unknown"), is(Optional.empty()));
  }

  @Test
  @ShouldMatchDataSet(location="/testdata/expectedData.json")
  public void saveAndFindDocument(){
    repository.save(testDocument);
  }

  @Test
  @UsingDataSet(locations="/testdata/expectedData.json")
  public void shouldFindExistingDocument(){
    Optional<Document> document = repository.findById("1");
    assert document.isPresent();
    assertEquals(testDocument, document.get());
  }
}
