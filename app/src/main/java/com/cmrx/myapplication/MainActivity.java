package com.cmrx.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        int sim1Subid = getSubidBySlotId(this, 0);
        int sim2Subid = getSubidBySlotId(this, 1);
        tm.listen(new listener1(sim1Subid), PhoneStateListener.LISTEN_CELL_LOCATION);
        tm.listen(new listener2(sim2Subid), PhoneStateListener.LISTEN_CELL_LOCATION);


    }

    //SIM卡1的基站信息监听
    public class listener1 extends PhoneStateListener {
        public listener1(int subId) {
            Field field;
            Class<PhoneStateListener> cl = (Class<PhoneStateListener>) this.getClass().getSuperclass();
            try {
                field = cl.getDeclaredField("mSubId");
                field.setAccessible(true);
                field.set(this, subId);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            signalStrength.toString();
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            GsmCellLocation location1 = (GsmCellLocation) location;
            String s = location1.getLac() + "lac" + location1.getCid();
            Log.i("1-------", s);
            location.toString();
        }
    }

    //SIM卡2的基站信息监听
    public class listener2 extends PhoneStateListener {
        public listener2(int subId) {
            Field field;
            Class<PhoneStateListener> cl = (Class<PhoneStateListener>) this.getClass().getSuperclass();
            try {
                field = cl.getDeclaredField("mSubId");
                field.setAccessible(true);
                field.set(this, subId);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            signalStrength.toString();
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            GsmCellLocation location1 = (GsmCellLocation) location;
            String s = location1.getLac() + "lac" + location1.getCid() + location1;
            Log.i("2-------", s);
            location.toString();
        }
    }

    /**
     * 通过卡槽id（slotid）获取相应卡的id（subid）
     *
     * @param context
     * @param slotId
     * @return
     */
    public static int getSubidBySlotId(Context context, int slotId) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(subscriptionManager.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimState = telephonyClass.getMethod("getSubId", parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotId;
            Object ob_phone = getSimState.invoke(subscriptionManager, obParameter);
            if (ob_phone != null) {
                Log.d("Mactivity:  ", "slotId:" + slotId + ";" + ((int[]) ob_phone)[0]);
                return ((int[]) ob_phone)[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
