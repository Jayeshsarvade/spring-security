package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * This class implements the FileService interface and provides methods for file operations.
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * This method is responsible for uploading an image file to the specified path.
     * It generates a random UUID for the file name to avoid overwriting existing files.
     *
     * @param path The path where the image file will be stored.
     * @param file The MultipartFile object containing the image data.
     * @return The original filename of the uploaded image.
     * @throws IOException If an error occurs during file operations.
     */

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //file name  user multipart for upload file on server
        String name = file.getOriginalFilename();//DYWFYSFDY.png
        //random name generate
        String randomId = UUID.randomUUID().toString();   
        String fileName1 = randomId.concat(name.substring(name.lastIndexOf(".")));//23456789.jpg
        //full path
        String filePath = path + File.separator + fileName1;  //     image/23456789.jpg
        //create folder if not created
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        //file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));// when we upload a file on server 
        return name;
    }
}
