package ru.polescanner.describableexample.domain.base;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//ToDo Check if Image has to be serializable
public class Image extends Description {

    private Image(Builder builder){
        super(builder.thumbnail, builder.metadata, builder.filepath, builder.hash, builder.isStored);
    }

    public static Builder imageNew(@NonNull String filepath) {
        return new Builder(filepath, Builder.hash(filepath));
    }

    public static Builder image(@NonNull String filepath, @NonNull String hash) {
        return new Builder(filepath, hash);
    }


    public static final class Builder extends GenericBuilder<Builder> {

        private Builder(String filepath, String hash) {
            super(filepath, hash);
        }

        @Override
        protected Bitmap createThumbnail() {
            return null;
        }

        @Override
        public Description build(){
            setMetadata();
            isStored();
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