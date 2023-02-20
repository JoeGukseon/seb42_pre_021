package com.roseknife.stackoverflow.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roseknife.stackoverflow.answer.entity.Answer;
import com.roseknife.stackoverflow.audit.Auditable;
import com.roseknife.stackoverflow.comment.entity.QuestionComment;
import com.roseknife.stackoverflow.bookmark.entity.QuestionBookmark;
import com.roseknife.stackoverflow.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(length = 100, nullable = false)
    private String title;

    private String content;

    private Integer viewCount=0;    //초기화 = 0
    private Integer AnswerCount=0;  //초기화 = 0

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @JsonIgnore //목록으로 가져오는 쪽 에서만 적용해도 가능
    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE)    //
    private List<Answer> answers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE)
    private List<QuestionComment> questionComments = new ArrayList<>();

//    @OneToMany(mappedBy = "question")
//    private List<QuestionImage> questionImages;
//
//    @OneToMany(mappedBy = "question")
//    private List<QuestionTag> questionTags;
//
//    @OneToMany(mappedBy = "question")
//    private List<QuestionComment> questionComments;

//    @OneToMany(mappedBy = "question")
//    private List<QuestionVote> questionVotes;

    // modified 55-63
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private QuestionBookmark questionBookmark;

    public void setQuestionBookmark(QuestionBookmark questionBookmark) {
        this.questionBookmark = questionBookmark;
        if (questionBookmark.getQuestion() != this) {
            questionBookmark.setQuestion(this);
        }
    }
}
