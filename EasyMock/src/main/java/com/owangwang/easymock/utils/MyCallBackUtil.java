package com.owangwang.easymock.utils;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * 用于验证指纹的工具类
 * Created by wangchao on 2017/12/27.
 */

public class MyCallBackUtil {
    public FingerprintManagerCompat manager;
    public Context mContext;
    FingerprintOnCall call;
    MyCallBackUtil(Context mContext, FingerprintOnCall call){
        this.call=call;
        this.mContext=mContext;
        this.manager=FingerprintManagerCompat.from(mContext);

    }
    public void startVerify(){
        if (manager!=null)
        manager.authenticate(null,0,null,new MyCallBack(call),null);
    }
    public void stopVerify(){


    }
    public interface FingerprintOnCall{
        void onSuccess();
        void onFailed();
        void onHelp(int helpMsgId, CharSequence helpString);
        void onError(int errMsgId, CharSequence errString);
    }
    public class  MyCallBack extends  FingerprintManagerCompat.AuthenticationCallback{
        private FingerprintOnCall call1;
        MyCallBack( FingerprintOnCall call){
            call1=call;
        }
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            call1.onHelp(helpMsgId,helpString);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            call1.onFailed();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            call1.onSuccess();
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            call1.onError(errMsgId,errString);
        }
    }
}
