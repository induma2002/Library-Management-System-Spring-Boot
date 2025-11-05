package com.usj.bookmark.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books", uniqueConstraints = {
		@UniqueConstraint(name = "uk_books_isbn", columnNames = "isbn")
})
public class Book extends BaseEntity {

	@Column(nullable = false, length = 180)
	private String title;

	@Column(nullable = false, length = 140)
	private String author;

	@Column(nullable = false, length = 32)
	private String isbn;

	@Column(length = 120)
	private String publisher;

	@Column(length = 60)
	private String category;

	@Column(length = 40)
	private String language;

	private Integer publicationYear;

	@Column(nullable = false)
	private Integer totalCopies = 0;

	@Column(nullable = false)
	private Integer availableCopies = 0;

	@Column(nullable = false)
	private boolean referenceOnly = false;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "tag", length = 40)
	private Set<String> tags = new HashSet<>();

	@Version
	private Long version;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Integer getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(Integer totalCopies) {
		this.totalCopies = totalCopies;
	}

	public Integer getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(Integer availableCopies) {
		this.availableCopies = availableCopies;
	}

	public boolean isReferenceOnly() {
		return referenceOnly;
	}

	public void setReferenceOnly(boolean referenceOnly) {
		this.referenceOnly = referenceOnly;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void increaseCopies(int count) {
		totalCopies = totalCopies + count;
		availableCopies = availableCopies + count;
	}

	public void decreaseAvailableCopies() {
		availableCopies = Math.max(0, availableCopies - 1);
	}

	public void increaseAvailableCopies() {
		availableCopies = Math.min(totalCopies, availableCopies + 1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Book other)) {
			return false;
		}
		return Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
}
