package com.homework.springboot.featureNote.admin;

import com.homework.springboot.featureNote.entity.Note;
import com.homework.springboot.featureNote.service.NoteService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HttpServletBean;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
@RequestMapping("/note")
@Controller
public class NoteController {
    private final NoteService noteService;
    private final TemplateEngine engine;
    private static List<Note> notes = new CopyOnWriteArrayList<>();



    @GetMapping("/search")
    public ModelAndView search(@RequestParam(required = false) String query,
                                       HttpServletResponse response){

        ModelAndView result = new ModelAndView("test");
        result.addObject("listNote", noteService.searchQuery(query));
        return result;
    }

    @GetMapping("/list")
    public ModelAndView getCurrentList(@RequestParam(required = false) String timezone,
                                       HttpServletResponse response){

        ModelAndView result = new ModelAndView("test");
        result.addObject("listNote", noteService.listAll());
        return result;
    }

//    @GetMapping("/create")
//    public String editNoteForm() {
//        init();
//        return "redirect:/note/list"; // the name of the HTML template for the edit note form
//    }

    @GetMapping("/add")
    public String addNoteForm(@RequestParam(value = "title", required = false) String title, String content, Model model) {
        Note note = new Note().builder().title(title).content(content).build();
        model.addAttribute("note", note);

        return "add-note"; // the name of the HTML template for the edit note form
    }

    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note) {
        // save the updated Note object to your database or service layer
        noteService.add(note);
        return "redirect:/note/list"; // the URL of the page that displays the list of notes
    }
    @GetMapping("/edit/{id}")
    public String editNoteForm(@PathVariable("id") Long id, Model model) {
        System.out.println("id = " + id);
        Note note = noteService.getById(id);
        System.out.println("note = " + note);
        System.out.println("noteService.listAll() = " + noteService.listAll());
        model.addAttribute("note", note);
        return "edit"; // the name of the HTML template for the edit note form
    }

    @PostMapping("/edit")
    public String editNoteSubmit(@ModelAttribute Note note) {
        // save the updated Note object to your database or service layer
        noteService.update(note);
        return "redirect:/note/list"; // the URL of the page that displays the list of notes
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") Long id) {
        // save the updated Note object to your database or service layer
        noteService.deleteById(id);
        return "redirect:/note/list"; // the URL of the page that displays the list of notes
    }


}
