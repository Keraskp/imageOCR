package ehospital.lis.imageOCR;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class FileController {

	@PostMapping("/image-to-json")
	public ResponseEntity<InputStreamResource> jsonOcrApi(@RequestParam("image") MultipartFile image) {
		ByteArrayInputStream payload;
		if (image.isEmpty()) {
			payload = new ByteArrayInputStream("Image File is Required".getBytes());
			return ResponseEntity.badRequest().body(new InputStreamResource(payload));
		}
		try {
			byte[] imageBytes = image.getBytes();
			String extractedtext = imageToText(imageBytes);
			extractedtext = textToJson(extractedtext);
			byte[] byteData = extractedtext.getBytes();
			payload = new ByteArrayInputStream(byteData);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setContentDispositionFormData("attachment", "report.json");

			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(payload));

		} catch (IOException e) {
			e.printStackTrace();
			payload = new ByteArrayInputStream("Error processing image file.".getBytes());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InputStreamResource(payload));
		}

	}


	@PostMapping("/image-to-text")
	public ResponseEntity<InputStreamResource> imageOcrApi(@RequestParam("image") MultipartFile image) {
		ByteArrayInputStream payload;
		if (image.isEmpty()) {
			payload = new ByteArrayInputStream("Image File is Required".getBytes());
			return ResponseEntity.badRequest().body(new InputStreamResource(payload));
		}
		try {
			byte[] imageBytes = image.getBytes();
			String extractedtext = imageToText(imageBytes);
			byte[] byteData = extractedtext.getBytes();
			payload = new ByteArrayInputStream(byteData);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			headers.setContentDispositionFormData("attachment", "report.txt");

			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(payload));

		} catch (IOException e) {
			e.printStackTrace();
			payload = new ByteArrayInputStream("Error processing image file.".getBytes());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InputStreamResource(payload));
		}
	}

	private String imageToText(byte[] imageBytes) {
		String tesseractModel = "combined";
		Tesseract tesseract = new Tesseract();
		tesseract.setVariable("user_defined_dpi", "300");
		tesseract.setLanguage("eng");
		tesseract.setPageSegMode(1);
		tesseract.setDatapath(String.format("./tessdata/tessdata_%s/", tesseractModel));
		if (tesseractModel.equals("combined"))
			tesseract.setOcrEngineMode(2);
		else
			tesseract.setOcrEngineMode(1);

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			return tesseract.doOCR(image);
		} catch (IOException | TesseractException e) {
			e.printStackTrace();
			return "Error extracting text from the image.";
		}
	}

	public String textToJson(String text){
		JSONObject object = new JSONObject();
		String [] lines = text.split("\\R");
		Pattern pattern = Pattern.compile("\\b[0-9].*\\b");
		for(String line : lines){
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				int idx = matcher.start();
				String value = matcher.group();
				String key = line.substring(0, idx);
				object.put(key, value);
			}
		}
		return object.toString();
	}
}