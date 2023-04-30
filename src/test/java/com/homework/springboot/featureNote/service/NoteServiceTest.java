package com.homework.springboot.featureNote.service;

import com.homework.springboot.featureNote.entity.Note;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteServiceTest {

    private static NoteService noteService;
    private static List<Note> notes;


    @BeforeAll
    public static void init(){
        noteService = new NoteService();
    }

    @Test
    @Order(1)
    void add() {
        Note note = new Note().builder().title("Todo").content("Buy milk").build();
        Note addedNote = noteService.add(note);
        System.out.println("addedNote.getId() = " + addedNote.getId());
        assertEquals(note.getTitle(), addedNote.getTitle());
    }

    @Test
    @Order(2)
    void listAll() {
        Note note = new Note().builder().title("Todo2").content("Buy sugar").build();
        Note addedNote = noteService.add(note);
        notes = noteService.listAll();
        assertEquals(2, notes.size());
    }

    @Test
    @Order(3)
    void deleteById() {
        Note note = new Note().builder().title("Todo3").content("Buy mango").build();
        Note addedNote = noteService.add(note);

        Long id = addedNote.getId();
        Note deleteById = noteService.deleteById(id);
        assertEquals(addedNote, deleteById);
    }

    @Test
    @Order(4)
    void getById() {
        Note note = new Note().builder().title("Todo3").content("Buy mango").build();
        Note addedNote = noteService.add(note);
        Long id = addedNote.getId();

        Note byId = noteService.getById(id);
        System.out.println("byId = " + byId);
        System.out.println("notes = " + noteService.listAll());
        assertEquals(addedNote, byId);

    }

    @Test
    @Order(5)
    void update() {
        Note note = notes.stream().findFirst().get();
        Long id = note.getId();
        String newContent = "Sell house";

        note.setContent(newContent);
        noteService.update(note);
        Note noteAfterUpdate = noteService.getById(id);

        assertEquals(newContent,noteAfterUpdate.getContent());
    }
}