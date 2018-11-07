package fr.d2factory.libraryapp.library;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.helper.MemberConstants;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import junit.framework.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LibraryTest {
	private static Library library;
	private static BookRepository bookRepository;

	@BeforeClass
	public static void setup() {
		// instantiate the library
		library = new LibraryImpl();
		// instantiate the bookRepository
		bookRepository = new BookRepository();
		List<Book> availableBookList = new ArrayList<>();

		try {
			
			ObjectMapper mapper = new ObjectMapper();
			JsonFactory f = new JsonFactory();
			List<Book> books = null;
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("books.json");
			JsonParser jsonParser = f.createJsonParser(is);
			TypeReference<List<Book>> typeReference = new TypeReference<List<Book>>() {
			};
			availableBookList = mapper.readValue(jsonParser, typeReference);

			// set of bookRepository
			bookRepository.addBooks(availableBookList);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void A_member_can_borrow_a_book_if_book_is_available() {

		Resident resident = new Resident();
		resident.setIdMember(1234);
		resident.setWallet(new BigDecimal("10.0"));
		Book bookBorrow = library.borrowBook(465789453149L, resident, LocalDate.of(2018, 9, 6));
		Assert.assertNotNull(bookBorrow);

	}

	@Test
	public void B_borrowed_book_is_no_longer_available() {
		Resident resident = new Resident();
		resident.setIdMember(1234);
		resident.setWallet(new BigDecimal("10.0"));
		Book bookBorrow = library.borrowBook(465789453149L, resident, LocalDate.of(2018, 11, 6));
		Assert.assertNull(bookBorrow);
	}

	@Test
	public void C_residents_are_taxed_10cents_for_each_day_they_keep_a_book() {

		BigDecimal payedAmount = new BigDecimal("0.0");
		Resident resident = new Resident();
		resident.setIdMember(1234);
		resident.setWallet(new BigDecimal("10.0"));
		BigDecimal startWallet = resident.getWallet();
		resident.payBook(6);
		Assert.assertEquals(resident.getWallet(),
				startWallet.subtract(BigDecimal.valueOf(6).multiply(new BigDecimal("0.1"))));
	}

	@Test
	public void D_students_pay_10_cents_the_first_30days() {

		BigDecimal payedAmount = new BigDecimal("0.0");
		Student student = new Student();
		student.setIdMember(1235);
		student.setWallet(new BigDecimal("10.0"));
		BigDecimal startWallet = student.getWallet();
		student.payBook(6);
		Assert.assertEquals(student.getWallet(),
				startWallet.subtract(BigDecimal.valueOf(6).multiply(new BigDecimal("0.1"))));

	}

	@Test
	public void E_students_in_1st_year_are_not_taxed_for_the_first_15days() {

		BigDecimal payedAmount = new BigDecimal("0.0");
		Student student = new Student();
		student.setIdMember(1235);
		student.setFirstYearStudent(true);
		student.setWallet(new BigDecimal("10.0"));
		BigDecimal startWallet = student.getWallet();
		student.payBook(15);
		Assert.assertEquals(student.getWallet(), startWallet);

	}

	@Test
	public void F_students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {

		BigDecimal payedAmount = new BigDecimal("0.0");
		Student student = new Student();
		student.setIdMember(1235);
		student.setWallet(new BigDecimal("10.0"));
		BigDecimal startWallet = student.getWallet();
		student.payBook(31);
		Assert.assertEquals(student.getWallet(), startWallet
				.subtract(BigDecimal.valueOf(30).multiply(new BigDecimal("0.1"))).subtract(new BigDecimal("0.15")));
	}

	@Test
	public void G_residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {

		BigDecimal payedAmount = new BigDecimal("0.0");
		Resident resident = new Resident();
		resident.setIdMember(1234);
		resident.setWallet(new BigDecimal("10.0"));
		BigDecimal startWallet = resident.getWallet();
		resident.payBook(61);
		Assert.assertEquals(resident.getWallet(), startWallet
				.subtract(BigDecimal.valueOf(60).multiply(new BigDecimal("0.1"))).subtract(new BigDecimal("0.2")));
	}

	@Test(expected = HasLateBooksException.class)
	public void H_members_cannot_borrow_book_if_they_have_late_books() {

		Resident resident = new Resident();
		resident.setIdMember(1234);
		resident.setWallet(new BigDecimal("10.0"));
		Book bookBorrow = library.borrowBook(3326456467846L, resident, LocalDate.of(2018, 11, 6));

	}
}
