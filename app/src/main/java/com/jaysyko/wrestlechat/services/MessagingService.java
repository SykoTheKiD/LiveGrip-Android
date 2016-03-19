//package com.jaysyko.wrestlechat.services;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//import com.jaysyko.wrestlechat.fragments.MessagingFragment;
//import com.jaysyko.wrestlechat.models.Message;
//
//import java.util.List;
//
///**
// * Created by jarushaan on 2016-03-09
// */
////public class MessagingService extends Service {
////    private static final String TAG = MessagingService.class.getSimpleName();
////    private final IBinder mBinder = new MessageBinder(this);
////    private Handler handler = new Handler();
////    private List<Message> messageList = new ArrayList<>();
////    private final Runnable fetchMessageRunnable = new Runnable() {
////        @Override
////        public void run() {
////            fetchOldMessages();
////        }
////    };
//
////    @Nullable
////    @Override
////    public IBinder onBind(Intent intent) {
////        return mBinder;
////    }
////
////    @Override
////    public void onCreate() {
////        super.onCreate();
//////        handler.post(fetchMessageRunnable);
////    }
////
////    @Override
////    public boolean onUnbind(Intent intent) {
////        return false;
////    }
//
//
//
//    public void update(List<Message> messages) {
//        MessagingFragment.updateMessages(messages);
//    }
//}
