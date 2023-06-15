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
	public ResponseEntity<InputStreamResource> convertImageToText(@RequestParam("imageFile") MultipartFile imageFile) {
		ByteArrayInputStream bis;
		if (imageFile.isEmpty()) {
			bis = new ByteArrayInputStream("Image File is Required".getBytes());
			return ResponseEntity.badRequest().body(new InputStreamResource(bis));
		}
		try {
			byte[] imageBytes = imageFile.getBytes();
			String text = performImageToTextConversion(imageBytes);
			byte[] byteData = text.getBytes();
			bis = new ByteArrayInputStream(byteData);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			headers.setContentDispositionFormData("attachment", "report.txt");
			return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bis));
		} catch (IOException e) {
			e.printStackTrace();
			bis = new ByteArrayInputStream("Error processing image file.".getBytes());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InputStreamResource(bis));
		}
	}

	private String performImageToTextConversion(byte[] imageBytes) {
		Tesseract tesseract = new Tesseract();
		tesseract.setVariable("user_defined_dpi", "300");
		tesseract.setLanguage("eng");
		tesseract.setPageSegMode(1);
		tesseract.setOcrEngineMode(1);
		tesseract.setDatapath("./tessdata/tessdata_best/");

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			return tesseract.doOCR(image);
		} catch (IOException | TesseractException e) {
			e.printStackTrace();
			return "Error extracting text from the image.";
		}
	}
}