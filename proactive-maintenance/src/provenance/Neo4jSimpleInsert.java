package provenance;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class Neo4jSimpleInsert implements AutoCloseable {
    private final Driver driver;

    // Constructor - Veritabanı bağlantısı için
    public Neo4jSimpleInsert(String uri, String user, String password) {

        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() {
        driver.close();
    }

    // Veritabanına bir "Person" düğümü ekleyen metot
    public void addPerson(final String name) {
       Session session = driver.session();
       System.out.println("Person adding: " + name);
            session.run("CREATE (a:Person {name: $name})", parameters("name", name));
                
          
            System.out.println("Person added: " + name);
        
    }

    public static void main(String... args) {
        // Neo4j veritabanına bağlan
        try (Neo4jSimpleInsert app = new Neo4jSimpleInsert("bolt://localhost:7687", "neo4j", "e0125527")) {
            // "Person" düğümü ekle
            app.addPerson("John Doe");
        }
    }
}
