package com.roseknife.stackoverflow.question.service;

import com.roseknife.stackoverflow.answer.entity.Answer;
import com.roseknife.stackoverflow.answer.repository.AnswerRepository;
import com.roseknife.stackoverflow.exception.BusinessLogicException;
import com.roseknife.stackoverflow.exception.ExceptionCode;
import com.roseknife.stackoverflow.question.entity.Question;
import com.roseknife.stackoverflow.question.entity.FindStatus;
import com.roseknife.stackoverflow.question.mapper.QuestionMapper;
import com.roseknife.stackoverflow.question.repository.QuestionRepository;
import com.roseknife.stackoverflow.tag.entity.QuestionTag;
import com.roseknife.stackoverflow.tag.entity.Tag;
import com.roseknife.stackoverflow.tag.repository.QuestionTagRepository;
import com.roseknife.stackoverflow.utils.CustomBeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private final QuestionTagRepository questionTagRepository;
    private final QuestionMapper questionMapper;

    public Question createQuestion(Question question) {
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion;
    }

    public Question updateQuestion(Question question, List<Tag> tags) {
        Question findQuestion = findVerifiedQuestion(question.getQuestionId(),FindStatus.NONE);
        //수정시 title, content, voteCount, tag 변경가능
        Optional.ofNullable(question.getTitle()).ifPresent(findQuestion::setTitle);
        Optional.ofNullable(question.getMarkdown()).ifPresent(findQuestion::setMarkdown);
        Optional.ofNullable(question.getHtml()).ifPresent(findQuestion::setHtml);
        Optional.ofNullable(question.getVoteCount()).ifPresent(findQuestion::setVoteCount);

        //QuestionTag가 업데이트 시 기존 Tag 삭제
        if (!tags.isEmpty()) {
            findQuestion.getQuestionTags().clear(); //QuestionTag 리스트 비우기 (고아객체로 만들기) - orphan 옵션 true
            questionMapper.tagsToQuestionTags(findQuestion, tags);  //태그 리스트 업데이트
        }

        return findQuestion;
    }
    public Question findVerifiedQuestion(Long questionId, FindStatus option) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        Question findQuestion =
                optionalQuestion.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
        //엔티티 안에서 증가/감소 리팩터링
        switch(option){
            case FIND:
                findQuestion.viewCountPlus();   //단건 조회시 조회수 증가
                break;
            case ANSWER:
                findQuestion.answerCountPlus();
                break;
            case ANSWER_DEL:
                findQuestion.answerCountMinus();
                break;
        }


        return findQuestion;
    }
    @Transactional(readOnly = true)
    public Question findQuestion(Long questionId) {
        return findVerifiedQuestion(questionId,FindStatus.FIND);
    }

    public Page<Question> findQuestions(int page, int size, String sortDir, String sortBy) {
        //리팩토링 여부?
        PageRequest request;

        request = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sortBy);

        return questionRepository.findAll(request);
    }

    public Page<Question> searchQuestions(int page, int size, String sortDir, String sortBy, String keyword) {
        PageRequest request;

        request = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sortBy);
        return questionRepository.findByTitleContainsOrHtmlContains(keyword,keyword,request);
    }

    public void deleteQuestion(Long questionId) {
        Question findQuestion = findVerifiedQuestion(questionId, FindStatus.NONE);
        questionRepository.delete(findQuestion);
    }

    public Page<Answer> findQuestionAnswers(Long questionId,int page, int size, String sortDir, String sortBy) {
        PageRequest request;

        request = PageRequest.of(page, size, Sort.Direction.valueOf(sortDir), sortBy);

        return answerRepository.findByQuestionQuestionId(questionId,request);
    }
}
