package com.grayzone.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressUtils {
  public static String extractKeyAddress(String address) {
    log.debug("extractKeyAddress {}", address);
    if (address == null || address.isBlank()) return null;

    String[] parts = address.split(" ");
    StringBuilder keyAddressBuilder = new StringBuilder();

    for (String part : parts) {
      if (part == null || part.isBlank()) continue;
      keyAddressBuilder.append(part).append(" ");

      char lastChar = part.charAt(part.length() - 1);
      if ("동읍면가로".indexOf(lastChar) >= 0) {
        break;
      }
    }

    String keyAddress = keyAddressBuilder.toString().trim();

    char lastChar = keyAddress.charAt(keyAddress.length() - 1);
    if ("동읍면가로".indexOf(lastChar) == -1) return null;

    return keyAddress;
  }
}
