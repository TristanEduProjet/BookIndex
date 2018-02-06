package fr.esgi.bookindex.database.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "authors",
        indices = {@Index("firstName"), @Index("lastName")})
public class Author {

    @PrimaryKey
    private Integer id;
    private String firstName;
    private String lastName;

    /**
     * Default Constructor
     *
     * Room Database will use this no-arg constructor by default.
     * The others are annotated with @Ignore,
     * so Room will not give a warning about "Multiple Good Constructors".
     */
    public Author() {
    }

    //@Ignore
    //Only authorized to AppDatabase, to create Author use AppDatabase.createAuthorInDB()
    public Author(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Ignore
    public Author(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
