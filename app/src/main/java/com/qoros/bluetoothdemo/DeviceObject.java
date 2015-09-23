package com.qoros.bluetoothdemo;

import android.bluetooth.BluetoothClass;
import android.os.ParcelUuid;

import java.util.Arrays;

/**
 * Created by wanghuan on 15/9/23.
 * email: hunter.v.wang@gmail.com
 */
public class DeviceObject {

    private int bondState;
    private String name;
    private String address;

    private int type;
    private BluetoothClass bluetoothClass;
    private ParcelUuid[] uuids;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BluetoothClass getBluetoothClass() {
        return bluetoothClass;
    }

    public void setBluetoothClass(BluetoothClass bluetoothClass) {
        this.bluetoothClass = bluetoothClass;
    }

    public ParcelUuid[] getUuids() {
        return uuids;
    }

    public void setUuids(ParcelUuid[] uuids) {
        this.uuids = uuids;
    }

    public DeviceObject() {
    }

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DeviceObject{" +
                "bondState=" + bondState +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", bluetoothClass=" + bluetoothClass +
                ", uuids=" + Arrays.toString(uuids) +
                '}';
    }
}
