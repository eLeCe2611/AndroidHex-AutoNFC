package com.example.hexcalculator // ¡Tu paquete aquí!

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etHexInput = findViewById<EditText>(R.id.etHexInput)
        val btnCheckUnits = findViewById<Button>(R.id.btnCheckUnits)
        val tvCurrentUnits = findViewById<TextView>(R.id.tvCurrentUnits)

        val etUnitsInput = findViewById<EditText>(R.id.etUnitsInput)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnCopy = findViewById<Button>(R.id.btnCopy)

        // --- CONSULTAR UNIDADES ---
        btnCheckUnits.setOnClickListener {
            val hexString = etHexInput.text.toString().replace(" ", "").uppercase()

            if (hexString.length != 32) {
                Toast.makeText(this, "Error: La cadena debe tener 32 caracteres", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            try {
                val block1Hex = hexString.substring(0, 8)
                val block1Reversed = reverseHexBytes(block1Hex)
                val internalValue = block1Reversed.toLong(16)

                // Dividimos entre 200.0 para ver los decimales de lo que hay guardado
                val realUnits = internalValue / 200.0
                val formattedUnits = String.format(Locale.getDefault(), "%.2f", realUnits)

                tvCurrentUnits.text = "Unidades actuales: $formattedUnits"

            } catch (e: Exception) {
                Toast.makeText(this, "Error al descifrar la cadena", Toast.LENGTH_LONG).show()
            }
        }

        // --- CALCULAR (SUMAR UNIDADES) ---
        btnCalculate.setOnClickListener {
            val hexString = etHexInput.text.toString().replace(" ", "").uppercase()
            val unitsString = etUnitsInput.text.toString()

            if (hexString.length != 32) {
                Toast.makeText(this, "Error: La cadena debe tener 32 caracteres", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (unitsString.isEmpty()) {
                Toast.makeText(this, "Error: Pon el número de unidades a sumar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            try {
                // Ahora cogemos el número que pongas y lo multiplicamos por 200
                val unitsToAdd = unitsString.toLong()
                val internalUnitsToAdd = unitsToAdd * 200

                val block1Hex = hexString.substring(0, 8)
                val block4Hex = hexString.substring(24, 32)

                val block1Reversed = reverseHexBytes(block1Hex)
                val originalValue = block1Reversed.toLong(16)

                val newValue = originalValue + internalUnitsToAdd
                val newHex = String.format("%08X", newValue)

                val newBlock1 = reverseHexBytes(newHex)

                val invertedValue = newValue.xor(0xFFFFFFFFL)
                val invertedHex = String.format("%08X", invertedValue)
                val newBlock2 = reverseHexBytes(invertedHex)

                val newBlock3 = newBlock1

                tvResult.text = "$newBlock1$newBlock2$newBlock3$block4Hex"

            } catch (e: Exception) {
                Toast.makeText(this, "Error en los datos introducidos", Toast.LENGTH_LONG).show()
            }
        }

        // --- COPIAR ---
        btnCopy.setOnClickListener {
            val textoACopiar = tvResult.text.toString()

            if (textoACopiar == "..." || textoACopiar.isEmpty()) {
                Toast.makeText(this, "Aún no hay ningún resultado que copiar", Toast.LENGTH_SHORT).show()
            } else {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Cadena Hexadecimal", textoACopiar)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "¡Copiado al portapapeles!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reverseHexBytes(hex: String): String {
        var reversed = ""
        for (i in hex.length - 2 downTo 0 step 2) {
            reversed += hex.substring(i, i + 2)
        }
        return reversed
    }
}