package fr.d2factory.libraryapp.member;

import java.math.BigDecimal;

import fr.d2factory.libraryapp.helper.MemberConstants;

public class Student extends Member {

	boolean isFirstYearStudent;

	@Override
	public void payBook(int numberOfDays) {
		BigDecimal amountToBePaid = new BigDecimal("0.0");
		amountToBePaid = (this.isFirstYearStudent == true) ? computeFirstYearStudentTax(numberOfDays)
				: computeStudentTax(numberOfDays);
		this.setWallet(this.getWallet().subtract(amountToBePaid));

	}

	private BigDecimal computeFirstYearStudentTax(long daysBetween) {
		// calculate the days to be paid after substract 15 days from period to
		// be paid
		// if daysBetween <= 15 ==> daysToBePaid =0
		// if daysBetween > 15 ==> daysToBePaid = daysBetween -15
		// use the daysBetween to know if there is a higher tariff or not
		long daysToBePaid = (daysBetween > 15) ? (daysBetween - 15) : 0;
		return (daysBetween <= 30) ? BigDecimal.valueOf(daysToBePaid).multiply(MemberConstants.MEMBERTARIFF)
				: (BigDecimal.valueOf(daysBetween - 30).multiply(MemberConstants.STUDENTHIGHERTARIFF))
						.add(MemberConstants.FIRSTYEARSTUDENTFIRSTTHIRTYDAYSCOST);

	}

	private BigDecimal computeStudentTax(long daysBetween) {

		// calculate the days to be paid by Student member
		// if daysBetween <= 30 ==> resident must paid 0.1/day
		// if daysBetween > 30 ==> resident pay 0.1*30 and 0.15*the rest
		return (daysBetween <= 30) ? BigDecimal.valueOf(daysBetween).multiply(MemberConstants.MEMBERTARIFF)
				: (BigDecimal.valueOf(daysBetween - 30).multiply(MemberConstants.STUDENTHIGHERTARIFF))
						.add(MemberConstants.STUDENTFIRSTTHIRTYDAYSCOST);

	}

	public boolean isFirstYearStudent() {
		return isFirstYearStudent;
	}

	public void setFirstYearStudent(boolean isFirstYearStudent) {
		this.isFirstYearStudent = isFirstYearStudent;
	}
	
	
}
