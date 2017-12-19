package com.hexer.util;

import lombok.experimental.UtilityClass;

import javax.xml.bind.DatatypeConverter;

/**
 * {@code HexUtil} utility class.
 */
@UtilityClass
public class HexUtil {

    /**
     * Convert byte hash to string hash.
     *
     * @param bytes defines byte hash
     * @return defines string hash
     */
    public String toHex(final byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes).toLowerCase();
    }

}
