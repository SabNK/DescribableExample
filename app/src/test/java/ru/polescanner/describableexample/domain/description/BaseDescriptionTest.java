package ru.polescanner.describableexample.domain.description;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class BaseDescriptionTest {
    BaseDescription sut;

    @Nested
    @DisplayName("Metadata")
    class MetadataTest {
    }

    @Nested
    @DisplayName("Unable to build object without")
    class BuilderTest {
        @Test
        @DisplayName("Thumbnail")
        void buildWoThumbnail(){

        }
    }


}
