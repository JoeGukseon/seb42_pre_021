package com.roseknife.stackoverflow.question.repository;

import com.roseknife.stackoverflow.question.entity.Question;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

//    @Query(value = "select * from question where (title || content) like %:keyword%",nativeQuery = true)
//    List<Question> searchQuestionsByKeyword(@Param("keyword") String keyword);
//@EntityGraph(value = "Question.withAssociations", type = EntityGraph.EntityGraphType.FETCH)

    //EntityGraph로 fetch join 적용
    @EntityGraph(attributePaths = {"member","questionBookmark"})
    Page<Question> findByTitleContainsOrHtmlContains(String titleKeyword,String htmlKeyword,Pageable pageable);

    //fetch join JPQL로 적용
    @Query(value = "SELECT q FROM Question q " +
            "JOIN FETCH q.member m " +
//            "LEFT JOIN FETCH q.questionTags qt " +
//            "LEFT JOIN FETCH m.roles "+
            "LEFT JOIN FETCH q.questionBookmark ",
            countQuery = "SELECT COUNT(q) FROM Question q")
    Page<Question> findQuestionsWithAssociations(Pageable pageable);

//    @Query(value = "SELECT q FROM Question q " +
//            "JOIN FETCH q.member m " +
////            "LEFT JOIN FETCH q.questionTags qt " +
////            "LEFT JOIN FETCH qt.tag " +
//            "LEFT JOIN FETCH q.member.roles " ,
////            "LEFT JOIN FETCH q.questionBookmark ",
//            countQuery = "SELECT COUNT(q) FROM Question q")
//    Page<Question> findQuestionsWithAssociations(Pageable pageable);

//    @Query(value = "SELECT q FROM Question q " +
//        "JOIN FETCH q.member m " +
//        "LEFT JOIN FETCH m.roles " ,
//        countQuery = "SELECT COUNT(q) FROM Question q")
//    Page<Question> findQuestionsWithAssociations(Pageable pageable);


//    @EntityGraph(attributePaths = {"member","questionBookmark","questionTags"})
//    @Query("SELECT q FROM Question q")
//    Page<Question> findQuestionsWithAssociations(Pageable pageable);
}
