package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.controller;

import MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            long originalSize = file.getSize();
            String compressedImage = imageService.compressAndEncodeImage(file);
            long compressedSize = imageService.getImageSizeInBytes(compressedImage);
            
            double compressionRatio = ((double)(originalSize - compressedSize) / originalSize) * 100;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("image", compressedImage);
            response.put("originalSize", originalSize);
            response.put("compressedSize", compressedSize);
            response.put("compressionRatio", String.format("%.1f%%", compressionRatio));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/compress")
    public ResponseEntity<Map<String, Object>> compressBase64Image(@RequestBody Map<String, String> request) {
        try {
            String base64Image = request.get("image");
            long originalSize = imageService.getImageSizeInBytes(base64Image);
            String compressedImage = imageService.compressBase64Image(base64Image);
            long compressedSize = imageService.getImageSizeInBytes(compressedImage);
            
            double compressionRatio = ((double)(originalSize - compressedSize) / originalSize) * 100;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("image", compressedImage);
            response.put("originalSize", originalSize);
            response.put("compressedSize", compressedSize);
            response.put("compressionRatio", String.format("%.1f%%", compressionRatio));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}