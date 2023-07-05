package com.roseknife.stackoverflow.tag.entity;

import com.roseknife.stackoverflow.question.entity.Question;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class QuestionTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionTagId;

    //진짜 맵핑을 하는 부분 외래키가 저장되는 부분
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;
     public void setQuestion(Question question) {
        this.question = question;
        if (!this.question.getQuestionTags().contains(this)) {
            this.question.getQuestionTags().add(this);
        }
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TAG_ID")
    private Tag tag;

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
