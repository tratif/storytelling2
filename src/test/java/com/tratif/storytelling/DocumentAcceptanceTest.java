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

	Document draftDocument = document()
			.withEditors(editor, editor2)
			.build();

	@Test
	void documentIsAcceptedWhenAllEditorsAcceptIt() {
		Document document = document()
				.withStatus(SUBMITED)
				.withEditors(editor, editor2)
				.build();
		
		document.accept(editor);
		document.accept(editor2);

		assertThat(document).hasStatus(ACCEPTED);
	}

	@Test
	void documentIsNotAcceptedUntilAllEditorsAcceptIt() {
		Document document = document()
				.withStatus(SUBMITED)
				.withEditors(editor, editor2)
				.build();
		
		document.accept(editor);

		assertThat(document).hasStatus(SUBMITED);
	}

	@Test
	void editorCantAcceptDocumentMoreThanOnce() {
		Document document = document()
				.withStatus(SUBMITED)
				.withEditors(editor, editor2)
				.build();

		assertThrows(IllegalStateException.class, () -> document.accept(editor));
	}

	@Test
	void onlyEditorMayAcceptDocument() {
		Document document = document()
				.withStatus(SUBMITED)
				.withEditors(editor, editor2)
				.build();
		
		assertThrows(IllegalArgumentException.class, () -> document.accept(sbElse));
	}

	@Test
	void editorCantAcceptDocumentThatHasNotBeenSubmited() {
		assertThrows(IllegalStateException.class, () -> draftDocument.accept(editor));
	}
}
