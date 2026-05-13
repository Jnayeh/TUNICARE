package com.qatra.donationplatform.emergency.core.domain;

import com.qatra.donationplatform.shared.domain.enums.BloodType;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class BloodCompatibility {

  private BloodCompatibility() {}

  public static Set<BloodType> compatibleDonorTypes(BloodType needed) {
    if (needed == null || needed == BloodType.UNKNOWN) {
      return EnumSet.allOf(BloodType.class);
    }
    return switch (needed) {
      case O_NEGATIVE -> EnumSet.of(BloodType.O_NEGATIVE);
      case O_POSITIVE -> EnumSet.of(BloodType.O_NEGATIVE, BloodType.O_POSITIVE);
      case A_NEGATIVE -> EnumSet.of(BloodType.O_NEGATIVE, BloodType.A_NEGATIVE);
      case A_POSITIVE ->
          EnumSet.of(BloodType.O_NEGATIVE, BloodType.O_POSITIVE, BloodType.A_NEGATIVE, BloodType.A_POSITIVE);
      case B_NEGATIVE -> EnumSet.of(BloodType.O_NEGATIVE, BloodType.B_NEGATIVE);
      case B_POSITIVE ->
          EnumSet.of(BloodType.O_NEGATIVE, BloodType.O_POSITIVE, BloodType.B_NEGATIVE, BloodType.B_POSITIVE);
      case AB_NEGATIVE ->
          EnumSet.of(
              BloodType.O_NEGATIVE,
              BloodType.A_NEGATIVE,
              BloodType.B_NEGATIVE,
              BloodType.AB_NEGATIVE);
      case AB_POSITIVE -> EnumSet.complementOf(EnumSet.of(BloodType.UNKNOWN));
      case UNKNOWN -> EnumSet.allOf(BloodType.class);
    };
  }

  public static Set<String> compatibleDonorTypeNames(BloodType needed) {
    return compatibleDonorTypes(needed).stream().map(Enum::name).collect(Collectors.toSet());
  }
}
