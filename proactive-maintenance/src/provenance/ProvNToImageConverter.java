package provenance;



import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;



public class ProvNToImageConverter {

	public static void executeProvConvertCommand(String inputProvNFilePath, String outputSvgFilePath)
            throws IOException, InterruptedException {
        // Construct the command
        String command = String.format("D:\\ProvToolbox\\bin\\provconvert.bat --infile \"%s\" --outfile \"%s\"", inputProvNFilePath, outputSvgFilePath);

        // Execute the command
        Process process = Runtime.getRuntime().exec(command);

        // Read the output from the command
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Read the error stream
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("provconvert command failed with exit code " + exitCode);
        }
    }


	public static void convertSvgToPng(String svgFilePath, String pngFilePath) {
   
        
        // Desired width and height for the output PNG
        float pngWidth = 1200f;
        float pngHeight = 1200f;

        try {
            // Create a PNG transcoder
            PNGTranscoder transcoder = new PNGTranscoder();

            // Set the desired width and height
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, pngWidth);
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, pngHeight);

            // Create the input and output streams
            FileInputStream inputStream = new FileInputStream(new File(svgFilePath));
            FileOutputStream outputStream = new FileOutputStream(new File(pngFilePath));

            // Create the transcoder input and output
            TranscoderInput input = new TranscoderInput(inputStream);
            TranscoderOutput output = new TranscoderOutput(outputStream);

            // Perform the transcoding
            transcoder.transcode(input, output);

            // Close the streams
            inputStream.close();
            outputStream.close();

            System.out.println("SVG file converted to PNG successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
