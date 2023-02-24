package com.roseknife.stackoverflow.question.mapper;

import com.roseknife.stackoverflow.answer.dto.AnswerDto;
import com.roseknife.stackoverflow.answer.entity.Answer;
import com.roseknife.stackoverflow.bookmark.dto.QuestionBookmarkDto;
import com.roseknife.stackoverflow.bookmark.entity.AnswerBookmark;
import com.roseknife.stackoverflow.bookmark.entity.QuestionBookmark;
import com.roseknife.stackoverflow.comment.dto.AnswerCommentDto;
import com.roseknife.stackoverflow.comment.entity.AnswerComment;
import com.roseknife.stackoverflow.comment.entity.QuestionComment;
import com.roseknife.stackoverflow.dto.PageInfo;
import com.roseknife.stackoverflow.member.entity.Member;
import com.roseknife.stackoverflow.question.dto.QuestionDto;
import com.roseknife.stackoverflow.question.entity.Question;
import com.roseknife.stackoverflow.tag.entity.QuestionTag;
import com.roseknife.stackoverflow.tag.entity.Tag;
import com.roseknife.stackoverflow.tag.repository.TagRepository;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    //기본 질문 Mapper

    @Mapping(source = "member.memberId",target = "memberId")
    QuestionBookmarkDto.Response questionBookmarkToQuestionBookmarkResponseDto(QuestionBookmark questionBookmark);
//    @Mapping(source = "memberId",target = "member.memberId")
    default Question questionPostToQuestion(QuestionDto.Post requestBody,List<Tag> tags) {
        if ( requestBody == null ) {
            return null;
        }

        Question question = new Question();
        Member member = new Member();
        member.setMemberId(requestBody.getMemberId());

        question.setMember(member);
        question.setTitle( requestBody.getTitle() );
        question.setContent( requestBody.getContent() );
        List<QuestionTag> questionTags = tags.stream()
                .map(requestTag -> {
                    QuestionTag questionTag = new QuestionTag();
                    questionTag.addTag(requestTag);// qT -> tag name //id content x
                    questionTag.addQuestion(question);
                    return questionTag;
                    }
                ).collect(Collectors.toList());

        question.setQuestionTags(questionTags);

        return question;
    }


    Question questionPatchToQuestion(QuestionDto.Patch requestBody);

    //질문 전체 조회 Mapper
    @Named("Q2R3")
    @Mapping(source = "member.nickname",target = "questionMember.nickname")
    @Mapping(source = "member.profile",target = "questionMember.profile")
    @Mapping(source = "member.memberId",target = "questionMember.memberId")
    QuestionDto.ResponseAll questionToQuestionResponseAll(Question requestBody);
    @IterableMapping(qualifiedByName = "Q2R3")
    List<QuestionDto.ResponseAll> questionsToQuestionResponses(List<Question> questions);

    //답변 댓글 -> 답변 댓글 리스폰DTO로 변경 (#1)
    @Named("Q2R1")
    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.profile", target = "profile")
    @Mapping(source = "member.memberId", target = "memberId")
    AnswerCommentDto.Response answerCommentToAnswerCommentResponseDto(AnswerComment answerComment);
    //답변 댓글 -> 답변 댓글 리스폰DTO 리스트로 변경 (#2)
    @IterableMapping(qualifiedByName = "Q2R1")
    List<AnswerCommentDto.Response> answerCommentsToAnswerCommentResponseDtos(List<AnswerComment> answerComments);

    //답변 -> 질문-답변 Dto로 변경 (답변-댓글 가공때문에 Default) - 추후 리팩토링 예정 (#3)
    default QuestionDto.QuestionAnswer answerToQuestionAnswer(Answer requestBody){
        if ( requestBody == null ) {
            return null;
        }

        QuestionDto.QuestionMember questionMember = new QuestionDto.QuestionMember(requestBody.getMember().getMemberId(),requestBody.getMember().getNickname(),requestBody.getMember().getProfile());
        LocalDateTime createdAt = requestBody.getCreatedAt();
        LocalDateTime modifiedAt = requestBody.getModifiedAt();
        String content = requestBody.getContent();
        AnswerBookmark answerBookmark = requestBody.getAnswerBookmark();

        List<AnswerCommentDto.Response> answerCommentResponse = answerCommentsToAnswerCommentResponseDtos(requestBody.getAnswerComments());
        QuestionDto.QuestionAnswer questionAnswer = new QuestionDto.QuestionAnswer( createdAt, modifiedAt, content, questionMember,answerCommentResponse,answerBookmark);

        return questionAnswer;
    }
    //답변 -> 질문-답변 Dto 리스트로 변경 (#4)
    List<QuestionDto.QuestionAnswer> answersToQuestionAnswers(List<Answer> requestBody);

    //댓글 -> 질문-댓글용 DTO로 변경 (#1)
    @Named("Q2R2")
    @Mapping(source = "member.nickname",target = "nickname")
    @Mapping(source = "member.memberId",target = "memberId")
    @Mapping(source = "member.profile",target = "profile")
    QuestionDto.QuestionCommentResponse commentToQuestionCommentResponse(QuestionComment requestBody);

    //댓글 -> 질문-댓글용 DTO 리스트로 변경 (#2)
    @IterableMapping(qualifiedByName = "Q2R2")
    List<QuestionDto.QuestionCommentResponse> commentsToQuestionCommentResponses(List<QuestionComment> requestBody);

    //질문 -> 질문 단건 리스폰스로 변경 (페이지네이션 적용, 여러 리스트로 인해 defualt 사용 - 추후 리팩토링 예정)
    default QuestionDto.Response questionsToQuestionAnswer(Question question, Page<Answer> pageAnswers) {
        if ( question == null ) {
            return null;
        }

        QuestionDto.QuestionMember questionMember = new QuestionDto.QuestionMember(question.getMember().getMemberId(),question.getMember().getNickname(),question.getMember().getProfile());
        List<QuestionDto.QuestionCommentResponse> questionComments = null;
        List<QuestionDto.QuestionAnswer> questionAnswers = null;
//        List<QuestionTag> questionTags = question.getQuestionTags();
        List<String> questionTags = new ArrayList<>();
        for (QuestionTag questionTag : question.getQuestionTags()) {
            questionTags.add(questionTag.getTag().getName());
        }
        Long questionId = question.getQuestionId();
        String title = question.getTitle();
        String content = question.getContent();
        LocalDateTime createdAt = question.getCreatedAt();
        LocalDateTime modifiedAt = question.getModifiedAt();
        Integer viewCount = question.getViewCount();
        Integer answerCount = question.getAnswerCount();
        Integer voteCount = question.getVoteCount();
//        QuestionBookmark questionBookmark = question.getQuestionBookmark();
        QuestionBookmarkDto.Response questionBookmark = questionBookmarkToQuestionBookmarkResponseDto(question.getQuestionBookmark());
        List<Answer> answers = pageAnswers.getContent();
        List<QuestionComment> comments = question.getQuestionComments();

        PageInfo pageInfo = new PageInfo(pageAnswers.getNumber() + 1,
                pageAnswers.getSize(), pageAnswers.getTotalElements(), pageAnswers.getTotalPages());

        questionAnswers = answersToQuestionAnswers(answers);
        questionComments = commentsToQuestionCommentResponses(comments);

        QuestionDto.Response response = new QuestionDto.Response(questionId, title, content, createdAt, modifiedAt, viewCount,
                answerCount, questionMember, questionAnswers, pageInfo, questionComments,questionTags,questionBookmark);

        return response;
    }


}
