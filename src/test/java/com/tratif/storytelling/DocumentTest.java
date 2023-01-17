package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentAssert.assertThat;
import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.ACCEPTED;
import static com.tratif.storytelling.RevisionStatus.DRAFT;
import static com.tratif.storytelling.RevisionStatus.REJECTED;
import static com.tratif.storytelling.RevisionStatus.SUBMITED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DocumentTest {

	Person homer = new Person("Homer Simpson");
	Person bart = new Person("Bart Simpson");
	Person peter = new Person("Peter Griffin");

	@Test
	void newDocumentHasStatusDraft() {
		Document document = document().build();
		assertTrue(document.isDraft());
	}

	@Test
	public void authorCanAmendContentOfADraft() {
		Document document = document()
				.authoredBy(homer)
				.build();

		document.amend("new content", homer);

		assertThat(document).hasContent("new content");
	}

	@Test
	public void authorCanAmendTheContentOfADraftWithoutCreatingNewRevision() {
		Document document = document()
				.authoredBy(homer)
				.build();

		document.amend("new content", homer);

		assertThat(document).hasRevisionNumber(1);
	}

	@Test
	public void documentCanBeAmendedOnlyByAuthor() {
		Document document = document()
				.authoredBy(homer)
				.build();

		assertThrows(IllegalArgumentException.class, () -> document.amend("new content", bart));
	}

	@Test
	public void newRevisionIsCreatedWhenAuthorEditsARejectedDocument() {
		Document document = document()
				.authoredBy(homer)
				.withStatus(REJECTED)
				.build();

		document.amend("new content", homer);

		assertThat(document).hasRevisionNumber(2);
	}

	@Test
	public void authorCanAmendContentOfARejectedDocument() {
		Document document = document()
				.authoredBy(homer)
				.withStatus(REJECTED)
				.build();

		document.amend("new content", homer);

		assertThat(document).hasContent("new content");
	}

	@Test
	public void editorMayRejectSubmitedDocument() {
		Document document = document()
				.withStatus(SUBMITED)
				.authoredBy(homer)
				.withEditor(bart)
				.build();

		document.reject(bart);

		assertThat(document).hasStatus(REJECTED);
	}

	@Test
	public void editorCantRejectDocumentThatHasNotBeenSubmited() {
		Document document = document()
				.withStatus(DRAFT)
				.authoredBy(homer)
				.withEditor(bart)
				.build();

		assertThrows(IllegalStateException.class, () -> document.reject(bart));
	}

	@Test
	public void onlyEditorsMayRejectDocument() {
		Document document = document()
				.withStatus(SUBMITED)
				.authoredBy(homer)
				.withEditor(bart)
				.build();

		assertThrows(IllegalArgumentException.class, () -> document.reject(peter));
	}

	@Test
	public void editorMayAcceptSubmitedDocument() {
		Document document = document()
				.withStatus(SUBMITED)
				.authoredBy(homer)
				.withEditor(bart)
				.build();

		document.accept(bart);

		assertThat(document).hasStatus(ACCEPTED);
	}

	@Test
	public void onlyEditorMayAcceptDocument() {
		Document document = document()
				.withStatus(SUBMITED)
				.authoredBy(homer)
				.withEditor(bart)
				.build();

		assertThrows(IllegalArgumentException.class, () -> document.accept(peter));
	}

	@Test
	public void editorCantAcceptDocumentThatHasNotBeenSubmited() {
		Document document = document()
				.withStatus(DRAFT)
				.authoredBy(homer)
				.withEditor(bart)
				.build();

		assertThrows(IllegalStateException.class, () -> document.accept(bart));
	}

}
