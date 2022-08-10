package ru.polescanner.describableexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.polescanner.describableexample.R;
import ru.polescanner.describableexample.domain.base.Description;
import ru.polescanner.describableexample.domain.base.DescriptionUtility;

//ToDo discuss async operations with load multimedia from Server
public class DescriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "DescriptionAdapter";
    private Context context;
    private List<Description> descriptions;

    public DescriptionAdapter(Context context, List<Description> descriptions) {
        this.context = context;
        this.descriptions = descriptions;
        this.descriptions.add(getAddStub(context));
    }

    @Override
    public int getItemViewType(int position) {
        if (descriptions.get(position).getClass().getSimpleName().equals("Image"))
            return 0;
        else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,
                                                                          parent,
                                                                          false);
                return new ImageViewHolder(v);
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_description2,
                                                                          parent,
                                                                          false);
                return new ImageViewHolder2(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Description current = descriptions.get(position);
        DescriptionViewHolder viewHolder = (DescriptionViewHolder) holder;
        Glide.with(context)
                .asBitmap()
                .load(current.getThumbnail())
                .into(viewHolder.ivDescriptionThumbnail);
        viewHolder.tvDescriptionMetadata.setText(current.getMetadata());
        viewHolder.cvDescriptionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + current.getMetadata());
                Toast.makeText(context, current.getMetadata(), Toast.LENGTH_SHORT).show();
                //Intent to view or listen or read the multimedia
                if (position == descriptions.size() - 1) {
                    Log.d(TAG, "onClick: ADD NEW DESCRIPTION!");
                    //Dialog for new multimedia
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    private Description getAddStub(Context context){
        Bitmap bm = getBitmap(R.drawable.pen_plus);
        Description.Metadata metadata = new Description.Metadata("ADD NOW!");
        String filename = DescriptionUtility.saveBitmapToFile(bm, context);
        return new AddDescriptionStub(DescriptionUtility.getThumbnail(filename, context),
                                      metadata, filename, DescriptionUtility.getHash(filename));
    };

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    abstract class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "DescriptionViewHolder";
        ImageView ivDescriptionThumbnail;
        TextView tvDescriptionMetadata;
        CardView cvDescriptionItem;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ImageViewHolder extends DescriptionViewHolder {

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDescriptionThumbnail = itemView.findViewById(R.id.ivDescriptionThumbnail);
            tvDescriptionMetadata = itemView.findViewById(R.id.tvDescriptionMetadata);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionItem);
        }
    }

    public class ImageViewHolder2 extends DescriptionViewHolder {

        public ImageViewHolder2(@NonNull View itemView) {
            super(itemView);
            ivDescriptionThumbnail = itemView.findViewById(R.id.ivDescriptionThumbnail2);
            tvDescriptionMetadata = itemView.findViewById(R.id.tvDescriptionMetadata2);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionItem2);
        }
    }


    //ToDo make a res with an icon +add multimedia
    private class AddDescriptionStub extends Description {
        private AddDescriptionStub(@NonNull Bitmap thumbnail,
                                   @NonNull Metadata metadata,
                                   @NonNull String filename,
                                   @NonNull String hash) {
            super(thumbnail, metadata, filename, hash);
        }


        @Override
        public String toString64() {
            return null;
        }
    }
}
