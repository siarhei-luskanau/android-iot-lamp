package siarhei.luskanau.iot.lamp.android_things.view.display;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.things.contrib.driver.ssd1306.BitmapHelper;
import com.google.android.things.contrib.driver.ssd1306.Ssd1306;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

import siarhei.luskanau.iot.lamp.android_things.R;
import siarhei.luskanau.iot.lamp.domain.interactor.DefaultObserver;
import siarhei.luskanau.iot.lamp.domain.interactor.message.ListenLampMessageUseCase;
import timber.log.Timber;

public class Ssd1306DisplayView implements IDisplayView {

    @NonNull
    private final ListenLampMessageUseCase listenLampMessageUseCase;

    private Ssd1306 ssd1306;
    private final ViewHolder viewHolder;

    public Ssd1306DisplayView(
            @NonNull final ListenLampMessageUseCase listenLampMessageUseCase,
            @NonNull final Context context
    ) {
        this.listenLampMessageUseCase = listenLampMessageUseCase;
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(context).inflate(R.layout.view_display_ssd1306, null, false);
        viewHolder = new ViewHolder(view);
    }

    public void open() {
        testI2cDevices();
        try {
            ssd1306 = new Ssd1306(getI2cName());
        } catch (final Exception e) {
            Timber.e(e);
        }

        listenLampMessageUseCase.execute(
                new DefaultObserver<String>() {

                    @Override
                    public void onNext(final String lampMessage) {
                        setText(lampMessage);
                    }
                },
                null
        );
    }

    public void close() {
        try {
            ssd1306.close();
        } catch (final Exception e) {
            Timber.e(e);
        }
    }

    public void setText(final CharSequence text) {
        if (ssd1306 != null) {
            try {
                viewHolder.textView.setText(text);

                final Bitmap bitmap = loadBitmapFromView(viewHolder.view, ssd1306.getLcdWidth(), ssd1306.getLcdHeight());
                ssd1306.clearPixels();
                BitmapHelper.setBmpData(ssd1306, 0, 0, bitmap, false);
                ssd1306.show();
            } catch (final Exception e) {
                Timber.e(e);
            }
        }
    }

    private String getI2cName() {
        final PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        final List<String> deviceList = peripheralManagerService.getI2cBusList();
        final String bus;
        if (deviceList.isEmpty()) {
            bus = "I2C1";
        } else {
            bus = deviceList.get(0);
        }
        return bus;
    }

    private static Bitmap loadBitmapFromView(final View view, final int lcdWidth, final int lcdHeight) {
        final int measureWidth = View.MeasureSpec.makeMeasureSpec(lcdWidth, View.MeasureSpec.EXACTLY);
        final int measureHeight = View.MeasureSpec.makeMeasureSpec(lcdHeight, View.MeasureSpec.EXACTLY);
        view.measure(measureWidth, measureHeight);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static class ViewHolder {

        private final View view;
        private final TextView textView;

        ViewHolder(final View view) {
            this.view = view;
            textView = view.findViewById(R.id.textView);
        }
    }

    private void testI2cDevices() {
        final PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        final int TEST_REGISTER = 0x0;
        final int MAX_ADDRESS = 256;
        for (int address = 0; address < MAX_ADDRESS; address++) {

            //auto-close the devices
            try (final I2cDevice device = peripheralManagerService.openI2cDevice("I2C1", address)) {

                try {
                    device.readRegByte(TEST_REGISTER);
                    Timber.i("Trying: 0x%02X - SUCCESS", address);
                } catch (final IOException e) {
                    Timber.i("Trying: 0x%02X - FAIL", address);
                }

            } catch (final IOException e) {
                //in case the openI2cDevice(name, address) fails
                Timber.e(e);
            }
        }
    }
}
