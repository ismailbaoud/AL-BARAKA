package com.ismail.al_baraka.service.impliment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ismail.al_baraka.dto.document.request.DocumentRequest;
import com.ismail.al_baraka.dto.document.response.DocumentResponse;
import com.ismail.al_baraka.mapper.DocumentMapper;
import com.ismail.al_baraka.model.Document;
import com.ismail.al_baraka.model.Operation;
import com.ismail.al_baraka.repository.DocumentRepository;
import com.ismail.al_baraka.repository.OperationRepository;
import com.ismail.al_baraka.service.DocumentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final OperationRepository operationRepository;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    @Transactional
    public DocumentResponse uploadDocument(DocumentRequest request, Long operationId) {
        validateFile(request.getFile());
        Operation operation = operationRepository.findById(operationId).orElseThrow();
        String originalFilename = request.getFile().getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = Instant.now().toEpochMilli() + "_" + originalFilename;

        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        Path filePath = uploadPath.resolve(uniqueFilename);
        try {
            Files.copy(request.getFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not save file: " + e.getMessage(), e);
        }

        Document document = new Document();
        document.setFileName(originalFilename);
        document.setFileType(fileExtension);
        document.setFilePath(filePath.toString());
        document.setOperation(operation);

        Document savedDocument = documentRepository.save(document);
        return documentMapper.toResponse(savedDocument);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException(
                    String.format("File size exceeds 5MB limit. Current size: %.2f MB",
                            file.getSize() / (1024.0 * 1024.0))
            );
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (contentType == null) {
            throw new RuntimeException("Could not determine file type");
        }

        boolean isValidType = contentType.equals("application/pdf") ||
                contentType.startsWith("image/jpeg") ||
                contentType.startsWith("image/jpg") ||
                contentType.startsWith("image/png");

        if (!isValidType) {
            throw new RuntimeException(
                    "Invalid file type. Only PDF, JPG, and PNG files are allowed"
            );
        }

        if (originalFilename == null || originalFilename.contains("..")) {
            throw new RuntimeException("Invalid filename");
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}
