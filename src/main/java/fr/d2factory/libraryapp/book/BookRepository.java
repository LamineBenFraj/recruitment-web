package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {

	private static Map<ISBN, Book> availableBooks = new HashMap<>();
	private static Map<Book, Map<Member, LocalDate>> borrowedBooks = new HashMap<>();

	public void addBooks(List<Book> books) {

		for (Book newBook : books) {
			this.availableBooks.computeIfAbsent(newBook.getIsbn(), x -> newBook);
		}
	}

	
	public Book findBook(ISBN isbnCode) {

		Book book = null;
		if (!availableBooks.isEmpty())
			book = availableBooks.entrySet().stream().filter(e -> e.getKey().getIsbnCode() == (isbnCode.getIsbnCode()))
					.map(Map.Entry::getValue).findFirst().orElse(null);
		return book;
	}
	

	public Book findBorrowBook(ISBN isbnCode) {

		Book book = null;
		if (!borrowedBooks.isEmpty())
			book = borrowedBooks.entrySet().stream()
					.filter(e -> e.getKey().getIsbn().getIsbnCode() == (isbnCode.getIsbnCode())).map(Map.Entry::getKey)
					.findFirst().orElse(null);
		return book;
	}

	
	public void saveBookBorrow(Book book, LocalDate borrowedAt, Member member) {
		Map<Member, LocalDate> map = new HashMap<>();
		map.put(member, borrowedAt);
		if (findBook(book.getIsbn()) != null) {
			availableBooks.remove(book.getIsbn());
		}
		this.borrowedBooks.putIfAbsent(book, map);

	}
	

	public void removeBorrowBook(Book book) {
		this.borrowedBooks.remove(book);
	}

	
	public LocalDate findBorrowedBookDate(Book book) {
		LocalDate borrowedAt = null;

		if (this.borrowedBooks.containsKey(book))
			borrowedAt = this.borrowedBooks.get(book).entrySet().stream().findFirst().get().getValue();

		return borrowedAt;
	}

	
	public boolean hasBooksLate(Member member) {

		long daysBetween = 0;
		if (borrowedBooks.size() == 0)
			return false;

		for (Entry<Book, Map<Member, LocalDate>> book : borrowedBooks.entrySet()) {
			if (member.getIdMember()
					.equals(book.getValue().entrySet().stream().findFirst().get().getKey().getIdMember())) {
				daysBetween = ChronoUnit.DAYS.between(book.getValue().entrySet().stream().findFirst().get().getValue(),
						LocalDate.now());
				if (member.getClass().equals(Student.class) && daysBetween > 30) {
					return true;
				} else if (member.getClass().equals(Resident.class) && daysBetween > 60) {
					return true;
				}
			}
		}
		return false;
	}

}
