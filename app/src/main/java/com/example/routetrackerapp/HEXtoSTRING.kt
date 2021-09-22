package com.example.routetrackerapp

class HEXtoSTRING() {

    fun hexToASCII(hex: String): String? {
        // initialize the ASCII code string as empty.
        var ascii = ""
        var i = 0
        while (i < hex.length) {

            try {
                // extract two characters from hex string
                val part = hex.substring(i, i + 2)

                // change it into base 16 and typecast as the character

                val ch = part.toInt(16).toChar()

                // add this char to final ASCII string
                ascii = ascii + ch
                i += 2
            }catch (e :  NumberFormatException){

                continue

            }
        }
        return ascii
    }

   

}
//.toChar()