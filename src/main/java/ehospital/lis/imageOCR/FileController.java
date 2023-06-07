package ehospital.lis.imageOCR;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<String> convertImageToText(@RequestParam("imageFile") MultipartFile imageFile) {
		if (imageFile.isEmpty()) {
			return ResponseEntity.badRequest().body("Image file is required.");
		}

		try {
			byte[] imageBytes = imageFile.getBytes();
			String text = performImageToTextConversion(imageBytes);
			return ResponseEntity.ok(text);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the image file.");
		}
	}

	private String performImageToTextConversion(byte[] imageBytes) {
		Tesseract tesseract = new Tesseract();
		tesseract.setVariable("user_defined_dpi", "300");
		tesseract.setLanguage("eng");
		tesseract.setPageSegMode(1);
		tesseract.setOcrEngineMode(2);
		tesseract.setDatapath("E:/tess_data_combined/");
		// tessdata_best OCR EngineMode (1) for NeuralNet LSTMS
		// tess_data_combined OCR Engine Mode (20 for LSTMS and Legacy

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			return tesseract.doOCR(image);
		} catch (IOException | TesseractException e) {
			e.printStackTrace();
			return "Error extracting text from the image.";
		}
	}
}