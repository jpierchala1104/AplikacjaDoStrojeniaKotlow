package com.example.aplikacjadostrojeniakotlow

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_blue_tooth.*

class BlueToothActivity : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1

    companion object{
        const val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blue_tooth)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter == null){
            Toast.makeText(this, "this device does't support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }
        if (!bluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        find_devices_btn.setOnClickListener{ pairedDeviceList() }
    }

    private fun pairedDeviceList(){
        pairedDevices = bluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in pairedDevices) {
                list.add(device)
                Log.i("device", "" + device)
            }
        } else {
            Toast.makeText(this, "no paired bluetooth devices found", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, list)
        device_list.adapter = adapter
        device_list.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                if (bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this,"Bluetooth has been enabled", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bluetooth has beed disabled", Toast.LENGTH_SHORT).show()
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth enabling has been canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}