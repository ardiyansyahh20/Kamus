package com.ardi.kamus.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ardi.kamus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailKamusActivity extends AppCompatActivity {
    public static final String ITEM_WORD = "item_word";
    public static final String ITEM_DESC = "item_description";

    @BindView(R.id.tv_word_detail)
    TextView tv_word_detail;
    @BindView(R.id.tv_desc_detail)
    TextView tv_desc_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kamus);
        ButterKnife.bind(this);

        tv_word_detail.setText(getIntent().getStringExtra(ITEM_WORD));
        tv_desc_detail.setText(getIntent().getStringExtra(ITEM_DESC));
    }
}
