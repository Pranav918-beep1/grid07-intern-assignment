package com.grid07.intern.model;

import jakarta.persistence.*;

@Entity
public class Bot 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String name;
      private String personaDescription;
    
    public Bot() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getName() { return name; }
    public void setName(String n) { name = n; }
    
    public String getPersonaDescription() { return personaDescription; }
    
    public void setPersonaDescription(String pd) { 
        this.personaDescription = pd; 
    }
}
