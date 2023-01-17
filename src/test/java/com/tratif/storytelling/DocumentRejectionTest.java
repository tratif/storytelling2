package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentAssert.assertThat;
import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.REJECTED;
import static com.tratif.storytelling.RevisionStatus.SUBMITED;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DocumentRejectionTest {

	Person author = new Person("Homer Simpson");
	Person editor = new Person("Bart Simpson");
	Person editor2 = new Person("Marge Simpson");
	Person sbElse = new Person("Peter Griffin");

	Document submitedDocument = document()
			.withStatus(SUBMITED)
			.authoredBy(author)
			.withEditors(editor, editor2)
			.build();

	Document draftDocument = document()
			.authoredBy(author)
			.withEditors(editor, editor2)
			.build();

	@Test
	void editorMayRejectSubmitedDocument() {
		submitedDocument.reject(editor);

		assertThat(submitedDocument).hasStatus(REJECTED);
	}

	@Test
	void editorCantRejectDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocument.reject(editor));
	}

	@Test
	void onlyEditorsMayRejectDocument() {
		assertThrows(IllegalArgumentException.class, () -> submitedDocument.reject(sbElse));
	}
}
