package fr.d2factory.libraryapp.member;

import java.math.BigDecimal;

import fr.d2factory.libraryapp.helper.MemberConstants;

public class Resident extends Member {


	@Override
	public void payBook(int numberOfDays) {
		BigDecimal amountToBePaid = new BigDecimal("0.0");
		amountToBePaid = computeResidentTax(numberOfDays);
		this.setWallet(this.getWallet().subtract(amountToBePaid));
	}

	private BigDecimal computeResidentTax(long daysBetween) {

		// calculate the days to be paid by resident member
		// if daysBetween <= 60 ==> resident must paid 0.1/day
		// if daysBetween > 60 ==> resident pay 0.1*60 and 0.2*the rest
		return (daysBetween <= 60) ? BigDecimal.valueOf(daysBetween).multiply(MemberConstants.MEMBERTARIFF)
				: (BigDecimal.valueOf(daysBetween - 60).multiply(MemberConstants.RESIDENTHIGHERTARIFF))
						.add(MemberConstants.RESIDENTFIRSTSIXTYDAYSCOST);

	}
}
