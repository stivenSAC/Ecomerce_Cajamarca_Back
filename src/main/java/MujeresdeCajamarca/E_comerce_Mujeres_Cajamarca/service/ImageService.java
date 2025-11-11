package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;
    private static final int SMALL_WIDTH = 600;
    private static final int SMALL_HEIGHT = 400;
    private static final float HIGH_QUALITY = 0.8f;
    private static final float MEDIUM_QUALITY = 0.6f;
    private static final float LOW_QUALITY = 0.4f;
    private static final long MAX_SIZE_BYTES = 200 * 1024; // 200KB para Base64

    public String compressAndEncodeImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (!isValidImageType(contentType)) {
            throw new IllegalArgumentException("Tipo de archivo no válido. Solo se permiten JPG, PNG, WEBP");
        }

        return compressImageAdaptive(file.getInputStream(), file.getSize());
    }

    public String compressBase64Image(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("La imagen Base64 está vacía");
        }

        // Extraer datos de la imagen Base64
        String[] parts = base64Image.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato Base64 inválido");
        }

        byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        
        return compressImageAdaptive(inputStream, imageBytes.length);
    }

    private String compressImageAdaptive(java.io.InputStream inputStream, long originalSize) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Determinar calidad basada en el tamaño original
        float quality = determineQuality(originalSize);
        
        // Primera compresión
        Thumbnails.of(inputStream)
                .size(MAX_WIDTH, MAX_HEIGHT)
                .outputQuality(quality)
                .outputFormat("jpg")
                .toOutputStream(outputStream);
        
        byte[] compressedBytes = outputStream.toByteArray();
        
        // Si aún es muy grande, comprimir más agresivamente
        if (compressedBytes.length > MAX_SIZE_BYTES) {
            outputStream.reset();
            ByteArrayInputStream tempInput = new ByteArrayInputStream(compressedBytes);
            
            Thumbnails.of(tempInput)
                    .size(SMALL_WIDTH, SMALL_HEIGHT)
                    .outputQuality(LOW_QUALITY)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);
            
            compressedBytes = outputStream.toByteArray();
            
            // Si TODAVÍA es muy grande, comprimir aún más
            if (compressedBytes.length > MAX_SIZE_BYTES) {
                outputStream.reset();
                tempInput = new ByteArrayInputStream(compressedBytes);
                
                Thumbnails.of(tempInput)
                        .size(400, 300)
                        .outputQuality(0.3f)
                        .outputFormat("jpg")
                        .toOutputStream(outputStream);
                
                compressedBytes = outputStream.toByteArray();
            }
        }
        
        String base64Image = Base64.getEncoder().encodeToString(compressedBytes);
        String result = "data:image/jpeg;base64," + base64Image;
        
        System.out.println("Imagen comprimida - Tamaño final: " + result.length() + " caracteres");
        System.out.println("Tamaño en bytes: " + compressedBytes.length + " bytes");
        
        return result;
    }
    
    private float determineQuality(long sizeInBytes) {
        if (sizeInBytes < 100 * 1024) { // < 100KB
            return HIGH_QUALITY;
        } else if (sizeInBytes < 500 * 1024) { // < 500KB
            return MEDIUM_QUALITY;
        } else {
            return LOW_QUALITY; // >= 500KB
        }
    }
    
    private boolean isValidImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/webp") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/bmp")
        );
    }

    public long getImageSizeInBytes(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return 0;
        }
        
        String[] parts = base64Image.split(",");
        if (parts.length != 2) {
            return 0;
        }
        
        return Base64.getDecoder().decode(parts[1]).length;
    }
}