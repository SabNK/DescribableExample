package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

//ToDo Check if Image has to be serializable
public class Image extends Description {
    //TODO security issue
    private String imageHash;

    private Image(Image.Builder builder){
        super(builder.thumbnail, builder.metadata, builder.filename, builder.hash);
    }

    @Override
    public String toString64() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(imageHash, image.imageHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageHash);
    }

    public static Builder image(@NonNull String filename) {
        return new Builder(filename);
    }

    public static final class Builder {
        private Bitmap thumbnail;
        private Description.Metadata metadata;
        private String author;
        private LocalDate date;
        private final String filename;
        private final String hash;

        private Builder(String filename) {
            this.filename = filename;
            this.hash = DescriptionUtility.getHash(filename);
        }

        public Builder thumbnail(Bitmap thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder author(String author){
            this.author = author;
            return this;
        }

        public Builder date(String dateDDPointMMPointYY){
            this.date = LocalDate.parse(dateDDPointMMPointYY, DateTimeFormatter.ofPattern("dd.MM.yy"));
            return this;
        }

        public Image build() {
            if (this.author != null) {
                if (this.date != null)
                    this.metadata = new Metadata(this.author, this.date);
                else
                    this.metadata = new Metadata(this.author);
            } else
                this.metadata = new Metadata();
            return new Image(this);
        }





    }
}
/*
data class Photo(
    val medium: String,
    val full: String
) {

  companion object {
    const val EMPTY_PHOTO = ""
  }

  fun getSmallestAvailablePhoto(): String {  // 1
    return when {
      isValidPhoto(medium) -> medium
      isValidPhoto(full) -> full
      else -> EMPTY_PHOTO
    }
  }

  private fun isValidPhoto(photo: String): Boolean { // 2
    return photo.isNotEmpty()
  }

class PhotoTests {

  private val mediumPhoto = "mediumPhoto"
  private val fullPhoto = "fullPhoto"
  private val invalidPhoto = "" // what’s tested in Photo.isValidPhoto()

  @Test
  fun photo_getSmallestAvailablePhoto_hasMediumPhoto() {
      // Given
      val photo = Media.Photo(mediumPhoto, fullPhoto)
      val expectedValue = mediumPhoto

      // When
      val smallestPhoto = photo.getSmallestAvailablePhoto()

      // Then
      assertEquals(smallestPhoto, expectedValue)
  }
  @Test
    fun photo_getSmallestAvailablePhoto_noMediumPhoto() {
    // Given
    val photo = Media.Photo(invalidPhoto, fullPhoto)
    val expectedValue = fullPhoto

    // When
    val smallestPhoto = photo.getSmallestAvailablePhoto()

    // Then
    assertEquals(smallestPhoto, expectedValue)
}

@Test
fun photo_getSmallestAvailablePhoto_noPhotos() {
    // Given
    val photo = Media.Photo(invalidPhoto, invalidPhoto)
    val expectedValue = Media.Photo.EMPTY_PHOTO

    // When
    val smallestPhoto = photo.getSmallestAvailablePhoto()

    // Then
    assertEquals(smallestPhoto, expectedValue)
}
}
}*/