package ehospital.lis.imageOCR;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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

@Controller
public class FileController {
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
}