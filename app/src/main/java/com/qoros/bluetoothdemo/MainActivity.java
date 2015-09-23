package com.qoros.bluetoothdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Andy Wang 2816100863@qq.com
 */

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    private final static int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private List<DeviceObject> listHistoryDevices = new ArrayList<>();
    private List<DeviceObject> listDevices = new ArrayList<>();

    @AfterViews
    void afterViews(){
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter startedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter finishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(receiver , foundFilter);
        this.registerReceiver(receiver , startedFilter);
        this.registerReceiver(receiver , finishedFilter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)return;
            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
                append("开始扫描");
                if(listDevices.size()>0){
                    listDevices.clear();
                }
            }
            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                append("扫描发现:");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                DeviceObject deviceObject = new DeviceObject();
                deviceObject.setName(device.getName());
                deviceObject.setAddress(device.getAddress());
                deviceObject.setBondState(device.getBondState());
                deviceObject.setType(device.getType());
                deviceObject.setBluetoothClass(device.getBluetoothClass());
                deviceObject.setUuids(device.getUuids());
                listDevices.add(deviceObject);

                if(device.getBondState() == BluetoothDevice.BOND_BONDED){
                    append("已经绑定过的设备:" + device.getName());
                }else{
                    append("新发现设备:" + device.getName() + " 地址:" + device.getAddress());
                }
            }
            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                append("结束扫描");

            }
        }
    };

    @ViewById(R.id.info)
    TextView info;

    @Click(R.id.connect)
    void connect(){
        if(listDevices.size()>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("匹配设备列表:");
            builder.setNegativeButton("取消",null);
            String[] items = new String[listDevices.size()];
            for(int i=0; i< listDevices.size();i++){
                if(listDevices.get(i) != null && listDevices.get(i).getName() != null){
                    items[i] = listDevices.get(i).getName();
                }else{
                    items[i] = "未知名称";
                }
            }
            builder.setSingleChoiceItems(items , 0 ,devicesClick);
            builder.show();
        }
    }

    private DialogInterface.OnClickListener devicesClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            connectDevice(listDevices.get(which));
        }
    };

    private void connectDevice(DeviceObject deviceObject){
        append(deviceObject.getName() + " 连接设备中...");
    }

    @Click(R.id.close)
    void close(){
        append("点击关闭蓝牙");
        bluetoothAdapter.disable();
    }

    @Click(R.id.start)
    void start(){
        append("点击开始扫描");
        bluetoothAdapter.startDiscovery();
    }

    @Click(R.id.check)
    void check(){
        if(bluetoothAdapter == null){
            append("本机不支持蓝牙");
            return;
        }
        if(!bluetoothAdapter.isEnabled()){
            append("本机蓝牙未打开");
        }else{
            append("本机蓝牙已经打开");
            append("蓝牙名称:" + bluetoothAdapter.getName());
            append("蓝牙地址:" + bluetoothAdapter.getAddress());
        }
    }

    @Click(R.id.open)
    void open(){
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent , REQUEST_ENABLE_BLUETOOTH);
    }

    @Click(R.id.discoverable)
    void discoverable(){
        if(bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION , 300);
            startActivity(discoverableIntent);
            append("本机蓝牙 300 秒内可见");
        }else{
            append("已经处于可发现状态");
        }
    }

    @Click(R.id.list)
    void list(){
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if(devices.size() > 0){
            append(" 历史匹配设备如下:");
            if(listHistoryDevices.size()>0){
                listHistoryDevices.clear();
            }
            for (BluetoothDevice device : devices){
                append("--------------------");
                append("name:" + device.getName());
                append("address:" + device.getAddress());
                append("status:" + device.getBondState());
                DeviceObject deviceObject = new DeviceObject();
                deviceObject.setName(device.getName());
                deviceObject.setAddress(device.getAddress());
                deviceObject.setBondState(device.getBondState());
                deviceObject.setType(device.getType());
                deviceObject.setBluetoothClass(device.getBluetoothClass());
                deviceObject.setUuids(device.getUuids());
                listHistoryDevices.add(deviceObject);
            }
            if(listHistoryDevices.size()>0){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("历史匹配设备:");
                builder.setNegativeButton("取消",null);
                String[] items = new String[listHistoryDevices.size()];
                for(int i=0; i< listHistoryDevices.size();i++){
                    items[i] = listHistoryDevices.get(i).getName();
                }
                builder.setSingleChoiceItems(items , 0 ,historyDevicesClick);
                builder.show();
            }
        }else{
            append("之前没有 已经被匹配的设备");
        }
    }

    private DialogInterface.OnClickListener historyDevicesClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BLUETOOTH){
            append("蓝牙打开成功 ");

        }
    }

    private void append(String message){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        info.append("\n" + message);
    }
}
