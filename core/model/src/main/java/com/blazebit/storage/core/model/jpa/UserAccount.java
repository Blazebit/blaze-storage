package com.blazebit.storage.core.model.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name = "idGenerator", sequenceName = RdbmsConstants.PREFIX + "user_account_seq")
public class UserAccount extends SequenceBaseEntity {

	private static final long serialVersionUID = 1L;

	private String key;
	private String name;
	private Map<String, String> tags = new HashMap<String, String>();
	
	public UserAccount() {
		super();
	}

	@NotNull
	@Column(unique = true)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ElementCollection
	@CollectionTable(name = "user_account_tags", 
		foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "user_account_tags_fk_user_account"),
		joinColumns = {
			@JoinColumn(name = "id", referencedColumnName = "user_id")
	})
	@MapKeyColumn(name = "tag", nullable = false)
	@Column(name = "value", nullable = false)
	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
	
}
