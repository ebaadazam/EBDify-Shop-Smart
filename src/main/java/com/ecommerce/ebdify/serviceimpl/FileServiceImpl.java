package com.ecommerce.ebdify.serviceimpl;

import com.ecommerce.ebdify.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get the current or original file names
        String originalFilename = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;

        // Check if path exists, if not then create
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Upload to server (codebase)
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Returning the file name
        return fileName;
    }
}
