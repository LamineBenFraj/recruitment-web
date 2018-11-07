package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.member.Member;

public class LibraryImpl implements Library {

	BookRepository bookRepository = new BookRepository();

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		Book book = bookRepository.findBook(new ISBN(isbnCode));
		if (book != null) {
			if (bookRepository.hasBooksLate(member))
				throw new HasLateBooksException("Member cannot emprunt another book");
			bookRepository.saveBookBorrow(book, borrowedAt, member);
			return book;
		}
		return null;
	}

	@Override
	public void returnBook(Book book, Member member) {

		LocalDate borrowedBookDate = bookRepository.findBorrowedBookDate(book);
		if (borrowedBookDate != null) {
			int daysBetween = (int) ChronoUnit.DAYS.between(borrowedBookDate, LocalDate.now());
			member.payBook(daysBetween);
			bookRepository.removeBorrowBook(book);
		}

	}

}
