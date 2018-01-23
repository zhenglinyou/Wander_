package com.zocki.wander;

/**
 * Created by kaisheng3 on 2018/1/22.
 */

public class LoginModelImpl implements LoginContract.LoginModel {

    @Override
    public void login(String name, String password, final LoginContract.LoginCallBack callBack) {
        // LoginNetUtils.getInstance().login(name, password, null);
    }
}
