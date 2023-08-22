package com.magicfluids.listeners

import javax.microedition.khronos.opengles.*

interface GLWrapper {

    fun wrap(gl: GL?): GL?
}