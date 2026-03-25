package com.example.hexcalculator
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Conectamos el código con el diseño
        val etHexInput = findViewById<EditText>(R.id.etHexInput)
        val etUnitsInput = findViewById<EditText>(R.id.etUnitsInput)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // Le decimos al botón qué hacer cuando se pulsa
        btnCalculate.setOnClickListener {

            // Cogemos los textos, quitamos los espacios que el usuario haya puesto y pasamos a mayúsculas
            val hexString = etHexInput.text.toString().replace(" ", "").uppercase()
            val unitsString = etUnitsInput.text.toString()

            // Comprobaciones de seguridad
            if (hexString.length != 32) {
                Toast.makeText(this, "Error: La cadena debe tener exactamente 32 caracteres", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (unitsString.isEmpty()) {
                Toast.makeText(this, "Error: Pon el número de unidades a sumar", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            try {
                // --- INICIO DE LA LÓGICA MATEMÁTICA ---

                val unitsToAdd = unitsString.toLong()
                val internalUnitsToAdd = unitsToAdd * 2 // Regla: sumar el doble del valor real

                // Separamos los bloques necesarios
                val block1Hex = hexString.substring(0, 8)
                val block4Hex = hexString.substring(24, 32)

                // Le damos la vuelta al Bloque 1 (Little-Endian) y pasamos a Decimal
                val block1Reversed = reverseHexBytes(block1Hex)
                val originalValue = block1Reversed.toLong(16)

                // Sumamos
                val newValue = originalValue + internalUnitsToAdd

                // Volvemos a pasar a Hexadecimal asegurando 8 caracteres
                val newHex = String.format("%08X", newValue)

                // CONSTRUIMOS LOS NUEVOS BLOQUES
                // Bloque 1: Little-Endian del nuevo valor
                val newBlock1 = reverseHexBytes(newHex)

                // Bloque 2: Inverso a nivel de bits (NOT lógico)
                val invertedValue = newValue.xor(0xFFFFFFFFL)
                val invertedHex = String.format("%08X", invertedValue)
                val newBlock2 = reverseHexBytes(invertedHex)

                // Bloque 3: Copia exacta del Bloque 1
                val newBlock3 = newBlock1

                // Unimos todo SIN espacios, tal y como has pedido
                tvResult.text = "$newBlock1$newBlock2$newBlock3$block4Hex"

            } catch (e: Exception) {
                Toast.makeText(this, "Error en los datos introducidos", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Herramienta matemática para darle la vuelta a los pares de bytes (Little-Endian)
    private fun reverseHexBytes(hex: String): String {
        var reversed = ""
        for (i in hex.length - 2 downTo 0 step 2) {
            reversed += hex.substring(i, i + 2)
        }
        return reversed
    }
}