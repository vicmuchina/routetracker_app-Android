package com.example.routetrackerapp

class ASCIItoHEX() {


    // function to convert ASCII to HEX
    fun toHex(ascii: String): String? {

        // Initialize final String
        var hex: String? = ""

        // Make a loop to iterate through
        // every character of ascii string
        for (i in 0 until ascii.length) {

            // take a char from
            // position i of string
            val ch = ascii[i]

            // cast char to integer and
            // find its ascii value
            val `in` = ch.toInt()

            // change this ascii value
            // integer to hexadecimal value
            val part = Integer.toHexString(`in`)

            // add this hexadecimal value
            // to final string.
            hex += part
        }

        // return the final string hex
        return hex
    }


}