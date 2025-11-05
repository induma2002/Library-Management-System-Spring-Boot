package com.usj.bookmark.repository;

import com.usj.bookmark.domain.entity.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

	Optional<Book> findByIsbn(String isbn);

	@Query("SELECT b FROM Book b WHERE "
		+ "(:query IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) "
		+ "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) "
		+ "OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%'))) "
		+ "AND (:category IS NULL OR LOWER(b.category) = LOWER(:category))")
	Page<Book> search(@Param("query") String query,
					 @Param("category") String category,
					 Pageable pageable);

	List<Book> findByAvailableCopiesGreaterThan(int minCopies);

	long countByAvailableCopiesGreaterThan(int minCopies);

	@Query("SELECT COALESCE(SUM(b.availableCopies),0) FROM Book b")
	long sumAvailableCopies();
}
