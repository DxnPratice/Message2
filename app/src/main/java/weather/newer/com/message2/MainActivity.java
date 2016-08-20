package weather.newer.com.message2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  TextView textview;
    ResultReceiver  receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview= (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        receiver=new ResultReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(DownLoadService.ACTION_RESULT);
        //注册广播
        super.onResume();
        //注册本地广播
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销本地广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void downloada(View view) {
        Intent intent=new Intent(this,DownLoadService.class);
        intent.putExtra(DownLoadService.EXTRA_URL,"a");
        startService(intent);
    }

    public void dowuloadb(View view) {
        Intent intent=new Intent(this,DownLoadService.class);
        intent.putExtra(DownLoadService.EXTRA_URL,"b");
        startService(intent);
    }
    class ResultReceiver extends BroadcastReceiver{


        @Override
        public void onReceive(Context context, Intent intent) {
            String result=intent.getStringExtra(DownLoadService.EXTRA_RESULT);
            textview.setText(result);
        }
    }

}
