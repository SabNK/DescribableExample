package ru.polescanner.describableexample.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import ru.polescanner.describableexample.R;
import ru.polescanner.describableexample.adapters.DescriptionAdapter;
import ru.polescanner.describableexample.domain.base.BaseDescription;
import ru.polescanner.describableexample.domain.base.DescriptionIO;
import ru.polescanner.describableexample.domain.base.DescriptionUtility;
import ru.polescanner.describableexample.domain.base.ImagePortrait;
import ru.polescanner.describableexample.domain.base.ImageLandscape;
import ru.polescanner.describableexample.ui.viewmodels.MainViewModel;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private MainViewModel mViewModel;



    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: Use the ViewModel
        initDescribableEntity(view);
    }

    private void initDescribableEntity(@NonNull View view) {
        Context context = view.getContext();
        Log.d(TAG, "initBitmaps: preparing bitmaps.");
        String[] urls = new String[]{
                "https://i.redd.it/tpsnoz5bzo501.jpg",
                "https://i.redd.it/qn7f9oqu7o501.jpg",
                "https://i.redd.it/j6myfqglup501.jpg"
        };
        String[] authors = new String[]{
                "SabNK",
                "RykVS",
                "SabGN"
        };
        String[] dates = new String[]{
                "11.06.99",
                "05.07.02",
                "03.08.21"
        };
        for (int i = 0; i < urls.length; i++) {
            final int k = i;
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
                                        .description(filename)
                                        .thumbnail(thumbnail, view.getContext())
                                        .author(authors[k])
                                        .date(dates[k])
                                        .build();
                            }
                            else {
                                desc = ImageLandscape
                                        .description(filename)
                                        .thumbnail(thumbnail, view.getContext())
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
    }

    private void initDescriptionRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvDescription);
        DescriptionAdapter adapter = new DescriptionAdapter(view.getContext(),
                                                            mViewModel.describableEntity.getDescriptions());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, true));
    }
}