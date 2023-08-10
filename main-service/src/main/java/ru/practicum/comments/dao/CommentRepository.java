package ru.practicum.comments.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByAuthorId(Integer authorId, Pageable pageable);

    Comment findByAuthorIdAndEventId(Integer authorId, Integer eventId);

    @Query("SELECT comment " +
            "FROM Comment comment " +
            "WHERE (:users IS null or comment.author.id in :users)"
    )
    List<Comment> findByAuthorIdIn(List<Integer> users, Pageable pageable);

    @Query("SELECT comment " +
            "FROM Comment comment " +
            "WHERE (:events IS null or comment.event.id in :events)"
    )
    List<Comment> findByEventIdIn(List<Integer> events, Pageable pageable);
}
