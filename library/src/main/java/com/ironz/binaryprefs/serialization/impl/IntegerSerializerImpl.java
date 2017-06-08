package com.ironz.binaryprefs.serialization.impl;

import com.ironz.binaryprefs.serialization.Serializer;

/**
 * Integer to byte array implementation of {@link Serializer} and backwards
 */
public final class IntegerSerializerImpl implements Serializer<Integer> {

    /**
     * Minimum size primitive type of {@link Integer}
     */
    private static final int SIZE_INT = 5;

    /**
     * Uses for detecting byte array primitive type of {@link Integer}
     */
    private static final byte FLAG_INT = (byte) -3;

    /**
     * Serialize {@code int} into byte array with following scheme:
     * [{@link #FLAG_INT}] + [int_bytes].
     *
     * @param value target int to serialize.
     * @return specific byte array with scheme.
     */
    @Override
    public byte[] serialize(Integer value) {
        int i = 0xff;
        return new byte[]{
                FLAG_INT,
                (byte) ((value >>> 24) & i),
                (byte) ((value >>> 16) & i),
                (byte) ((value >>> 8) & i),
                (byte) ((value) & i)
        };
    }

    /**
     * Deserialize byte by {@link #serialize(Integer)} convention
     *
     * @param key   object token key
     * @param bytes target byte array for deserialization
     * @return deserialized int
     */
    @Override
    public Integer deserialize(String key, byte[] bytes) {
        return deserialize(Serializer.EMPTY_KEY, bytes, 0, SIZE_INT);
    }

    /**
     * Deserialize byte by {@link #serialize(Integer)} convention
     *
     * @param key
     * @param bytes  target byte array for deserialization
     * @param offset offset of bytes array
     * @param length of bytes array part
     * @return deserialized int
     */
    @Override
    public Integer deserialize(String key, byte[] bytes, int offset, int length) {
        int i = 0xff;
        return ((bytes[4 + offset] & i)) +
                ((bytes[3 + offset] & i) << 8) +
                ((bytes[2 + offset] & i) << 16) +
                ((bytes[1 + offset]) << 24);
    }

    @Override
    public boolean isMatches(byte flag) {
        return flag == FLAG_INT;
    }

    @Override
    public boolean isMatches(Object o) {
        return o instanceof Integer;
    }

    @Override
    public int bytesLength() {
        return SIZE_INT;
    }

    @Override
    public byte getFlag() {
        return FLAG_INT;
    }
}