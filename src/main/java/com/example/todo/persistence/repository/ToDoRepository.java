package com.example.todo.persistence.repository;

import com.example.todo.persistence.entity.ToDoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoEntity, Long> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    List<ToDoEntity> findAllByOrderByOrderAsc();

    @Modifying
    @Transactional
    @Query("DELETE FROM ToDoEntity t WHERE t.completed = true")
    void deleteAllCompleted();
}
