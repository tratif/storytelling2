package com.tratif.storytelling;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class DocumentAssert extends AbstractAssert<DocumentAssert, Document> {

	protected DocumentAssert(Document actual) {
		super(actual, DocumentAssert.class);
	}

	public DocumentAssert hasRevisionNumber(int num) {
		isNotNull();
		Assertions.assertThat(actual.getRevisions()).hasSize(num);
		return this;
	}

	public DocumentAssert hasContent(String content) {
		isNotNull();
		Assertions.assertThat(actual.getLastRevision().getContent()).isEqualTo(content);
		return this;
	}

	public DocumentAssert hasStatus(RevisionStatus status) {
		isNotNull();
		Assertions.assertThat(actual.getLastRevision().getStatus()).isEqualTo(status);
		return this;
	}

	public static DocumentAssert assertThat(Document doc) {
		return new DocumentAssert(doc);
	}
}
