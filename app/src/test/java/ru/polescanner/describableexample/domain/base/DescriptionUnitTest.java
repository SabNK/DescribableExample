package ru.polescanner.describableexample.domain.base;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class DescriptionUnitTest {

    @Test
    public void addition_isCorrect() {
        assertThat(4).isEqualTo(2+2);
    }

    @Test
    public void download_isStored_correct() {
        Description desc = new Description.FakeDescription();
        assertThat(desc.isStored(null)).isFalse();
        desc.download();
        assertThat(desc.isStored(null)).isTrue();
    }

/*
    public void thumbnailIsCorrect() {
        String url = "https://i.redd.it/tpsnoz5bzo501.jpg";
        String author = "SabNK";
        String date = "11.06.99";
        Glide.with(context)
                .asBitmap()
                .load(urls[i])
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        BaseDescription.Metadata metadata = new BaseDescription.Metadata(authors[k],
                                                                                         dates[k]);
                        String filename = DescriptionUtility.saveBitmapToFile(resource, view.getContext());
                        Bitmap thumbnail = DescriptionUtility.getThumbnail(filename, view.getContext());
                        DescriptionIO utility = new DescriptionUtility(view.getContext());
                        BaseDescription desc;
                        if (k/2 == 0) {
                            desc = ImagePortrait
                                    .image(filename, view.getContext())
                                    .thumbnail(thumbnail)
                                    .author(authors[k])
                                    .date(dates[k])
                                    .build();
                        }
                        else {
                            desc = ImageLandscape
                                    .image(filename, view.getContext())
                                    .thumbnail(thumbnail)
                                    .author(authors[k])
                                    .date(dates[k])
                                    .build();
                        }
                        mViewModel.describableEntity.addDescription(desc);
                        if (mViewModel.describableEntity.getDescriptions().size() == 3) {
                            initDescriptionRecyclerView(view);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
*/
}
