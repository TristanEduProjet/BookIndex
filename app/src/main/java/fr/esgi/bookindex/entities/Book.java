package fr.esgi.bookindex.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "books",
        indices = {@Index("id"), @Index("authorId")},
        foreignKeys = @ForeignKey(entity = Author.class,
                                  parentColumns = "id",
                                  childColumns = "authorId"))
public class Book {

    @PrimaryKey
    private int id;

    private String title;

    private int authorId;

    private String description;

    //Only authorized to AppDatabase, to create Book use AppDatabase.createBookInDB()
    public Book(int id, String title, int authorId, String description) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
