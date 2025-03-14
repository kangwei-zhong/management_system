package com.example.demo.service;

import com.example.demo.model.File;
import com.example.demo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final Path rootLocation = Paths.get("upload-dir");

    @Autowired
    private FileRepository fileRepository;

    public List<File> getFilesByUserId(Long userId) {
        return fileRepository.findByUserId(userId);
    }

    public File saveFile(MultipartFile file, Long userId, Long noteId) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setFilePath(this.rootLocation.resolve(filename).toString());
        newFile.setUser(userService.findById(userId)); // 设置 userId
        newFile.setNote(noteService.findById(noteId)); // 设置 noteId
        return fileRepository.save(newFile);
    }

    public Path loadFile(String filename) {
        return rootLocation.resolve(filename);
    }
}