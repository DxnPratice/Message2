package weather.newer.com.message2;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 *
 *
 * 下载任务
 */
public class DownLoadService extends Service {
    public  static final String TAG = "DownLoadService";
    public static final String EXTRA_URL ="url" ;
    public  static final String ACTION_RESULT = "com.newer.ACTION_RESULT";
    public static final String  EXTRA_RESULT = "result";
    // HandlerThread 由于在服务中创建的，就是服务中的子线程
    HandlerThread   serviceThread;//子线程
    ServiceHandler   serviceHandler;//子线程的消息处理器
    public DownLoadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建一个子线程
        serviceThread=new HandlerThread("DownLoadthread");
        //启动线程，注意该线程和普通线程的区别，它有一个Looper对象，即消息循环
        serviceThread.start();
        //handler在哪里创建就在哪个线程中，如果不指定参数，默认在当前线程即主线程，而现在消息处理器则在服务线程中
        //它是这个消息循环所在线程中的消息处理器
        serviceHandler=new ServiceHandler(serviceThread.getLooper());
    }

    /**
     *
     * 该方法会被多次执行
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:发消息");
        Message msg=Message.obtain();
        msg.obj=intent;
        serviceHandler.sendMessage(msg);
        return super.onStartCommand(intent, flags, startId);


    }

    //
    class  ServiceHandler extends Handler {
        /**
         *
         * 创建服务线程的消息处理器
         * @param looper   消息循环
         */

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent= (Intent) msg.obj;
           String url= intent.getStringExtra(EXTRA_URL);
            Log.d(TAG," handleMessage     "+url);
            //开始下载
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //使用本地广播
            Intent intentResult=new Intent();
            intentResult.setAction(ACTION_RESULT);
            intentResult.putExtra(EXTRA_RESULT, url + "结果");
            LocalBroadcastManager.getInstance(DownLoadService.this).sendBroadcast(intentResult);
        }
    }





    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceThread.quit();
    }
}
