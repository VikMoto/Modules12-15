package com.homework.springboot.featureNote.service;

import com.homework.springboot.featureNote.entity.Note;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NoteService {
    private static Map<Long, Note> notes = new HashMap<>();
    public Note add(Note note) {
        Long id;
//        do {
          id = (long) (Math.random()/Math.nextDown(1.0) * 3000400002304L);
//        }while (notes.get(id) == null);

        note.setId(id);
        notes.put(id, note);
        return notes.get(id);
    }

    public List<Note> listAll() {
        List<Note> values = new ArrayList<>(notes.values());
        return  values;
    }

    public Note deleteById(Long id) {
        return notes.remove(id);
    }


    public Note getById(Long id) {
        return notes.get(id);
    }

    public void update(Note note) {
        System.out.println("note = " + note);
        Optional<Note> note1 = Optional.ofNullable(notes.get(note.getId()));
        if (note1.isPresent()) {
            note1.get().setTitle(note.getTitle());
            note1.get().setContent(note.getContent());
        } else {
            throw new RuntimeException();
        }
    }
}
