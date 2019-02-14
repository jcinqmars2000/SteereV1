package com.steereengineering.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;



@Data
@Entity
@Table(name = "manual")
public class Manual {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "manual_id")
	private Long manual_id;
	@Column(name = "name")
	private String name;
	@Column(name = "type")
	private String type;
	@Column(name = "path")
    private String path;

}