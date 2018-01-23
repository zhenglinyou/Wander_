package com.zocki.wander;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity<LoginContract.LoginView, LoginPresenter>
        implements LoginContract.LoginView, View.OnClickListener {

    @Override
    protected void initActivityView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void findViewById() {
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void getData() {
        mPresenter.checkFormat("123","123");
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCheckFormatSuccess() {
        //loading.show();
    }

    @Override
    public void onCheckFormatFail(String info) {
    }

    @Override
    public void onLoginSuccess(Login login) {
    }

    @Override
    public void onLoginFail(String errorInfo) {
    }

}
