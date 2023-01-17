package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentAssert.assertThat;
import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.REJECTED;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DocumentAmendingTest {

	Person author = new Person("Homer Simpson");
	Person editor = new Person("Bart Simpson");
	Person editor2 = new Person("Marge Simpson");
	Person sbElse = new Person("Peter Griffin");

	Document rejectedDocument = document()
			.authoredBy(author)
			.withStatus(REJECTED)
			.build();

	Document draftDocument = document()
			.authoredBy(author)
			.withEditors(editor, editor2)
			.build();

	@Test
	void authorCanAmendContentOfADraft() {
		draftDocument.amend("new content", author);

		assertThat(draftDocument).hasContent("new content");
	}

	@Test
	void authorCanAmendTheContentOfADraftWithoutCreatingNewRevision() {
		draftDocument.amend("new content", author);

		assertThat(draftDocument).hasRevisionNumber(1);
	}

	@Test
	void documentCanBeAmendedOnlyByAuthor() {
		assertThrows(IllegalArgumentException.class, () -> draftDocument.amend("new content", editor));
	}

	@Test
	void newRevisionIsCreatedWhenAuthorEditsARejectedDocument() {
		rejectedDocument.amend("new content", author);

		assertThat(rejectedDocument).hasRevisionNumber(2);
	}

	@Test
	void authorCanAmendContentOfARejectedDocument() {
		rejectedDocument.amend("new content", author);

		assertThat(rejectedDocument).hasContent("new content");
	}
}
