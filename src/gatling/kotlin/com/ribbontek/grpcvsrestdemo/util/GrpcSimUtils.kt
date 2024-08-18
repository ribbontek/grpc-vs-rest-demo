package com.ribbontek.grpcvsrestdemo.util

import sun.misc.Unsafe

object GrpcSimUtils {
    /**
     * Reflection Util function used for the gRPC simulation that overrides private static final fields
     */
    fun setStaticFinalField(
        clazz: Class<*>,
        fieldName: String,
        newValue: Any,
    ) {
        val declaredField = clazz.getDeclaredField(fieldName).apply { isAccessible = true }
        val unsafe = getUnsafeInstance()
        unsafe.putObject(unsafe.staticFieldBase(declaredField), unsafe.staticFieldOffset(declaredField), newValue)
    }

    private fun getUnsafeInstance(): Unsafe {
        return Unsafe::class.java.getDeclaredField("theUnsafe").apply { isAccessible = true }.get(null) as Unsafe
    }
}
