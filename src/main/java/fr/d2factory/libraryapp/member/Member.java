package fr.d2factory.libraryapp.member;

import java.math.BigDecimal;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    
	private Integer idMember;
	/**
     * An initial sum of money the member has
     */
    private BigDecimal wallet;

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);
    

	public Integer getIdMember() {
		return idMember;
	}

	public void setIdMember(Integer idMember) {
		this.idMember = idMember;
	}


	public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }
	
    public BigDecimal getWallet() {
        return wallet;
    }

}
