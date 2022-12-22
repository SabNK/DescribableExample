package ru.polescanner.describableexample.domain.description;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.polescanner.describableexample.domain.base.Description;
import ru.polescanner.describableexample.domain.base.DescriptionFile;

public abstract class Image extends BaseDescription {
    protected Image(@NonNull String thumbnail64,
                    @NonNull Metadata metadata,
                    @NonNull DescriptionFile file,
                    @Nullable Description reference) {
        super(thumbnail64, metadata, file, reference);
    }

    @Override
    protected String intentType(){
        return "image/*";
    }
}
