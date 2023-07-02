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
import org.springframework.web.bind.annotation.GetMapping;
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
	@GetMapping("/hello")
	public String Greetings(){
		return "Hello from API";
	}

	@PostMapping("/image-to-json")
	public ResponseEntity<InputStreamResource> imageToJsonAPI(@RequestParam("image") MultipartFile image) throws IOException {
		ByteArrayInputStream payload;
		if (image.isEmpty()) {
			payload = new ByteArrayInputStream("Image File is Required".getBytes());
			return ResponseEntity.badRequest().body(new InputStreamResource(payload));
		}
		byte[] imageBytes = image.getBytes();
		String extractedText = imageToText(imageBytes);
		extractedText = (extractedText != null)? textToJson(extractedText): extractedText;
		return createResponse(extractedText, image.getOriginalFilename().split("[.]")[0] + ".json" , MediaType.APPLICATION_JSON);
	}

	@PostMapping("/image-to-text")
	public ResponseEntity<InputStreamResource> imageToTextAPI(@RequestParam("image") MultipartFile image) throws IOException {
		ByteArrayInputStream payload;
		if (image.isEmpty()) {
			payload = new ByteArrayInputStream("No file provided".getBytes());
			return ResponseEntity.badRequest().body(new InputStreamResource(payload));
		}
		byte[] imageBytes = image.getBytes();
		String extractedText = imageToText(imageBytes);
		return createResponse(extractedText, image.getOriginalFilename().split("[.]")[0] + ".txt", MediaType.TEXT_PLAIN);
	}

	private ResponseEntity<InputStreamResource> createResponse(String text, String filename, MediaType mediaType) {
		if (text == null) {
			ByteArrayInputStream payload = new ByteArrayInputStream("Error! provide proper image file".getBytes());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InputStreamResource(payload));
		}

		byte[] byteData = text.getBytes();
		ByteArrayInputStream payload = new ByteArrayInputStream(byteData);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.setContentDispositionFormData("attachment", filename);

		return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new InputStreamResource(payload));
	}

	private String imageToText(byte[] imageBytes) {
		String tesseractModel = "combined";
		Tesseract tesseract = new Tesseract();
		tesseract.setVariable("user_defined_dpi", "300");
		tesseract.setLanguage("eng");
		tesseract.setPageSegMode(1);
		tesseract.setDatapath(String.format("./tessdata/tessdata_%s/", tesseractModel));
		if (tesseractModel.equals("combined")) {
			tesseract.setOcrEngineMode(2);
		} else {
			tesseract.setOcrEngineMode(1);
		}

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			return tesseract.doOCR(image);
		} catch (IOException | TesseractException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String textToJson(String text) {
		JSONObject object = new JSONObject();
		String[] lines = text.split("\\R");
		Pattern pattern = Pattern.compile("\\b[0-9].*\\b");
		for (String line : lines) {
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