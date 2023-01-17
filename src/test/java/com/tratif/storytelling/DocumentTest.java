package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentAssert.assertThat;
import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.ACCEPTED;
import static com.tratif.storytelling.RevisionStatus.REJECTED;
import static com.tratif.storytelling.RevisionStatus.SUBMITED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DocumentTest {

	Person author = new Person("Homer Simpson");
	Person editor = new Person("Bart Simpson");
	Person sbElse = new Person("Peter Griffin");

	Document submitedDocument = document()
			.withStatus(SUBMITED)
			.authoredBy(author)
			.withEditor(editor)
			.build();

	Document rejectedDocument = document()
			.authoredBy(author)
			.withStatus(REJECTED)
			.build();

	Document draftDocument = document()
			.authoredBy(author)
			.withEditor(editor)
			.build();

	@Test
	void newDocumentHasStatusDraft() {
		Document document = document().build();
		assertTrue(document.isDraft());
	}

	@Test
	public void authorCanAmendContentOfADraft() {
		draftDocument.amend("new content", author);

		assertThat(draftDocument).hasContent("new content");
	}

	@Test
	public void authorCanAmendTheContentOfADraftWithoutCreatingNewRevision() {
		draftDocument.amend("new content", author);

		assertThat(draftDocument).hasRevisionNumber(1);
	}

	@Test
	public void documentCanBeAmendedOnlyByAuthor() {
		assertThrows(IllegalArgumentException.class, () -> draftDocument.amend("new content", editor));
	}

	@Test
	public void newRevisionIsCreatedWhenAuthorEditsARejectedDocument() {
		rejectedDocument.amend("new content", author);

		assertThat(rejectedDocument).hasRevisionNumber(2);
	}

	@Test
	public void authorCanAmendContentOfARejectedDocument() {
		rejectedDocument.amend("new content", author);

		assertThat(rejectedDocument).hasContent("new content");
	}

	@Test
	public void editorMayRejectSubmitedDocument() {
		submitedDocument.reject(editor);

		assertThat(submitedDocument).hasStatus(REJECTED);
	}

	@Test
	public void editorCantRejectDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocument.reject(editor));
	}

	@Test
	public void onlyEditorsMayRejectDocument() {
		assertThrows(IllegalArgumentException.class, () -> submitedDocument.reject(sbElse));
	}

	@Test
	public void editorMayAcceptSubmitedDocument() {
		submitedDocument.accept(editor);

		assertThat(submitedDocument).hasStatus(ACCEPTED);
	}

	@Test
	public void onlyEditorMayAcceptDocument() {
		assertThrows(IllegalArgumentException.class, () -> submitedDocument.accept(sbElse));
	}

	@Test
	public void editorCantAcceptDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocument.accept(editor));
	}

}
