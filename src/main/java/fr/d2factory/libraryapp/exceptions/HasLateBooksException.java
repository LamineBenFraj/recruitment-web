package fr.d2factory.libraryapp.exceptions;

/**
 * This exception is thrown when a member who owns late books tries to borrow another book
 * this exception will be used in service layer that manage repository
 */
public class HasLateBooksException extends RuntimeException  {

	/** exception message details  */
	String messageDetail;

	/**
	 * @param messageDetails
	 */
	public HasLateBooksException(String messageDetails) {
	
		this.messageDetail = messageDetails;
	}

	public HasLateBooksException() {
	}

	public String getDeveloperMessage() {
		return messageDetail;
	}

	public void setDeveloperMessage(String messageDetails) {
		this.messageDetail = messageDetails;
	}

}
