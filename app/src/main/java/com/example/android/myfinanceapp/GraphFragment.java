package com.example.android.myfinanceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

public class GraphFragment extends Fragment {

    public  GraphFragment(){

    }

    public int mSaldo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.coming_soon, container, false);
        TextView comingSoon = (TextView) rootView.findViewById(R.id.coming_soon_text_view);
        comingSoon.setText("Coming Soon");
        return rootView;
/*        final TextView saldoTextView = (TextView) rootView.findViewById(R.id.saldo_value_text_view);
        final SharedPreferences mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        final int defaultValue = getResources().getInteger(R.integer.default_value);
        mSaldo = mSharedPreferences.getInt(getString(R.string.saved_saldo), defaultValue);
        Button button = (Button) rootView.findViewById(R.id.set_budget_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue(rootView, defaultValue);
                mEditor.putInt(getString(R.string.saved_saldo), mSaldo);
                mEditor.apply();
                saldoTextView.setText(String.valueOf(mSaldo));
            }
        });
        saldoTextView.setText(String.valueOf(mSaldo));
        return rootView;
    }

    private void getValue(View view, int defaultValue){
        mSaldo=defaultValue;
        EditText editText = (EditText) view.findViewById(R.id.saldo_edit_text);
        String saldoString = editText.getText().toString().trim();
        if(TextUtils.isEmpty(saldoString)){
            return;
        }
        mSaldo = Integer.parseInt(saldoString);
    }

    public int getSaldo(){
        return mSaldo;
    }

    public void setSaldo(int i){
        mSaldo-=i;
    }*/

}}
