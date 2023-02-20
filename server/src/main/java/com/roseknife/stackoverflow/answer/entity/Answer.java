package com.roseknife.stackoverflow.answer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.roseknife.stackoverflow.audit.Auditable;
import com.roseknife.stackoverflow.member.entity.Member;
import com.roseknife.stackoverflow.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Answer extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long answerId;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;

	//JsonIgnore 제거(Many에선 필요 X)
	@ManyToOne
	@JoinColumn(name = "QUESTION_ID")
	private Question question;

}
