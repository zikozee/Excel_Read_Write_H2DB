package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.globalHandling.MyNumberFormatException;
import com.hbng.miniapp.charger.storage.StorageException;
import com.hbng.miniapp.charger.storage.StorageFileNotFoundException;
import com.hbng.miniapp.charger.storage.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    private String uploadedFilePath;

    @Autowired
    public StorageServiceImpl(StorageProperties properties) {
        log.info("I am in FileSystemStorageService class");
        this.rootLocation = Paths.get(properties.getLocation());

    }

    @Override
    public String getDestinationPath(){
        String root = System.getProperty("user.home");
        String desktop = root + "\\Desktop";
        Path toRoot = Paths.get(root);
        Path toDesktop = Paths.get(desktop);

        if(Files.exists(rootLocation)){
            return rootLocation.toAbsolutePath().toString() + "\\ProcessedExcel.xlsx";
        }else if(Files.exists(toDesktop)){
            return desktop + "ProcessedExcel.xlsx";
        }else if(Files.exists(toRoot)){
            return root + "ProcessedExcel.xlsx";
        }else{
            throw new MyNumberFormatException("Path to write to does not exist");
        }
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        log.info(rootLocation.resolve(filename).toAbsolutePath().toString());
        log.info(filename);
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void setFileName(MultipartFile multipartFile) {
        uploadedFilePath = multipartFile.getOriginalFilename();
    }

    @Override
    public String getFilePath() {
        return rootLocation.resolve(uploadedFilePath).toAbsolutePath().toString();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
