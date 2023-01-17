package com.tratif.storytelling;

public class DocumentBuilder {

	private String content = "dummy content";
	private Person author = new Person("Author");
	private Person editor = new Person("Editor");
	private Person reviewer = new Person("Reviewer");
	private RevisionStatus status;

	private DocumentBuilder() {
	}

	public DocumentBuilder authoredBy(Person author) {
		this.author = author;
		return this;
	}

	public DocumentBuilder withStatus(RevisionStatus status) {
		this.status = status;
		return this;
	}

	public Document build() {
		Document doc = new Document(content, author, editor, reviewer);
		if (status == RevisionStatus.REJECTED) {
			doc.submit(author);
			doc.reject(editor);
		}
		return doc;
	}

	public static DocumentBuilder document() {
		return new DocumentBuilder();
	}
}
