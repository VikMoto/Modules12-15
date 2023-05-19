package com.homework.springboot.featureNote.service;

import com.homework.springboot.featureNote.entity.Note;
import com.homework.springboot.featureNote.entity.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class NoteService {
    @Autowired
    private final NoteRepository noteRepository;

    public Note add(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> searchQuery(String query) {
        return noteRepository.searchByNativeSqlQuery("%" + query + "%");
    }

    public List<Note> listAll() {

        return  noteRepository.findAll();
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id.toString());
    }


    public Note getById(Long id) {
        return noteRepository.searchById(id);
    }

    public void update(Note note) {
        noteRepository.setNoteInfoById(note.getTitle(),note.getContent(),note.getId());
        System.out.println("note = " + note);
    }
}
