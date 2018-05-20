package thuannv.pageslider.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import thuannv.pageslider.InfinitePagerAdapter;
import thuannv.pageslider.PageSliderView;

public class DemoActivity extends AppCompatActivity {

    @BindView(R.id.image_slider)
    PageSliderView mImageSliderView;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;


    private Unbinder mUnbinder;

    private ImageSliderAdapter mImageSliderAdapter;

    private void setupImageSlider() {
        mImageSliderAdapter = new ImageSliderAdapter(ImagesProvider.provides());
        mImageSliderAdapter.setItemClickListener((image) -> {
            //TODO: your code goes here.
        });
        mImageSliderView.setAdapter(InfinitePagerAdapter.wrap(mImageSliderAdapter));
    }

    private void setupRecyclerView() {
        DemoRecyclerViewAdapter adapter = new DemoRecyclerViewAdapter(ItemProvider.provides());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mUnbinder = ButterKnife.bind(this);
        setupImageSlider();
        setupRecyclerView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release listener to prevent memory leak causes by Cyclic Reference
        if (mImageSliderAdapter != null) {
            mImageSliderAdapter.setItemClickListener(null);
        }

        mUnbinder.unbind();
    }


    /**
     * {@link DemoRecyclerViewAdapter}
     */
    static class DemoRecyclerViewAdapter extends RecyclerView.Adapter<DemoRecyclerViewAdapter.VH> {

        private List<ListItem> mItems;

        public DemoRecyclerViewAdapter(List<ListItem> items) {
            mItems = items;
        }

        private ListItem getItemAt(int adapterPosition) {
            return mItems.get(adapterPosition);
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new VH(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(this);
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        /**
         * {@link VH}
         */
        static class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.image)
            ImageView mImageView;

            @BindView(R.id.tvTitle)
            TextView mTitle;

            @BindView(R.id.tvSubtitle)
            TextView mSubtitle;

            VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void bind(DemoRecyclerViewAdapter demoRecyclerViewAdapter) {
                int adapterPosition = getAdapterPosition();
                ListItem item = demoRecyclerViewAdapter.getItemAt(adapterPosition);
                bindImage(item.image);
                mTitle.setText(item.title);
                mSubtitle.setText(item.subtitle);
            }

            void bindImage(ImageSource image) {
                final RequestManager glide = Glide.with(itemView.getContext());
                if (image.isPath()) {
                    glide.load(image.getPath()).into(mImageView);
                } else if (image.isFile()) {
                    glide.load(image.getFile()).into(mImageView);
                } else if (image.isUri()) {
                    glide.load(image.getUri()).into(mImageView);
                } else if (image.isLocalResourceId()) {
                    glide.load(image.getResourceId()).into(mImageView);
                }

            }
        }
    }
}
