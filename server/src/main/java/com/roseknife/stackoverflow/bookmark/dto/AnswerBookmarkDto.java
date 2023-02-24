package com.roseknife.stackoverflow.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

public class AnswerBookmarkDto {
	@Getter
	@Setter
	public static class Patch {
		private Long answerBookmarkId;

		@Positive
		private Long memberId;
	}

	@Getter
	@Setter
	public static class Response {
		private Long answerBookmarkId;

		private boolean answerBookmarkFlag;

		private Long memberId;
	}
}
