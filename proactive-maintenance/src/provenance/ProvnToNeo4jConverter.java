package provenance;

import org.neo4j.driver.*;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.notation.ProvDeserialiser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ProvnToNeo4jConverter {
    private final Driver driver;

    public ProvnToNeo4jConverter(String uri, String user, String password) {
        // Bağlantı testi
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
            System.out.println("Connected to Neo4j database successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Neo4j database. Please check your connection details.", e);
        }
    }

    // Close the driver connection
    public void close() {
        driver.close();
    }

    public void convertProvNToNeo4j(String filePath) {
        // Dosya yolunun geçerli olup olmadığını kontrol et
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        try (Session session = driver.session()) {
            // Dosyanın var olduğunu gösteren çıktı
            System.out.println("Processing file: " + filePath);

            // Prov-N dosyasını deserialise et
            org.openprovenance.prov.xml.ProvFactory defaultFactory = new org.openprovenance.prov.xml.ProvFactory();
            ProvDeserialiser deserialiser = new ProvDeserialiser(defaultFactory);
            InputStream in = new FileInputStream(file);
            Document provDocument = deserialiser.deserialiseDocument(in);

            // Statement'ları çek ve boş olup olmadığını kontrol et
            List<StatementOrBundle> statements = provDocument.getStatementOrBundle();
            System.out.println("Total statements found: " + statements.size());

            if (statements.isEmpty()) {
                System.out.println("No statements found in the document.");
                return;
            }

            // PROV-N dosyasındaki tüm beyanları işle
            for (StatementOrBundle statement : statements) {
                if (statement instanceof Entity) {
                    Entity entity = (Entity) statement;
                    createNode(session, "Entity", entity.getId().toString());
                } else if (statement instanceof Agent) {
                    Agent agent = (Agent) statement;
                    createNode(session, "Agent", agent.getId().toString());
                } else if (statement instanceof Activity) {
                    Activity activity = (Activity) statement;
                    createNode(session, "Activity", activity.getId().toString());
                } else if (statement instanceof WasGeneratedBy) {
                    WasGeneratedBy wasGeneratedBy = (WasGeneratedBy) statement;
                    createRelationship(session, "Entity", wasGeneratedBy.getEntity().toString(),
                            "Activity", wasGeneratedBy.getActivity().toString(), "WAS_GENERATED_BY");
                }
            }

        } catch (Exception e) {
            System.err.println("Error processing the PROV-N file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Neo4j'ye düğüm oluştur
    private void createNode(Session session, String label, String id) {
        // Test için CREATE kullanılıyor
        session.run("CREATE (n:" + label + " {id: $id})", Values.parameters("id", id));
        System.out.println("Created node with label: " + label + " and id: " + id);
    }

    // Neo4j'ye ilişki oluştur
    private void createRelationship(Session session, String label1, String id1, String label2, String id2, String relationship) {
        // Test için MATCH ile bulunan düğümler arasında ilişki yarat
        session.run("MATCH (a:" + label1 + " {id: $id1}), (b:" + label2 + " {id: $id2}) " +
                        "CREATE (a)-[r:" + relationship + "]->(b)",
                Values.parameters("id1", id1, "id2", id2));
        System.out.println("Created relationship: " + relationship + " between " + label1 + " (id: " + id1 + ") and " + label2 + " (id: " + id2 + ")");
    }

    public static void main(String[] args) {
        // Neo4j bağlantı bilgileri
        String uri = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "e0125527";

        // Prov-N dosyasının yolu
        String provnFilePath = "D:\\\\prov2neo4j\\\\prov.provn";

        // Yeni bir converter örneği oluştur ve Prov-N dosyasını yükle
        ProvnToNeo4jConverter converter = new ProvnToNeo4jConverter(uri, user, password);
        converter.convertProvNToNeo4j(provnFilePath);

        // Bağlantıyı kapat
        converter.close();
    }
}
