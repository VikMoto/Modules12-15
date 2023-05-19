package com.homework.springboot.featureNote.entity;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,String> {

    @Query(nativeQuery = true, value =
            "SELECT id, title, content " +
                    "FROM note " +
                    "WHERE lower(title) LIKE lower(:query) " +
                    "OR lower(content) LIKE lower(:query)")
    List<Note> searchByNativeSqlQuery(@Param("query") String query);

    @Query(nativeQuery = true, value =
            "SELECT id, title, content " +
                    "FROM note " +
                    "WHERE id = :query ")
    Note searchById(Long query);

    @Modifying
    @Transactional
    @Query("update Note n set n.title = ?1, n.content = ?2 where n.id = ?3")
    void setNoteInfoById(String title, String content, Long id);

}
