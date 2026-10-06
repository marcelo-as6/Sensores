package com.example.sensores;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    // Elementos de la pantalla
    private Button btnDescargar;
    private Button btnAsyncTask;
    private ImageView imgResultado;
    private TextView txtOrientacion;
    private TextView txtAsync;

    // Sensor
    private SensorManager sensorManager;
    private Sensor sensorRotacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar la pantalla
        setContentView(R.layout.activity_main);

        // Conectar los elementos del XML
        btnDescargar = findViewById(R.id.btnDescargar);
        btnAsyncTask = findViewById(R.id.btnAsyncTask);
        imgResultado = findViewById(R.id.imgResultado);
        txtOrientacion = findViewById(R.id.txtOrientacion);
        txtAsync = findViewById(R.id.txtAsync);

        // =========================================
        // CONFIGURAR SENSOR DE ROTACIÓN
        // =========================================

        sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorRotacion =
                sensorManager.getDefaultSensor(
                        Sensor.TYPE_ROTATION_VECTOR
                );

        if (sensorRotacion == null) {

            txtOrientacion.setText(
                    "El dispositivo no tiene sensor de rotación."
            );
        }

        // =========================================
        // BOTÓN PARA MOSTRAR EL PUMA
        // =========================================

        btnDescargar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cargarPuma();

            }
        });

        // =========================================
        // BOTÓN ASYNCTASK
        // =========================================

        btnAsyncTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new MiAsyncTask().execute();

            }
        });
    }

    // =============================================
    // THREAD PARA CARGAR EL PUMA
    // =============================================

    private void cargarPuma() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                // Cargar la imagen puma.jpg
                final Bitmap imagenPuma =
                        BitmapFactory.decodeResource(
                                getResources(),
                                R.drawable.puma
                        );

                // Volver al hilo principal
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (imagenPuma != null) {

                            imgResultado.setImageBitmap(imagenPuma);

                            txtAsync.setText(
                                    "Imagen del puma cargada correctamente."
                            );

                        } else {

                            txtAsync.setText(
                                    "No se pudo cargar la imagen."
                            );
                        }
                    }
                });
            }

        }).start();
    }

    // =============================================
    // ACTIVAR SENSOR
    // =============================================

    @Override
    protected void onResume() {

        super.onResume();

        if (sensorRotacion != null) {

            sensorManager.registerListener(
                    this,
                    sensorRotacion,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    // =============================================
    // DESACTIVAR SENSOR
    // =============================================

    @Override
    protected void onPause() {

        super.onPause();

        if (sensorManager != null) {

            sensorManager.unregisterListener(this);
        }
    }

    // =============================================
    // CAMBIO DEL SENSOR
    // =============================================

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() ==
                Sensor.TYPE_ROTATION_VECTOR) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            String datos =
                    "Vector de rotación:\n\n" +
                            "X: " + x + "\n" +
                            "Y: " + y + "\n" +
                            "Z: " + z;

            txtOrientacion.setText(datos);
        }
    }

    // =============================================
    // CAMBIO DE PRECISIÓN DEL SENSOR
    // =============================================

    @Override
    public void onAccuracyChanged(
            Sensor sensor,
            int accuracy) {

        // No necesitamos realizar ninguna acción
    }

    // =============================================
    // ASYNCTASK
    // =============================================

    private class MiAsyncTask
            extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {

            txtAsync.setText(
                    "Ejecutando operación..."
            );
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                // Simular una operación que demora
                Thread.sleep(3000);

                return "Operación terminada correctamente.";

            } catch (InterruptedException e) {

                return "Ocurrió un error.";
            }
        }

        @Override
        protected void onPostExecute(String resultado) {

            txtAsync.setText(resultado);
        }
    }
}


