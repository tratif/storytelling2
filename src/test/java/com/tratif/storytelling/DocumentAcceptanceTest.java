package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentAssert.assertThat;
import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.ACCEPTED;
import static com.tratif.storytelling.RevisionStatus.SUBMITED;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class DocumentAcceptanceTest {
	
	Person editor = new Person("Bart Simpson");
	Person editor2 = new Person("Marge Simpson");
	Person sbElse = new Person("Peter Griffin");

	Document submitedDocument = document()
			.withStatus(SUBMITED)
			.withEditors(editor, editor2)
			.build();

	Document draftDocument = document()
			.withEditors(editor, editor2)
			.build();

	@Test
	void documentIsAcceptedWhenAllEditorsAcceptIt() {
		submitedDocument.accept(editor);
		submitedDocument.accept(editor2);

		assertThat(submitedDocument).hasStatus(ACCEPTED);
	}

	@Test
	void documentIsNotAcceptedUntilAllEditorsAcceptIt() {
		submitedDocument.accept(editor);

		assertThat(submitedDocument).hasStatus(SUBMITED);
	}

	@Test
	void editorCantAcceptDocumentMoreThanOnce() {
		submitedDocument.accept(editor);

		assertThrows(IllegalStateException.class, () -> submitedDocument.accept(editor));
	}

	@Test
	void onlyEditorMayAcceptDocument() {
		assertThrows(IllegalArgumentException.class, () -> submitedDocument.accept(sbElse));
	}

	@Test
	void editorCantAcceptDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocument.accept(editor));
	}
}
