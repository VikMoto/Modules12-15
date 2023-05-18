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
    private static Map<Long, Note> notes = new HashMap<>();


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
//        Optional<Note> note1 = Optional.ofNullable(notes.get(note.getId()));
//        if (note1.isPresent()) {
//            note1.get().setTitle(note.getTitle());
//            note1.get().setContent(note.getContent());
//        } else {
//            throw new RuntimeException();
//        }
    }
}
