package com.usj.bookmark.config;

import com.usj.bookmark.domain.entity.Book;
import com.usj.bookmark.domain.entity.Member;
import com.usj.bookmark.domain.enums.MemberStatus;
import com.usj.bookmark.repository.BookRepository;
import com.usj.bookmark.repository.MemberRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	public DataInitializer(BookRepository bookRepository, MemberRepository memberRepository) {
		this.bookRepository = bookRepository;
		this.memberRepository = memberRepository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (bookRepository.count() == 0) {
			Book cleanCode = new Book();
			cleanCode.setTitle("Clean Code");
			cleanCode.setAuthor("Robert C. Martin");
			cleanCode.setIsbn("9780132350884");
			cleanCode.setPublisher("Prentice Hall");
			cleanCode.setCategory("Software");
			cleanCode.setLanguage("EN");
			cleanCode.setPublicationYear(2008);
			cleanCode.setTotalCopies(5);
			cleanCode.setAvailableCopies(5);
			cleanCode.setTags(Set.of("software", "clean-code", "best-practice"));
			bookRepository.save(cleanCode);

			Book designPatterns = new Book();
			designPatterns.setTitle("Design Patterns");
			designPatterns.setAuthor("Erich Gamma");
			designPatterns.setIsbn("9780201633610");
			designPatterns.setPublisher("Addison-Wesley");
			designPatterns.setCategory("Software");
			designPatterns.setLanguage("EN");
			designPatterns.setPublicationYear(1994);
			designPatterns.setTotalCopies(3);
			designPatterns.setAvailableCopies(3);
			designPatterns.setTags(Set.of("software", "patterns"));
			bookRepository.save(designPatterns);
		}

		if (memberRepository.count() == 0) {
			Member alice = new Member();
			alice.setMembershipId("MEM-001");
			alice.setFullName("Alice Johnson");
			alice.setEmail("alice@example.com");
			alice.setPhoneNumber("+12025550111");
			alice.setStatus(MemberStatus.ACTIVE);
			memberRepository.save(alice);

			Member bob = new Member();
			bob.setMembershipId("MEM-002");
			bob.setFullName("Bob Smith");
			bob.setEmail("bob@example.com");
			bob.setPhoneNumber("+12025550112");
			bob.setStatus(MemberStatus.ACTIVE);
			memberRepository.save(bob);
		}
	}
}
