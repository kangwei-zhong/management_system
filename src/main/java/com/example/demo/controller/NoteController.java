package com.example.demo.controller;

import com.example.demo.model.Note;
import com.example.demo.model.User;
import com.example.demo.service.NoteService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String viewNotes(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        List<Note> notes = noteService.getNotesByUserId(user.getId());
        model.addAttribute("notes", notes);
        return "notes";
    }

    @GetMapping("/note/new")
    public String newNoteForm(Model model) {
        model.addAttribute("note", new Note());
        return "new_note";
    }

    @PostMapping("/note")
    public String saveNote(@ModelAttribute Note note) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        note.setUser(user);
        noteService.saveNote(note);
        return "redirect:/";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return "redirect:/";
    }
}