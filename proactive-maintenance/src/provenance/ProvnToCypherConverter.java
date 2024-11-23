package provenance;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.notation.ProvDeserialiser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ProvnToCypherConverter {

    public void convertProvNToCypher(String filePath, String outputFilePath) {
        // Dosya yolunun geçerli olup olmadığını kontrol et
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return;
        }

        try {
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

            // Çıktı dosyasına Cypher scriptlerini yazmak için FileWriter kullanılıyor
            try (FileWriter writer = new FileWriter(outputFilePath)) {

                // PROV-N dosyasındaki tüm beyanları işle
                for (StatementOrBundle statement : statements) {
                    if (statement instanceof Entity) {
                        Entity entity = (Entity) statement;
                        String cypher = createNodeCypher("Entity", entity.getId().toString());
                        writer.write(cypher + "\n");
                    } else if (statement instanceof Agent) {
                        Agent agent = (Agent) statement;
                        String cypher = createNodeCypher("Agent", agent.getId().toString());
                        writer.write(cypher + "\n");
                    } else if (statement instanceof Activity) {
                        Activity activity = (Activity) statement;
                        String cypher = createNodeCypher("Activity", activity.getId().toString());
                        writer.write(cypher + "\n");
                    } else if (statement instanceof WasGeneratedBy) {
                        WasGeneratedBy wasGeneratedBy = (WasGeneratedBy) statement;
                        String cypher = createRelationshipCypher("Entity", wasGeneratedBy.getEntity().toString(),
                                "Activity", wasGeneratedBy.getActivity().toString(), "WAS_GENERATED_BY");
                        writer.write(cypher + "\n");
                    } else if (statement instanceof Used) {
                        Used used = (Used) statement;
                        String cypher = createRelationshipCypher("Activity", used.getActivity().toString(),
                                "Entity", used.getEntity().toString(), "USED");
                        writer.write(cypher + "\n");
                    } else if (statement instanceof WasDerivedFrom) {
                        WasDerivedFrom wasDerivedFrom = (WasDerivedFrom) statement;
                        String cypher = createRelationshipCypher("Entity", wasDerivedFrom.getGeneratedEntity().toString(),
                                "Entity", wasDerivedFrom.getUsedEntity().toString(), "WAS_DERIVED_FROM");
                        writer.write(cypher + "\n");
                    } else if (statement instanceof WasAssociatedWith) {
                    	 WasAssociatedWith wasAssociatedWith = (WasAssociatedWith) statement;
                    	if(wasAssociatedWith.getAgent()!=null) {
                       
                        String cypher = createRelationshipCypher("Activity", wasAssociatedWith.getActivity().toString(),
                                "Agent", wasAssociatedWith.getAgent().toString(), "WAS_ASSOCIATED_WITH");
                        writer.write(cypher + "\n");
                    }}
                    // Diğer ilişki tipleri burada ele alınabilir.
                }
                System.out.println("Cypher queries written to: " + outputFilePath);
            }

        } catch (Exception e) {
            System.err.println("Error processing the PROV-N file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Neo4j için düğüm oluşturan Cypher sorgusu üretir
    private String createNodeCypher(String label, String id) {
        return "CREATE (n:" + label + " {id: '" + id.replace("'", "\\'") + "'});";
    }

    // Neo4j için ilişki oluşturan Cypher sorgusu üretir
    private String createRelationshipCypher(String label1, String id1, String label2, String id2, String relationship) {
        return "MATCH (a:" + label1 + " {id: '" + id1.replace("'", "\\'") + "'}), (b:" + label2 + " {id: '" + id2.replace("'", "\\'") + "'}) " +
                "CREATE (a)-[r:" + relationship + "]->(b);";
    }

    

    public static void main(String[] args) {
        // Prov-N dosyasının yolu
        String provnFilePath = "D:\\\\prov2neo4j\\\\prov.provn";
        // Cypher sorgularının yazılacağı dosya yolu
        String outputFilePath = "D:\\\\prov2neo4j\\\\cypher_output.cql";

        // Yeni bir converter örneği oluştur ve Prov-N dosyasını Cypher sorgularına çevir
        ProvnToCypherConverter converter = new ProvnToCypherConverter();
        converter.convertProvNToCypher(provnFilePath, outputFilePath);
    }
}
