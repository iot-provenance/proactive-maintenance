package provenance;
import org.neo4j.driver.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ProvnPerformanceTester {
    private final Driver neo4jDriver;
    private final Random random = new Random();
    
    public ProvnPerformanceTester(String uri, String user, String password) {
        this.neo4jDriver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    // Method to load a single Prov-O document from a CQL file
    public String loadProvODocumentFromCQL(String filePath) throws IOException {
        // Read the file content and return as a string
        return new String(Files.readAllBytes(Paths.get(filePath))).trim(); // Read the file and trim extra spaces
    }

    // Store Prov-O document in Neo4j
    public void storeDocument(String provnDoc) {
        try (Session session = neo4jDriver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (:ProvDoc {content: $content})", 
                        Values.parameters("content", provnDoc));
                return null;
            });
        }
    }

    // Retrieve Prov-O document from Neo4j
    public String retrieveDocument() {
        try (Session session = neo4jDriver.session()) {
            return session.readTransaction(tx -> {
                Result result = tx.run("MATCH (d:ProvDoc) RETURN d.content LIMIT 1");
                return result.single().get("d.content").asString();
            });
        }
    }

    // Performance Test: Single-threaded, measuring store/retrieve time for Prov-O documents
    public void performanceTest(List<String> provDocs) {
        int iterations = 100;
        
        for (String provDoc : provDocs) {
            List<Long> storeTimes = new ArrayList<>();
            List<Long> retrieveTimes = new ArrayList<>();
            
            for (int i = 0; i < iterations; i++) {
                long storeStart = System.nanoTime();
                storeDocument(provDoc);
                long storeEnd = System.nanoTime();
                storeTimes.add(storeEnd - storeStart);
                
                long retrieveStart = System.nanoTime();
                retrieveDocument();
                long retrieveEnd = System.nanoTime();
                retrieveTimes.add(retrieveEnd - retrieveStart);
            }
            
            // Calculate average and standard deviation for store and retrieve times
            double avgStoreTime = storeTimes.stream().mapToLong(Long::longValue).average().orElse(0);
            double avgRetrieveTime = retrieveTimes.stream().mapToLong(Long::longValue).average().orElse(0);
            
            double stdDevStore = calculateStandardDeviation(storeTimes, avgStoreTime);
            double stdDevRetrieve = calculateStandardDeviation(retrieveTimes, avgRetrieveTime);
            
            System.out.println("Document size: " + provDoc.length() + " chars");
            System.out.println("Average Store Time: " + avgStoreTime / 1_000_000.0 + " ms");
            System.out.println("Standard Deviation Store Time: " + stdDevStore / 1_000_000.0 + " ms");
            System.out.println("Average Retrieve Time: " + avgRetrieveTime / 1_000_000.0 + " ms");
            System.out.println("Standard Deviation Retrieve Time: " + stdDevRetrieve / 1_000_000.0 + " ms");
            System.out.println("--------------------------------------------");
        }
    }

    // Scalability Test: Multithreaded, using 10 threads with each thread making 100 requests
    public void scalabilityTest(List<String> provDocs) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            tasks.add(() -> {
                for (int j = 0; j < 100; j++) {
                    String provDoc = provDocs.get(random.nextInt(provDocs.size()));
                    long storeStart = System.nanoTime();
                    storeDocument(provDoc);
                    long storeEnd = System.nanoTime();
                    
                    long retrieveStart = System.nanoTime();
                    retrieveDocument();
                    long retrieveEnd = System.nanoTime();
                    
                    // You can also record these times for each thread for detailed analysis
                    long storeTime = storeEnd - storeStart;
                    long retrieveTime = retrieveEnd - retrieveStart;
                    // Do something with the store and retrieve times (e.g., log or save them)
                }
                return null;
            });
        }
        
        long testStart = System.nanoTime();
        List<Future<Void>> results = executorService.invokeAll(tasks);
        long testEnd = System.nanoTime();
        
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        double totalTestTime = (testEnd - testStart) / 1_000_000.0;
        System.out.println("Total time for 10 threads performing 100 requests each: " + totalTestTime + " ms");
    }

    // Helper function to calculate the standard deviation
    private double calculateStandardDeviation(List<Long> times, double avgTime) {
        double sum = 0;
        for (long time : times) {
            sum += Math.pow((time - avgTime), 2);
        }
        return Math.sqrt(sum / times.size());
    }

    // Close the Neo4j connection
    public void close() {
        neo4jDriver.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ProvnPerformanceTester tester = new ProvnPerformanceTester("neo4j://localhost:7687", "neo4j", "password");

        // Example of loading and testing one CQL file at a time
        String provnDoc1 = tester.loadProvODocumentFromCQL("path/to/your/file1.cql");
        String provnDoc2 = tester.loadProvODocumentFromCQL("path/to/your/file2.cql");
        String provnDoc3 = tester.loadProvODocumentFromCQL("path/to/your/file3.cql");

        // Prepare list of documents for testing
        List<String> provnDocs = new ArrayList<>();
        provnDocs.add(provnDoc1);
        provnDocs.add(provnDoc2);
        provnDocs.add(provnDoc3);

        // Run the performance test
        tester.performanceTest(provnDocs);

        // Run the scalability test
        tester.scalabilityTest(provnDocs);

        // Close the Neo4j connection
        tester.close();
    }
}

