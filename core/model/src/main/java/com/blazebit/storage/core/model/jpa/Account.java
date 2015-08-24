package com.blazebit.storage.core.model.jpa;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity
@SequenceGenerator(name = "idGenerator", sequenceName = RdbmsConstants.PREFIX + "account_seq")
public class Account extends SequenceBaseEntity {

	private static final long serialVersionUID = 1L;

	private String key;
	private String name;
	private User owner;
	private Map<String, String> tags = new HashMap<String, String>();
	
	public Account() {
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

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "account_fk_owner"))
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ElementCollection
	@CollectionTable(name = "account_tags", 
		foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "account_tags_fk_user_account"),
		joinColumns = {
			@JoinColumn(name = "account_id", referencedColumnName = "id")
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