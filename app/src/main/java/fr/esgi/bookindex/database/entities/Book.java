package fr.esgi.bookindex.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "books",
        indices = {@Index("id"), @Index("authorId")},
        foreignKeys = @ForeignKey(entity = Author.class, parentColumns = "id", childColumns = "authorId"))
public class Book {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String title;
    private Integer authorId;
    private String description;

    /**
     * Default Constructor
     *
     * Room Database will use this no-arg constructor by default.
     * The others are annotated with @Ignore,
     * so Room will not give a warning about "Multiple Good Constructors".
     */
    public Book() {
    }

    //@Ignore
    //Only authorized to AppDatabase, to create Book use AppDatabase.createBookInDB()
    public Book(Integer id, String title, Integer authorId, String description) {
        this(title, authorId, description);
        this.id = id;
    }

    @Ignore
    public Book(String title, Integer authorId, String description) {
        this.title = title;
        this.authorId = authorId;
        this.description = description;
    }

    @Ignore
    public Book(final String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
