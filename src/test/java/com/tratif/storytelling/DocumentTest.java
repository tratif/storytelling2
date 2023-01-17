package com.tratif.storytelling;

import static com.tratif.storytelling.DocumentAssert.assertThat;
import static com.tratif.storytelling.DocumentBuilder.document;
import static com.tratif.storytelling.RevisionStatus.REJECTED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DocumentTest {

    Person homer = new Person("Homer Simpson");
    Person bart = new Person("Bart Simpson");

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
}
