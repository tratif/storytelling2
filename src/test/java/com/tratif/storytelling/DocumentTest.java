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

	Person homer = new Person("Homer Simpson");
	Person bart = new Person("Bart Simpson");
	Person peter = new Person("Peter Griffin");

	Document submitedDocumentAuthoredByHomerWithEditorBart = document()
			.withStatus(SUBMITED)
			.authoredBy(homer)
			.withEditor(bart)
			.build();

	Document rejectedDocumentAuthoredByHomer = document()
			.authoredBy(homer)
			.withStatus(REJECTED)
			.build();

	Document draftDocumentAuthoredByHomerWithEditorBart = document()
			.authoredBy(homer)
			.withEditor(bart)
			.build();

	@Test
	void newDocumentHasStatusDraft() {
		Document document = document().build();
		assertTrue(document.isDraft());
	}

	@Test
	public void authorCanAmendContentOfADraft() {
		draftDocumentAuthoredByHomerWithEditorBart.amend("new content", homer);

		assertThat(draftDocumentAuthoredByHomerWithEditorBart).hasContent("new content");
	}

	@Test
	public void authorCanAmendTheContentOfADraftWithoutCreatingNewRevision() {
		draftDocumentAuthoredByHomerWithEditorBart.amend("new content", homer);

		assertThat(draftDocumentAuthoredByHomerWithEditorBart).hasRevisionNumber(1);
	}

	@Test
	public void documentCanBeAmendedOnlyByAuthor() {
		assertThrows(IllegalArgumentException.class, () -> draftDocumentAuthoredByHomerWithEditorBart.amend("new content", bart));
	}

	@Test
	public void newRevisionIsCreatedWhenAuthorEditsARejectedDocument() {
		rejectedDocumentAuthoredByHomer.amend("new content", homer);

		assertThat(rejectedDocumentAuthoredByHomer).hasRevisionNumber(2);
	}

	@Test
	public void authorCanAmendContentOfARejectedDocument() {
		rejectedDocumentAuthoredByHomer.amend("new content", homer);

		assertThat(rejectedDocumentAuthoredByHomer).hasContent("new content");
	}

	@Test
	public void editorMayRejectSubmitedDocument() {
		submitedDocumentAuthoredByHomerWithEditorBart.reject(bart);

		assertThat(submitedDocumentAuthoredByHomerWithEditorBart).hasStatus(REJECTED);
	}

	@Test
	public void editorCantRejectDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocumentAuthoredByHomerWithEditorBart.reject(bart));
	}

	@Test
	public void onlyEditorsMayRejectDocument() {
		assertThrows(IllegalArgumentException.class, () -> submitedDocumentAuthoredByHomerWithEditorBart.reject(peter));
	}

	@Test
	public void editorMayAcceptSubmitedDocument() {
		submitedDocumentAuthoredByHomerWithEditorBart.accept(bart);

		assertThat(submitedDocumentAuthoredByHomerWithEditorBart).hasStatus(ACCEPTED);
	}

	@Test
	public void onlyEditorMayAcceptDocument() {
		assertThrows(IllegalArgumentException.class, () -> submitedDocumentAuthoredByHomerWithEditorBart.accept(peter));
	}

	@Test
	public void editorCantAcceptDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocumentAuthoredByHomerWithEditorBart.accept(bart));
	}

}
