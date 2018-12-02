package com.ardi.kamus.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ardi.kamus.R;
import com.ardi.kamus.entity.Kamus;
import com.ardi.kamus.main.DetailKamusActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_word)
    TextView tv_word;
    @BindView(R.id.tv_desc)
    TextView tv_desc;

    SearchViewHolder(View view){
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(final Kamus kamus){
        tv_word.setText(kamus.getWord());
        tv_desc.setText(kamus.getDescription());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), DetailKamusActivity.class);
                intent.putExtra(DetailKamusActivity.ITEM_WORD, kamus.getWord());
                intent.putExtra(DetailKamusActivity.ITEM_DESC, kamus.getDescription());
                itemView.getContext().startActivity(intent);
            }
        });
    }
}
