package com.ecommerce.sortizecom.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    public String uploadImage(String path, MultipartFile file) throws IOException {

        // File name of current / original file
        String originalFilename = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // uuid = 1234 -> test.jpg -> unique filename -> 1234.jpg - substring get the last index of . to get the subset string for the extension
        String filename = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        // build the filepath -> images/1234.jpg
        String filePath = path + File.separator + filename;

        // Check if the path exists, otherwise create it
        // creates a file object to check if exists, if not, creates the folder - check if main folder exists image/
        File imageFolder = new File(path);
        if(!imageFolder.exists()){
            imageFolder.mkdir();
        }

        // upload to server - copy the inputstream into the path location (outputstream)
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return filename;
    }
}
