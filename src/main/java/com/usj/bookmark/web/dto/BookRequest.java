package com.usj.bookmark.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class BookRequest {

	@NotBlank
	@Size(max = 180)
	private String title;

	@NotBlank
	@Size(max = 140)
	private String author;

	@NotBlank
	@Pattern(regexp = "[0-9A-Za-z-]{10,20}", message = "must be a valid ISBN format")
	private String isbn;

	@Size(max = 120)
	private String publisher;

	@Size(max = 60)
	private String category;

	@Size(max = 40)
	private String language;

	@Min(1400)
	private Integer publicationYear;

	@NotNull
	@Min(0)
	private Integer totalCopies;

	private boolean referenceOnly;

	private Set<@Size(max = 40) String> tags;

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
}
