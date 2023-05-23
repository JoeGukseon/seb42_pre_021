package com.roseknife.stackoverflow.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roseknife.stackoverflow.answer.entity.Answer;
import com.roseknife.stackoverflow.audit.Auditable;
import com.roseknife.stackoverflow.comment.entity.QuestionComment;
import com.roseknife.stackoverflow.bookmark.entity.QuestionBookmark;
import com.roseknife.stackoverflow.member.entity.Member;
import com.roseknife.stackoverflow.tag.entity.QuestionTag;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//Setter 제거 @Builder, @AllArgsConstructor 추가
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
public class Question extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(length = 100, nullable = false)
    @Setter
    private String title;

    @Column(length = 1_000_000_000)
    @Setter
    private String html;

    @Column(length = 1_000_000_000)
    @Setter
    private String markdown;

    //카운트 초기화 엔티티에서 하는게 유리 - 23.5.16 리팩토링
    /*
        컬렉션은 필드에서 초기화 하자.
        컬렉션은 필드에서 바로 초기화 하는 것이 안전하다.
        null 문제에서 안전하다.
        빌더 초기화 로 @Builder.dafault
     */
    @Builder.Default
    private Integer viewCount=0;
    @Builder.Default
    private Integer answerCount=0;
    @Builder.Default
    private Integer voteCount=0;

    public void setVoteCount(Integer voteCount) {
        if (voteCount >= 0) {
            this.voteCount = voteCount;
        }
    }

    public void viewCountPlus() {
        viewCount++;
    }
    public void answerCountPlus() {
        answerCount++;
    }
    public void answerCountMinus() {
        if(answerCount > 0){ answerCount--;}
    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @JsonIgnore //목록으로 가져오는 쪽 에서만 적용해도 가능
    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE)    //
    private List<Answer> answers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE)
    private List<QuestionComment> questionComments = new ArrayList<>();

    //질문글과 같이 questionTag의 값이 같이 움직이기 때문에 양방향 맵핑 해야함!(글 생성 및 삭제)
    @Builder.Default
    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true)
    private List<QuestionTag> questionTags = new ArrayList<>();

    @OneToOne(mappedBy = "question",cascade = CascadeType.REMOVE)
    private QuestionBookmark questionBookmark;

    public void setQuestionBookmark(QuestionBookmark questionBookmark) {
        this.questionBookmark = questionBookmark;
        if (questionBookmark.getQuestion() != this) {
            questionBookmark.setQuestion(this);
        }
    }

}
