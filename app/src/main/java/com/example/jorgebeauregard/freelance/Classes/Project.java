package com.example.jorgebeauregard.freelance.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jorge Beauregard on 4/26/2017.
 */

public class Project {
    private int id;
    private String name;
    private String owner;
    private String description;
    private int difficulty;
    private String document;
    private List<String> image;
    private List<String> categories;
    private List<String> collaborators;

    public Project(int id, String name, String owner, String description, int difficulty, String document, List<String> image, List<String> categories, List<String> collaborators) {
        this.setId(id);
        this.setName(name);
        this.setOwner(owner);
        this.setDescription(description);
        this.setDifficulty(difficulty);
        this.setDocument(document);
        this.setImage(image);
        this.setCategories(categories);
        this.setCollaborators(collaborators);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getDocument() {
        return document;
    }

    public List<String> getImage() {
        return image;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }
}
