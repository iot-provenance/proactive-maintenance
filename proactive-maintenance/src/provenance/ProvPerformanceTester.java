package provenance;

import org.neo4j.driver.*;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ProvPerformanceTester {
    private final Driver neo4jDriver;
    
    public ProvPerformanceTester(String uri, String user, String password) {
        this.neo4jDriver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void storeDocument2(String provnDoc) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase("neo4j"))) { // Specify database here
            session.writeTransaction(tx -> {
                tx.run("CREATE (:ProvDoc {content: $content})", 
                        Values.parameters("content", provnDoc));
                return null;
            });
        }
    }
    
    String message2 = "CREATE (n:Agent {id: 'pr:{provns:}SOFTWARE'}); " +
            "CREATE (n:Agent {id: 'pr:{provns:}USER'}); " +
            "CREATE (n:Entity {id: 'pr:{provns:}IOT_DATA'}); " +
            "CREATE (n:Activity {id: 'pr:{provns:}DATA_COLLECTION'}); " +
            "MATCH (a:Entity {id: 'pr:{provns:}IOT_DATA'}), (b:Activity {id: 'pr:{provns:}DATA_COLLECTION'}) CREATE (a)-[r:WAS_GENERATED_BY]->(b); "; // Diğer sorgular devam eder
   
    
    public void storeDocument(String message) {
        try (Session session = neo4jDriver.session()) {
          
            // Sorguları bölün ve her birini ayrı çalıştırın
            String[] queries = message2.split(";");

            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    try {
                        session.run(query);
                    } catch (Exception e) {
                        System.out.println("Error executing query: " + query);
                        System.out.println("Error message: " + e.getLocalizedMessage());
                    }
                }
            }

            System.out.println("Execution completed");
        } catch (Exception e) {
            System.out.println("Session error: " + e.getLocalizedMessage());
        }
    }

    public void storeDocument3(String message) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase("neo4j"))) {
        	
        	   System.out.println(message);
        	   try {
        	 session.run(message);
        	   }
        	   catch(Exception e) {
        		   
        		   System.out.println("error"+e.getLocalizedMessage());
        	   }
            System.out.println("executed");
        }}

    // Retrieve Prov-O document from Neo4j (specify the provenance database)
    public String retrieveDocument() {
       return "111";
    }


    // Performance Test: Measure average and standard deviation for store/retrieve times
    public void runPerformanceTest(List<String> provnDocs) {
        int iterations = 10;
        List<Long> storeTimes = new ArrayList<>();
        List<Long> retrieveTimes = new ArrayList<>();
        
        for (String provnDoc : provnDocs) {
            for (int i = 0; i < iterations; i++) {
            	
                System.out.println("iteration: " + i+1 );
                // Measure store time
                long startStore = System.nanoTime();
                storeDocument(provnDoc);
                System.out.println("document stored: " + i+1 );
                long endStore = System.nanoTime();
                storeTimes.add(endStore - startStore);
                
                // Measure retrieve time
                long startRetrieve = System.nanoTime();
                retrieveDocument();
                System.out.println("document retrieved: " + i+1 );
                long endRetrieve = System.nanoTime();
                retrieveTimes.add(endRetrieve - startRetrieve);
            }
        }

        // Calculate average and standard deviation for store times
        long avgStoreTime = calculateAverage(storeTimes);
        double stdStoreTime = calculateStandardDeviation(storeTimes, avgStoreTime);
        
        // Calculate average and standard deviation for retrieve times
        long avgRetrieveTime = calculateAverage(retrieveTimes);
        double stdRetrieveTime = calculateStandardDeviation(retrieveTimes, avgRetrieveTime);
        
        // Print the results
        System.out.println("Store Time - Average: " + avgStoreTime + " ns, Standard Deviation: " + stdStoreTime + " ns");
        System.out.println("Retrieve Time - Average: " + avgRetrieveTime + " ns, Standard Deviation: " + stdRetrieveTime + " ns");
    }

    // Helper method to calculate average
    private long calculateAverage(List<Long> times) {
        return times.stream().mapToLong(Long::longValue).sum() / times.size();
    }

    // Helper method to calculate standard deviation
    private double calculateStandardDeviation(List<Long> times, long mean) {
        return Math.sqrt(times.stream().mapToDouble(time -> Math.pow(time - mean, 2)).sum() / times.size());
    }

    // Close Neo4j driver
    public void close() {
        neo4jDriver.close();
    }

    public static void main(String[] args) {
    	ProvPerformanceTester tester = new ProvPerformanceTester("bolt://localhost:7687", "neo4j", "e0125527");
        
        // Example Prov-O documents (use actual docs or load them)

        // Example of loading and testing one CQL file at a time
        String provnDoc1;
		try {
			provnDoc1 = tester.loadProvODocumentFromCQL("D:\\prov2neo4j\\1.cql");
		
        String provnDoc2 = tester.loadProvODocumentFromCQL("D:\\prov2neo4j\\1.cql");
        String provnDoc3 = tester.loadProvODocumentFromCQL("D:\\prov2neo4j\\1.cql");

        // Prepare list of documents for testing
        List<String> provnDocs = new ArrayList<>();
        provnDocs.add(provnDoc1);
        provnDocs.add(provnDoc2);
        provnDocs.add(provnDoc3);
        // Run the performance test
        tester.runPerformanceTest(provnDocs);
        
        // Close the connection
        tester.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    // Method to load a single Prov-O document from a CQL file
    public String loadProvODocumentFromCQL(String filePath) throws IOException {
        // Read the file content and return as a string
        return new String(Files.readAllBytes(Paths.get(filePath))).trim(); // Read the file and trim extra spaces
    }
}
