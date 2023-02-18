package com.roseknife.stackoverflow.question.controller;

import com.roseknife.stackoverflow.answer.entity.Answer;
import com.roseknife.stackoverflow.dto.MultiResponseDto;
import com.roseknife.stackoverflow.dto.SingleResponseDto;
import com.roseknife.stackoverflow.question.dto.QuestionDto;
import com.roseknife.stackoverflow.question.entity.Question;
import com.roseknife.stackoverflow.question.mapper.QuestionMapper;
import com.roseknife.stackoverflow.question.service.RealQuestionService;
import com.roseknife.stackoverflow.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/questions")
@Validated
public class QuestionController {
    private final static String QUESTION_DEFAULT_URL = "/questions";
    private final RealQuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionController(RealQuestionService questionService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionDto.Post requestBody) {
        Question question = questionMapper.questionPostToQuestion(requestBody);

        Question createQuestion = questionService.createQuestion(question);
        URI location = UriCreator.createUri(QUESTION_DEFAULT_URL, createQuestion.getQuestionId());

        return ResponseEntity.created(location).build();
//        return new ResponseEntity<>(
//                new SingleResponseDto<>(questionMapper.questionToQuestionResponse(question))
//                        , HttpStatus.OK);
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive Long questionId,
                                        @Valid @RequestBody QuestionDto.Patch requestBody) {
        requestBody.setQuestionId(questionId);

        Question question = questionService.updateQuestion(questionMapper.questionPatchToQuestion(requestBody));

//        return new ResponseEntity(new SingleResponseDto<>(questionMapper.questionToQuestionResponse(question))
//                , HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{question-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive Long questionId) {

        Question question = questionService.findQuestion(questionId);


        return new ResponseEntity<>(
                new SingleResponseDto<>(questionMapper.questionToQuestionResponse(question))
                , HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestions(@Positive @RequestParam("page") int page,
                                       @Positive @RequestParam("size") int size,
                                       @RequestParam("sortDir") String sortDir,
                                       @RequestParam("sortBy") String sortBy) {
        Page<Question> pageQuestions = questionService.findQuestions(page-1, size, sortDir, sortBy);

        List<Question> questions = pageQuestions.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(questionMapper.questionsToQuestionResponses(questions),
                        pageQuestions),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity getSearchQuestions(@RequestParam("keyword") String keyword,
                                            @Positive @RequestParam("page") int page,
                                            @Positive @RequestParam("size") int size,
                                            @RequestParam("sortDir") String sortDir,
                                            @RequestParam("sortBy") String sortBy
    ) {
        Page<Question> pageQuestions = questionService.searchQuestions(page - 1, size, sortDir, sortBy, keyword);

        List<Question> questions = pageQuestions.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(questionMapper.questionsToQuestionResponses(questions),
                        pageQuestions),
                HttpStatus.OK);
    }
}
