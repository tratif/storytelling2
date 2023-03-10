package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.REJECTED;
import static com.tratif.storytelling.RevisionStatus.SUBMITED;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DocumentTest {

	Person author = new Person("Homer Simpson");
	Person editor = new Person("Bart Simpson");
	Person editor2 = new Person("Marge Simpson");
	Person sbElse = new Person("Peter Griffin");

	Document submitedDocument = document()
			.withStatus(SUBMITED)
			.authoredBy(author)
			.withEditors(editor, editor2)
			.build();

	Document rejectedDocument = document()
			.authoredBy(author)
			.withStatus(REJECTED)
			.build();

	Document draftDocument = document()
			.authoredBy(author)
			.withEditors(editor, editor2)
			.build();

	@Test
	void newDocumentHasStatusDraft() {
		Document document = document().build();
		assertTrue(document.isDraft());
	}

}
