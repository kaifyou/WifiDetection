package com.example.wifidetection;

import java.util.List;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String TAG = "MainActivity";
	private Button scanButton, startButton, stopButton, checkButton;
	private TextView wifiView;
	private WifiUtils mWifiUtils;
	private List<ScanResult> mList;
	private ScanResult mScanResult;
	private StringBuffer strBuf = new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		scanButton = (Button) findViewById(R.id.scan);
		startButton = (Button) findViewById(R.id.start);
		stopButton = (Button) findViewById(R.id.stop);
		checkButton = (Button) findViewById(R.id.check);
		wifiView = (TextView) findViewById(R.id.allNetworks);
		BtnClickListener mClickListener = new BtnClickListener();
		scanButton.setOnClickListener(mClickListener);
		startButton.setOnClickListener(mClickListener);
		stopButton.setOnClickListener(mClickListener);
		checkButton.setOnClickListener(mClickListener);

		mWifiUtils = new WifiUtils(getApplicationContext());
	}

	public class BtnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.scan:
				getAllNetworkList();
				break;
			case R.id.start:
				mWifiUtils.openWifi();
				Toast.makeText(MainActivity.this,
						"当前wifi状态为：" + mWifiUtils.checkState(), 1).show();
				break;
			case R.id.stop:
				mWifiUtils.closeWifi();
				Toast.makeText(MainActivity.this,
						"当前wifi状态为：" + mWifiUtils.checkState(), 1).show();
				break;
			case R.id.check:
				Toast.makeText(MainActivity.this,
						"当前wifi状态为：" + mWifiUtils.checkState(), 1).show();
				break;
			default:
				break;
			}
		}

	}

	public void getAllNetworkList() {
		if (strBuf != null) {
			strBuf = new StringBuffer();
		}

		if (!mWifiUtils.startScan()) {
			return;
		}
		mList = mWifiUtils.getResults();
		if (mList != null) {
			Log.d(TAG, "mList number : " + mList.size());
			for (int i = 0; i < mList.size(); i++) {
				mScanResult = mList.get(i);
				strBuf = strBuf.append(mScanResult.BSSID + " ")
						.append(mScanResult.SSID + " ")
						.append(mScanResult.capabilities + " ")
						.append(mScanResult.frequency + " ")
						.append(mScanResult.level + "\n\n");
			}
			Toast.makeText(MainActivity.this,
					"扫描到的wifi网络：" + strBuf.toString(), 1).show();
			wifiView.setText("扫描到的wifi网络： \n" + strBuf.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
