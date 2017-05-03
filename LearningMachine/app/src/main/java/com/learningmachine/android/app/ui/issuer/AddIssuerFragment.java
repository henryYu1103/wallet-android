package com.learningmachine.android.app.ui.issuer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.learningmachine.android.app.R;
import com.learningmachine.android.app.data.IssuerManager;
import com.learningmachine.android.app.data.bitcoin.BitcoinManager;
import com.learningmachine.android.app.data.inject.Injector;
import com.learningmachine.android.app.databinding.FragmentAddIssuerBinding;
import com.learningmachine.android.app.ui.LMFragment;

import javax.inject.Inject;

public class AddIssuerFragment extends LMFragment {

    private FragmentAddIssuerBinding mBinding;

    @Inject protected BitcoinManager mBitcoinManager;
    @Inject protected IssuerManager mIssuerManager;


    public static AddIssuerFragment newInstance() {
        return new AddIssuerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_issuer, container, false);

        mBinding.addIssuerIdentityEditText.setOnEditorActionListener(mActionListener);

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.obtain(getContext())
                .inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_add_issuer, menu);
    }

    private void startIssuerIntroduction() {
        String introUrl = mBinding.addIssuerUrlEditText.getText()
                .toString();
        String nonce = mBinding.addIssuerIdentityEditText.getText()
                .toString();

        mBitcoinManager.getBitcoinAddress()
                .doOnSubscribe(() -> displayProgressDialog(R.string.fragment_add_issuer_adding_issuer_progress_dialog_message))
                .compose(bindToMainThread())
                .flatMap(bitcoinAddress -> mIssuerManager.addIssuer(introUrl, bitcoinAddress, nonce))
                .subscribe(aVoid -> {
                    hideProgressDialog();
                    getActivity().finish();
                }, throwable -> {
                    hideProgressDialog();
                    displayErrors(throwable, R.string.error_title_message);
                });
    }

    private TextView.OnEditorActionListener mActionListener = (v, actionId, event) -> {
        if (actionId == getResources().getInteger(R.integer.action_done) || actionId == EditorInfo.IME_ACTION_DONE) {
            startIssuerIntroduction();
            return false;
        }
        return false;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_add_issuer_verify:
                startIssuerIntroduction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
