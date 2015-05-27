package com.example.wifidetection;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.NoCopySpan.Concrete;
import android.util.Log;

public class WifiUtils {
	private String TAG = "WifiUtils";
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	// 扫描出的网络连接列表
	private List<ScanResult> mWifiList;
	private List<WifiConfiguration> mWifiConfigurations;
	private WifiLock mWifiLock;

	public WifiUtils(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	// 打开Wifi
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	// 关闭Wifi
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	public int checkState() {
		return mWifiManager.getWifiState();
	}

	public boolean startScan() {
		if (!mWifiManager.startScan()) {
			Log.d(TAG, "start scan failed...");
			return false;
		}
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();

		return true;
	}

	// 得到网络列表
	public List<ScanResult> getResults() {
		return mWifiList;
	}

	// 得到配置好的网络
	public List<WifiConfiguration> getConfigurations() {
		return mWifiConfigurations;
	}

	// 创建一个wifiLock
	public void createWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("WifiTest");
	}

	// 锁定wifiLock
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	// 解锁wifiLock
	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}

	// 连接指定配置好的网络
	public boolean connectConfiguration(int index) {
		if (index > mWifiConfigurations.size()) {
			return false;
		}

		if (!mWifiManager.enableNetwork(index, true)) {
			return false;
		}

		return true;
	}

	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public int getIpAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	public int getNetworkId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// 得到wifiInfo所有信息
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	//添加一个网络连接
	public boolean addNetwork(WifiConfiguration configuration) {
		int wcgId = mWifiManager.addNetwork(configuration);
		if (wcgId < 0) {
			return false;
		}

		if (!mWifiManager.enableNetwork(wcgId, true)) {
			return false;
		}

		return true;
	}
	
	//断开指定ID的网络
	public void disconnectWifi(int index){
		mWifiManager.disableNetwork(index);
		mWifiManager.disconnect();
	}
}
