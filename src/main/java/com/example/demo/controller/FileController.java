package com.example.demo.controller;

import com.example.demo.model.File;
import com.example.demo.model.User;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @GetMapping("/files")
    public String viewFiles(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        List<File> files = fileService.getFilesByUserId(user.getId());
        model.addAttribute("files", files);
        return "files";
    }

    @GetMapping("/files/upload")
    public String uploadFileForm(Model model) {
        return "upload_file";
    }

    @PostMapping("/files/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("noteId") Long noteId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        try {
            fileService.saveFile(file, user.getId(), noteId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/files";
    }

    @GetMapping("/files/download/{filename}")
    @ResponseBody
    public Resource downloadFile(@PathVariable String filename) {
        Path file = fileService.loadFile(filename);
        Resource resource = null;
        try {
            resource = new UrlResource(file.toUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }
}